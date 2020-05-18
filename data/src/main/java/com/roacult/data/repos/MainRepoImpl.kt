package com.roacult.data.repos

import com.google.gson.Gson
import com.roacult.data.local.MainLocal
import com.roacult.data.local.entities.toLocalEntity
import com.roacult.data.remote.AuthLocal
import com.roacult.data.remote.MainRemote
import com.roacult.data.remote.entities.toRemote
import com.roacult.data.utils.getNext
import com.roacult.data.utils.toRemote
import com.roacult.domain.entities.Categorie
import com.roacult.domain.entities.Declaration
import com.roacult.domain.entities.DeclarationState
import com.roacult.domain.entities.User
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.domain.repos.MainRepo
import com.roacult.domain.usecases.declaration.DeclarationPage
import com.roacult.domain.usecases.declaration.DeclarationPageParam
import com.roacult.domain.usecases.profile.AddDocumentsParams
import com.roacult.domain.usecases.profile.ChangePasswordParam
import com.roacult.domain.usecases.profile.EditInfoParams
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.functional.map
import com.roacult.kero.team7.jstarter_domain.interactors.None
import io.reactivex.Observable
import kotlin.coroutines.suspendCoroutine

class MainRepoImpl(
    private val mainLocal : MainLocal,
    private val authLocal: AuthLocal,
    private val gson : Gson,
    private val mainRemote : MainRemote
) : MainRepo {

    /**
     * get user entity local observable
     * */
    override fun getUserObservable(): Observable<Either<ProfileFailures, User>> {
        return mainLocal.getUserObservable()
    }

    /**
     * put new user data to server and store data locally
     * */
    override suspend fun editInfo(editUserInfo: EditInfoParams): Either<ProfileFailures, None> {

        //put new data to server
        val responce = mainRemote.putUserInfo(editUserInfo,authLocal.getToken())
        if(responce is Either.Left) return responce
        val user = (responce as Either.Right).b.toEntity()

        //store new user information locally
        authLocal.storeUser(user.toLocalEntity(),gson)

        return Either.Right(None())
    }

    /**
     * update current user password
     * */
    override suspend fun changePassword(changePasswordParam: ChangePasswordParam): Either<ProfileFailures, None> {
        return mainRemote.updatePassword(changePasswordParam.toRemote(),authLocal.getToken())
    }

    /**
     * fetch all declaration types
     * */
    override suspend fun fetchCategories(): Either<DeclarationFailure, List<Categorie>> {
        return mainRemote.fetchCategories(authLocal.getToken()).map {
            it.map { it.toCategorie() }
        }
    }

    /**
     * submit declaration (create declaration then upload all docs)
     * */
    override suspend fun submitDeclaration(declaration: Declaration): Either<DeclarationFailure, None> {

        //get token & user from local storage
        val token = authLocal.getToken()
        val user = mainLocal.getUser()

        // post new instance of declaration in remote
        val newDeclaration = declaration.copy(citizen = user.idu)
        val responce1 = mainRemote.submitDeclaration(token,newDeclaration.toRemote())
        if(responce1 is Either.Left) return responce1

        //remote declaration instance
        val remoteDeclaration = (responce1 as Either.Right).b

        //post all files
        for (attachment in newDeclaration.attachment) {
            val responce = mainRemote.postDoc(token,attachment.toRemote(remoteDeclaration.id))
            if(responce is Either.Left) return responce
        }

        //if every thing went ok we return success
        return Either.Right(None())
    }

    /**
     * fetch declaration page
     * */
    override suspend fun fetchDeclrations(page: DeclarationPageParam): Either<DeclarationFailure, DeclarationPage> {
        return mainRemote.fetchDeclarations(authLocal.getToken(),page).map {
            DeclarationPage(
                it.count,
                it.next?.getNext(),
                it.previous?.getNext(),
                it.results.map {
                    it.toDeclaration()
                }
            )
        }
    }

    /**
     * fetch user declaration page
     * */
    override suspend fun fetchUserDeclrations(page: Int): Either<DeclarationFailure, DeclarationPage> {
        val user = mainLocal.getUser()
        val token = authLocal.getToken()
        return mainRemote.fetchUserDeclarations(token,user.idu,page = page).map {
            DeclarationPage(
                it.count,
                it.next?.getNext(),
                it.previous?.getNext(),
                it.results.map {
                    it.toDeclaration()
                }
            )
        }
    }

    /**
     * fetch unfinished user declaration page
     * */
    override suspend fun fetchUnfinishedUserDeclrations(page: Int): Either<DeclarationFailure, DeclarationPage> {
        val user = mainLocal.getUser()
        val token = authLocal.getToken()
        //TODO fetch not_validated to
        return mainRemote.fetchUserDeclarations(
            token,user.idu,
            DeclarationState.LACK_OF_INFO.toRemote(),
            DeclarationState.NOT_VALIDATED.toRemote(),
            page
        ).map {
            DeclarationPage(
                it.count,
                it.next?.getNext(),
                it.previous?.getNext(),
                it.results.map {
                    it.toDeclaration()
                }
            )
        }
    }

    /**
     * post new documents
     * */
    override suspend fun postDocuments(documentsParams: AddDocumentsParams): Either<DeclarationFailure, None> {

        val token = authLocal.getToken()
        val user = mainLocal.getUser()

        // put new data to declaration
        val responce = mainRemote.putNewData(token,user.idu,documentsParams.delcaration)
        if(responce is Either.Left) return responce

        //post all files
        for (attachment in documentsParams.docs) {
            val responce2 = mainRemote.postDoc(token,attachment.toRemote(documentsParams.delcaration.id))
            if(responce2 is Either.Left) return responce2
        }

        return Either.Right(None())
    }
}
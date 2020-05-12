package com.roacult.data.repos

import com.google.gson.Gson
import com.roacult.data.local.MainLocal
import com.roacult.data.local.entities.toLocalEntity
import com.roacult.data.remote.AuthLocal
import com.roacult.data.remote.MainRemote
import com.roacult.data.remote.entities.toRemote
import com.roacult.domain.entities.Categorie
import com.roacult.domain.entities.User
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.domain.repos.MainRepo
import com.roacult.domain.usecases.declaration.SubmitionParams
import com.roacult.domain.usecases.profile.ChangePasswordParam
import com.roacult.domain.usecases.profile.EditInfoParams
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.functional.map
import com.roacult.kero.team7.jstarter_domain.interactors.None
import io.reactivex.Observable

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
    override suspend fun submitDeclaration(submitionParams: SubmitionParams): Either<DeclarationFailure, None> {

        //get token from local storage
        val token = authLocal.getToken()

        // post new instance of declaration in remote
        val responce1 = mainRemote.submitDeclaration(token,submitionParams.declaration.toRemote())
        if(responce1 is Either.Left) return responce1

        //remote declaration instance
        val remoteDeclaration = (responce1 as Either.Right).b

        //post all files
        for (file in submitionParams.submitionDocs) {
            val type = if(file.endsWith(".pdf")) "pdf"
            else if (
                file.endsWith(".png") ||
                file.endsWith(".PNG") ||
                file.endsWith(".jpg") ||
                file.endsWith(".JPG") ||
                file.endsWith(".gif") ||
                file.endsWith(".GIF")
            ) "image"  else "other"
            val responce = mainRemote.postDoc(token,file,type,remoteDeclaration.id)
            if(responce is Either.Left) return responce
        }

        //if every thing went ok we return success
        return Either.Right(None())
    }
}
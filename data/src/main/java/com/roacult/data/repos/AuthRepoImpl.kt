package com.roacult.data.repos

import com.google.gson.Gson
import com.roacult.data.local.entities.toLocalEntity
import com.roacult.data.remote.AuthLocal
import com.roacult.data.remote.AuthRemote
import com.roacult.domain.exceptions.AuthFailure
import com.roacult.domain.repos.AuthRepo
import com.roacult.domain.usecases.auth.LoginParams
import com.roacult.domain.usecases.auth.RegistrationParams
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.None

class AuthRepoImpl(
    private val authRemote: AuthRemote,
    private val authLocal : AuthLocal,
    private val gson : Gson
) : AuthRepo {

    /**
     * login and store token and user entity in local storage
     * */
    override suspend fun logUserIn(loginParams: LoginParams): Either<AuthFailure, None> {

        // log user in with email and password
        val responce1 = authRemote.login(loginParams)
        if (responce1 is Either.Left) return responce1
        val token = (responce1 as Either.Right).b.token

        return getUserAndStoreData(token)
    }

    /**
     * send reset password email
     * */
    override suspend fun resetPassword(email: String): Either<AuthFailure, None> {
        return authRemote.resetPassword(email)
    }

    /**
     * post registration request
     * */
    override suspend fun register(registrationParams: RegistrationParams): Either<AuthFailure, None> {
        return authRemote.register(registrationParams)
    }

    /**
     * store data locally
     * */
    private suspend fun getUserAndStoreData(token: String) : Either<AuthFailure,None>{

        //get user entity
        val responce = authRemote.getUserEntity(token)
        if(responce is Either.Left) return responce
        val userEntity = (responce as Either.Right).b.toEntity()

        //store token & user entity locally
        authLocal.storeToken(token)
        authLocal.storeUser(userEntity.toLocalEntity(),gson)

        return Either.Right(None())
    }
}
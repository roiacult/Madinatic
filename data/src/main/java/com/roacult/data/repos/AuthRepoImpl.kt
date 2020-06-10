package com.roacult.data.repos

import com.google.gson.Gson
import com.pusher.pushnotifications.BeamsCallback
import com.pusher.pushnotifications.PushNotifications
import com.pusher.pushnotifications.PusherCallbackError
import com.pusher.pushnotifications.auth.AuthData
import com.pusher.pushnotifications.auth.AuthDataGetter
import com.pusher.pushnotifications.auth.BeamsTokenProvider
import com.roacult.data.local.entities.toLocalEntity
import com.roacult.data.remote.AuthLocal
import com.roacult.data.remote.AuthRemote
import com.roacult.data.utils.BASE_URL
import com.roacult.data.utils.TOKEN_PREFEXE
import com.roacult.domain.entities.User
import com.roacult.domain.exceptions.AuthFailure
import com.roacult.domain.repos.AuthRepo
import com.roacult.domain.usecases.auth.LoginParams
import com.roacult.domain.usecases.auth.RegistrationParams
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.None
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

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

        return getUser(token)
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
     * return user state
     * */
    override suspend fun getUserState() : Boolean{
        return authLocal.getUserState()
    }

    /**
     * logout user & delete all local storage
     * */
    override fun logout(): None {
        authLocal.logout()
        PushNotifications.clearAllState()
        return None()
    }

    /**
     * store data locally
     * */
    private suspend fun getUser(token: String) : Either<AuthFailure,None>{

        // get user entity
        val responce = authRemote.getUserEntity(token)
        if(responce is Either.Left) return responce
        val userEntity = (responce as Either.Right).b.toEntity()

        return connectToPusherBeamsAndStoreData(token,userEntity)
    }

    /**
     * connect to Pusher Beams server
     * */
    private suspend fun connectToPusherBeamsAndStoreData(
        token: String,
        user:User
    ) : Either<AuthFailure,None>{

        // get Beams token from rest api and
        // authenticate to pusher Beams
        val responce = authRemote.authentificatToPusherBeams(token,user.idu)
        if(responce is Either.Left) return responce

        // store token & user entity locally
        authLocal.storeToken(token)
        authLocal.storeUser(user.toLocalEntity(),gson)

        // if every think went ok we return Either.Right to inform
        // ui layer that's authentication succeeded
        return Either.Right(None())
    }
}
package com.roacult.data.remote

import com.pusher.pushnotifications.BeamsCallback
import com.pusher.pushnotifications.PushNotifications
import com.pusher.pushnotifications.PusherCallbackError
import com.pusher.pushnotifications.auth.AuthData
import com.pusher.pushnotifications.auth.AuthDataGetter
import com.pusher.pushnotifications.auth.BeamsTokenProvider
import com.roacult.data.remote.entities.*
import com.roacult.data.remote.services.AuthService
import com.roacult.data.utils.BASE_URL
import com.roacult.data.utils.TOKEN_PREFEXE
import com.roacult.domain.exceptions.AuthFailure
import com.roacult.domain.usecases.auth.LoginParams
import com.roacult.domain.usecases.auth.RegistrationParams
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.None
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AuthRemote (
    private val authService : AuthService
) {

    suspend fun login(loginParams: LoginParams): Either<AuthFailure, Token> =
        suspendCoroutine { coroutine ->
            authService.login(RemoteLoginParams(loginParams.email, loginParams.password))
                .enqueue(object : Callback<Token> {
                    override fun onFailure(call: Call<Token>, t: Throwable) {
                        Timber.v("login failled $t")
                        coroutine.resume(Either.Left(AuthFailure.InternetConnection))
                    }

                    override fun onResponse(call: Call<Token>, response: Response<Token>) {
                        val body = response.body()
                        if (body == null || !response.isSuccessful) {
                            Timber.v("login failled $response")
                            coroutine.resume(Either.Left(AuthFailure.AuthFailed))
                        } else {
                            Timber.v("login succeeded")
                            coroutine.resume(Either.Right(body))
                        }
                    }
                })
        }

    suspend fun getUserEntity(token: String): Either<AuthFailure, UserRemoteEntity> =
        suspendCoroutine { coroutine ->
            authService.searchUser(TOKEN_PREFEXE + token)
                .enqueue(object : Callback<UserRemoteEntity> {
                    override fun onFailure(call: Call<UserRemoteEntity>, t: Throwable) {
                        Timber.v("get user failled $t")
                        coroutine.resume(Either.Left(AuthFailure.InternetConnection))
                    }

                    override fun onResponse(
                        call: Call<UserRemoteEntity>,
                        response: Response<UserRemoteEntity>
                    ) {
                        val body = response.body()
                        if (body == null || !response.isSuccessful) {
                            coroutine.resume(Either.Left(AuthFailure.AuthFailed))
                        } else coroutine.resume(Either.Right(body))
                    }
                })
        }

    suspend fun resetPassword(email: String): Either<AuthFailure, None> = suspendCoroutine {coroutine->
        authService
            .resetPassword(ResetPassword(email))
            .enqueue(object : Callback<ResetPasswordResult> {
                override fun onFailure(call: Call<ResetPasswordResult>, t: Throwable) {
                    Timber.v("fail to send email ")
                    coroutine.resume(Either.Left(AuthFailure.InternetConnection))
                }

                override fun onResponse(
                    call: Call<ResetPasswordResult>,
                    response: Response<ResetPasswordResult>
                ) {
                    Timber.v("resetPassword response")
                    val body2 = response.body()
                    if(body2 == null || !response.isSuccessful){
                        Timber.v("resetPassword failed")
                        coroutine.resume(Either.Left(AuthFailure.InvalidEmail))
                    }else coroutine.resume(Either.Right(None()))
                }
            })

    }

    suspend fun register(registrationParams: RegistrationParams): Either<AuthFailure, None> = suspendCoroutine{coroutine->
        authService
            .register(registrationParams.toRemoteEntity())
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Timber.v("fail to post registration request ")
                    coroutine.resume(Either.Left(AuthFailure.InternetConnection))
                }

                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    val body2 = response.body()
                    if(body2 == null || !response.isSuccessful){
                        Timber.v("failed to post registration request")
                        val err = response.errorBody()!!.string()

                        if(err.contains("A user is already registered")){
                            coroutine.resume(Either.Left(AuthFailure.AlredySignedIn))
                        }else if(err.contains("This password is too short")){
                            coroutine.resume(Either.Left(AuthFailure.InvalidPassword))
                        } else coroutine.resume(Either.Left(AuthFailure.InvalidEmail))
                    }else coroutine.resume(Either.Right(None()))
                }
            })
    }


    suspend fun authentificatToPusherBeams(
        token: String,
        uid: String
    ) : Either<AuthFailure,None> = suspendCoroutine { coroutine->

        //create BeamsTokenProvider inorder to send http
        // request to rest api and get Beams-Token
        val tokenProvider = BeamsTokenProvider(
            BASE_URL + "api/beams_auth/",
            object: AuthDataGetter {
                override fun getAuthData(): AuthData {
                    return AuthData(
                        headers = hashMapOf("Authorization" to TOKEN_PREFEXE+token),
                        queryParams = hashMapOf()
                    )
                }
            }
        )

        //send request and authenticate
        PushNotifications.setUserId(uid, tokenProvider,
            object : BeamsCallback<Void, PusherCallbackError> {
                override fun onFailure(error: PusherCallbackError) {
                    Timber.v( "Could not login to Beams: $error")
                    Timber.v( "Could not login to Beams: ${error.message}")
                    Timber.v( "Could not login to Beams: ${error.cause}")
                    coroutine.resume(Either.Left(AuthFailure.AuthFailed))
                }

                override fun onSuccess(vararg values: Void) {
                    Timber.v( "Beams login success")
                    coroutine.resume(Either.Right(None()))
                }
            }
        )
    }
}
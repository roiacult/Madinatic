package com.roacult.data.remote

import com.roacult.data.remote.entities.RemoteLoginParams
import com.roacult.data.remote.entities.Token
import com.roacult.data.remote.entities.UserRemoteEntity
import com.roacult.data.remote.services.AuthService
import com.roacult.data.utils.TOKEN_PREFEXE
import com.roacult.domain.exceptions.AuthFailure
import com.roacult.domain.usecases.auth.LoginParams
import com.roacult.kero.team7.jstarter_domain.functional.Either
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
            authService.searchUser(TOKEN_PREFEXE + token,
                Token(token)
            )
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
}
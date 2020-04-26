package com.roacult.data.remote.services

import com.roacult.data.remote.entities.*
import com.roacult.data.utils.API_ROOTS
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST(API_ROOTS.LOGIN)
    fun login(
        @Body login : RemoteLoginParams
    ) : Call<Token>

    @POST(API_ROOTS.RESET_PASSWORD)
    fun resetPassword(
        @Body resetPassword: ResetPassword
    ): Call<ResetPasswordResult>

    @POST(API_ROOTS.USER_WITH_TOKEN)
    fun searchUser(
        @Header("Authorization") key: String,
        @Body token: Token
    ) : Call<UserRemoteEntity>
}
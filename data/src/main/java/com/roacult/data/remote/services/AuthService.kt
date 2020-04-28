package com.roacult.data.remote.services

import com.roacult.data.remote.entities.*
import com.roacult.data.utils.API_ROOTS
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
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

    @GET(API_ROOTS.USER_WITH_TOKEN)
    fun searchUser(
        @Header("Authorization") key: String
    ) : Call<UserRemoteWrapper>

    @POST(API_ROOTS.REGISTRATION)
    fun register(
        @Body remoteRegistrationParams: RemoteRegistrationParams
    ) : Call<ResponseBody>
}
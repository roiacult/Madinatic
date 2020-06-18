package com.roacult.data.remote.services

import com.roacult.data.remote.entities.*
import com.roacult.data.utils.APIROOTS
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST(APIROOTS.LOGIN)
    fun login(
        @Body login : RemoteLoginParams
    ) : Call<Token>

    @POST(APIROOTS.RESET_PASSWORD)
    fun resetPassword(
        @Body resetPassword: ResetPassword
    ): Call<ResetPasswordResult>

    @GET(APIROOTS.USER_WITH_TOKEN)
    fun searchUser(
        @Header("Authorization") key: String
    ) : Call<UserRemoteEntity>

    @POST(APIROOTS.REGISTRATION)
    fun register(
        @Body remoteRegistrationParams: RemoteRegistrationParams
    ) : Call<ResponseBody>
}
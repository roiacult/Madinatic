package com.roacult.data.remote.services

import com.roacult.data.remote.entities.RemoteLoginParams
import com.roacult.data.remote.entities.Token
import com.roacult.data.remote.entities.UserRemoteEntity
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("/rest-auth/login/")
    fun login(
        @Body login : RemoteLoginParams
    ) : Call<Token>

    @POST("/api/userwithtoken/")
    fun searchUser(
        @Header("Authorization") key: String,
        @Body token: Token
    ) : Call<UserRemoteEntity>
}
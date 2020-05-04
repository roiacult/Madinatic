package com.roacult.data.remote.services

import com.roacult.data.remote.entities.RemoteUpdatePassword
import com.roacult.data.remote.entities.UserRemoteEntity
import com.roacult.data.utils.API_ROOTS
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MainService {

    @Multipart
    @PUT(API_ROOTS.USER_WITH_TOKEN)
    fun putUserInfo(
        @Header("Authorization") key: String,
        @PartMap parts: HashMap<String, RequestBody>
    ) : Call<UserRemoteEntity>

    @Multipart
    @PUT(API_ROOTS.USER_WITH_TOKEN)
    fun putUserInfo(
        @Header("Authorization") key: String,
        @PartMap parts: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part
    ) : Call<UserRemoteEntity>

    @POST(API_ROOTS.UPDATEPASSWORD)
    fun updatePassword(
        @Header("Authorization") key: String,
        @Body updatePasswordParam : RemoteUpdatePassword
    ) : Call<ResponseBody>

}

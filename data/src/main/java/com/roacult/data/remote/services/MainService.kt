package com.roacult.data.remote.services

import com.roacult.data.remote.entities.RemoteCategorie
import com.roacult.data.remote.entities.RemoteDeclaration
import com.roacult.data.remote.entities.RemoteUpdatePassword
import com.roacult.data.remote.entities.UserRemoteEntity
import com.roacult.data.utils.APIROOTS
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface MainService {

    @Multipart
    @PUT(APIROOTS.USER_WITH_TOKEN)
    fun putUserInfo(
        @Header("Authorization") key: String,
        @PartMap parts: HashMap<String, RequestBody>
    ) : Call<UserRemoteEntity>

    @Multipart
    @PUT(APIROOTS.USER_WITH_TOKEN)
    fun putUserInfo(
        @Header("Authorization") key: String,
        @PartMap parts: HashMap<String, RequestBody>,
        @Part image: MultipartBody.Part
    ) : Call<UserRemoteEntity>

    @POST(APIROOTS.UPDATEPASSWORD)
    fun updatePassword(
        @Header("Authorization") key: String,
        @Body updatePasswordParam : RemoteUpdatePassword
    ) : Call<ResponseBody>


    @GET(APIROOTS.DECLARATIONTYPE)
    fun fetchCategories(
        @Header("Authorization") key: String
    ) : Call<List<RemoteCategorie>>

}

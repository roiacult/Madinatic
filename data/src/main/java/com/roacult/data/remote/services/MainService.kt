package com.roacult.data.remote.services

import com.roacult.data.remote.entities.*
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

    @POST(APIROOTS.DECLARATION)
    fun postDeclaration(
        @Header("Authorization") key: String,
        @Body remoteDeclaration: RemoteDeclaration
    ) : Call<RemoteDeclaration>

    @Multipart
    @POST(APIROOTS.DECLARATIONDOC)
    fun postDoc(
        @Header("Authorization") key: String,
        @PartMap parts: HashMap<String, RequestBody>,
        @Part image : MultipartBody.Part
    ) : Call<ResponseBody>


    @GET(APIROOTS.DECLARATION)
    fun getDeclarationPage(
        @Header("Authorization") key: String,
        @Query("page") page : Int
    ) : Call<RemoteDeclarationPage>

    @GET(APIROOTS.DECLARATION)
    fun getDeclarationPage(
        @Header("Authorization") key: String,
        @Query("citizen") uid : String,
        @Query("page") page : Int
    ) : Call<RemoteDeclarationPage>

}

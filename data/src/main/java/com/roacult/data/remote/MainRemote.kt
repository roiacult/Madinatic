package com.roacult.data.remote

import com.roacult.data.remote.entities.RemoteUpdatePassword
import com.roacult.data.remote.entities.UserRemoteEntity
import com.roacult.data.remote.services.MainService
import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.domain.usecases.profile.EditInfoParams
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.None
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class MainRemote(
    private val service : MainService
) {
    suspend fun putUserInfo(editUserInfo: EditInfoParams, token :String): Either<ProfileFailures,UserRemoteEntity> =
        suspendCoroutine{coroutine->

        val map = hashMapOf<String, RequestBody>()

        map["first_name"] = editUserInfo.firstName.toRequestBody("text/plain".toMediaTypeOrNull())
        map["last_name"] = editUserInfo.lastName.toRequestBody("text/plain".toMediaTypeOrNull())
        map["email"] = editUserInfo.email.toRequestBody("text/plain".toMediaTypeOrNull())
        map["phone"] = editUserInfo.phoneNumber.toRequestBody("text/plain".toMediaTypeOrNull())
        map["date_of_birth"] = editUserInfo.dateBirth.toRequestBody("text/plain".toMediaTypeOrNull())
        map["address"] = editUserInfo.address.toRequestBody("text/plain".toMediaTypeOrNull())

        val image = editUserInfo.image
        val call = if(image == null) {
            service.putUserInfo(token,map)
        }else{
            val file = File(image)
            val req = file.asRequestBody("image/*".toMediaTypeOrNull())
            service.putUserInfo(token,map, MultipartBody.Part.createFormData("image",file.name,req))
        }
        call.enqueue(object : Callback<UserRemoteEntity> {
            override fun onFailure(call: Call<UserRemoteEntity>, t: Throwable) {
                Timber.v("putUserInfo failled $t")
                coroutine.resume(Either.Left(ProfileFailures.InternetConnection))
            }

            override fun onResponse(call: Call<UserRemoteEntity>, response: Response<UserRemoteEntity>) {
                val body = response.body()
                if (body == null || !response.isSuccessful) {
                    Timber.v("putUserInfo failled $response")
                    coroutine.resume(Either.Left(ProfileFailures.AuthFailed))
                } else {
                    Timber.v("putUserInfo succeeded")
                    coroutine.resume(Either.Right(body))
                }
            }
        })
    }

    suspend fun updatePassword(
        toRemote: RemoteUpdatePassword,
        token: String
    ): Either<ProfileFailures, None> = suspendCoroutine{coroutine->
        service.updatePassword(token,toRemote).enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Timber.v("updatePassword failled $t")
                coroutine.resume(Either.Left(ProfileFailures.InternetConnection))
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val body = response.body()
                if (body == null || !response.isSuccessful) {
                    Timber.v("updatePassword failled $response")
                    coroutine.resume(Either.Left(ProfileFailures.PasswordInvalid))
                } else {
                    Timber.v("updatePassword succeeded")
                    coroutine.resume(Either.Right(None()))
                }
            }
        })
    }

}

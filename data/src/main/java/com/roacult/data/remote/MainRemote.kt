package com.roacult.data.remote

import com.roacult.data.remote.entities.*
import com.roacult.data.remote.services.MainService
import com.roacult.data.utils.toRemote
import com.roacult.domain.entities.DeclarationState
import com.roacult.domain.exceptions.AnnounceFailure
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.domain.usecases.announce.AnnounceFilter
import com.roacult.domain.usecases.declaration.DeclarationOrdering
import com.roacult.domain.usecases.declaration.DeclarationPage
import com.roacult.domain.usecases.declaration.DeclarationPageParam
import com.roacult.domain.usecases.profile.DeclarationUpdate
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
                    coroutine.resume(Either.Left(ProfileFailures.UnkonwError))
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
                    if(response.errorBody()!!.string().contains("old_password\":[\"Invalid password")) {
                        coroutine.resume(Either.Left(ProfileFailures.PasswordInvalid))
                    }else coroutine.resume(Either.Left(ProfileFailures.NewPasswordInvalid))
                } else {
                    Timber.v("updatePassword succeeded")
                    coroutine.resume(Either.Right(None()))
                }
            }
        })
    }

    suspend fun fetchCategories(token: String): Either<DeclarationFailure, List<RemoteCategorie>> = suspendCoroutine {coroutine->
        service
            .fetchCategories(token)
            .enqueue(object : Callback<List<RemoteCategorie>> {
                override fun onFailure(call: Call<List<RemoteCategorie>>, t: Throwable) {
                    Timber.v("fetchCategories failed")
                    coroutine.resume(Either.Left(DeclarationFailure.InternetConnection))
                }

                override fun onResponse(
                    call: Call<List<RemoteCategorie>>,
                    response: Response<List<RemoteCategorie>>
                ) {
                    val body = response.body()
                    if (body == null || !response.isSuccessful) {
                        Timber.v("fetchCategories failled $response")
                        coroutine.resume(Either.Left(DeclarationFailure.UnkonwError))
                    } else {
                        Timber.v("fetchCategories succeeded")
                        coroutine.resume(Either.Right(body))
                    }
                }
            })
    }

    suspend fun submitDeclaration(key : String, remoteDeclaration: RemoteDeclaration): Either<DeclarationFailure, RemoteDeclaration>
            = suspendCoroutine { coroutine->
        service
            .postDeclaration(key,remoteDeclaration)
            .enqueue(object : Callback<RemoteDeclaration> {

                override fun onFailure(call: Call<RemoteDeclaration>, t: Throwable) {
                    Timber.v("postDeclaration failed")
                    coroutine.resume(Either.Left(DeclarationFailure.InternetConnection))
                }

                override fun onResponse(
                    call: Call<RemoteDeclaration>,
                    response: Response<RemoteDeclaration>
                ) {
                    val body = response.body()
                    if (body == null || !response.isSuccessful) {
                        Timber.v("postDeclaration failled $response")
                        coroutine.resume(Either.Left(DeclarationFailure.UnkonwError))
                    } else {
                        Timber.v("postDeclaration succeeded")
                        coroutine.resume(Either.Right(body))
                    }
                }
            })
    }

    suspend fun postDoc(
        token: String,
        remoteAttachment: RemoteAttachment
    ): Either<DeclarationFailure, None>
            = suspendCoroutine { coroutine->

        val map = hashMapOf<String, RequestBody>()

        map["filetype"] = remoteAttachment.filetype.toRequestBody("text/plain".toMediaTypeOrNull())
        map["declaration"] = remoteAttachment.declaration.toRequestBody("text/plain".toMediaTypeOrNull())

        val file = File(remoteAttachment.src)
        val req = file.asRequestBody("image/*".toMediaTypeOrNull())

        service
            .postDoc(token,map,MultipartBody.Part.createFormData("src",file.name,req))
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Timber.v("putUserInfo failled $t")
                    coroutine.resume(Either.Left(DeclarationFailure.InternetConnection))
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val body = response.body()
                    if (body == null || !response.isSuccessful) {
                        Timber.v("putUserInfo failled $response")
                        coroutine.resume(Either.Left(DeclarationFailure.UnkonwError))
                    } else {
                        Timber.v("putUserInfo succeeded")
                        coroutine.resume(Either.Right(None()))
                    }
                }
            })
    }

    suspend fun fetchDeclarations(
        token: String,
        pageParam: DeclarationPageParam
    ): Either<DeclarationFailure, RemoteDeclarationPage> = suspendCoroutine {coroutine->

        val call = if(pageParam.ordering == null)
            service.getDeclarationPage(token,pageParam.page)
        else if(pageParam.ordering == DeclarationOrdering.OLD_TO_NEW)
            service.getDeclarationPageOrderd(token,"created_on",pageParam.page)
        else
            service.getDeclarationPageOrderd(token,"-created_on",pageParam.page)

        call.enqueue(object : Callback<RemoteDeclarationPage> {
            override fun onFailure(call: Call<RemoteDeclarationPage>, t: Throwable) {
                Timber.v("fetchDeclarations failed")
                coroutine.resume(Either.Left(DeclarationFailure.InternetConnection))
            }

            override fun onResponse(
                call: Call<RemoteDeclarationPage>,
                response: Response<RemoteDeclarationPage>
            ) {
                val body = response.body()
                if (body == null || !response.isSuccessful) {
                    Timber.v("fetchDeclarations failled $response")
                    coroutine.resume(Either.Left(DeclarationFailure.UnkonwError))
                } else {
                    Timber.v("fetchDeclarations succeeded")
                    coroutine.resume(Either.Right(body))
                }
            }
        })
    }

    suspend fun fetchUserDeclarations(
        token: String,
        uid : String,
        status: String? = null,
        status2: String? = null,
        page: Int
    ): Either<DeclarationFailure, RemoteDeclarationPage> = suspendCoroutine {coroutine->
        val call =if(status == null)
            service.getDeclarationPage(token,uid,page)
        else
            service.getDeclarationPage(token,uid,status,status2!!,page)

        call.enqueue(object : Callback<RemoteDeclarationPage> {
            override fun onFailure(call: Call<RemoteDeclarationPage>, t: Throwable) {
                Timber.v("fetchDeclarations failed")
                coroutine.resume(Either.Left(DeclarationFailure.InternetConnection))
            }

            override fun onResponse(
                call: Call<RemoteDeclarationPage>,
                response: Response<RemoteDeclarationPage>
            ) {
                val body = response.body()
                if (body == null || !response.isSuccessful) {
                    Timber.v("fetchDeclarations failled $response")
                    coroutine.resume(Either.Left(DeclarationFailure.UnkonwError))
                } else {
                    Timber.v("fetchDeclarations succeeded")
                    coroutine.resume(Either.Right(body))
                }
            }
        })
    }

    suspend fun putNewData(token: String,uid: String, updates: DeclarationUpdate): Either<DeclarationFailure,None>
            = suspendCoroutine{coroutine->

        val geoCord = "[${updates.lat},${updates.long}]"

        val map = hashMapOf<String, RequestBody>()

        map["title"] = updates.title.toRequestBody("text/plain".toMediaTypeOrNull())
        map["desc"] = updates.desc.toRequestBody("text/plain".toMediaTypeOrNull())
        map["dtype"] = updates.categorie.toRequestBody("text/plain".toMediaTypeOrNull())
        map["address"] = updates.address.toRequestBody("text/plain".toMediaTypeOrNull())
        map["geo_cord"] = geoCord.toRequestBody("text/plain".toMediaTypeOrNull())
        map["status"] = DeclarationState.NOT_VALIDATED.toRemote().toRequestBody("text/plain".toMediaTypeOrNull())
        map["citizen"] = uid.toRequestBody("text/plain".toMediaTypeOrNull())

        service.putDeclaration(token,updates.id,map)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Timber.v("putDeclaration failled $t")
                    coroutine.resume(Either.Left(DeclarationFailure.InternetConnection))
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val body = response.body()
                    if (body == null || !response.isSuccessful) {
                        Timber.v("putDeclaration failled $response")
                        coroutine.resume(Either.Left(DeclarationFailure.UnkonwError))
                    } else {
                        Timber.v("putDeclaration succeeded")
                        coroutine.resume(Either.Right(None()))
                    }
                }
            })
    }

    suspend fun deleteDeclaration(
        token: String,
        declaration: String
    ): Either<DeclarationFailure, None> = suspendCoroutine {coroutine->
        service.deleteDeclaration(token,declaration)
            .enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Timber.v("deleteDeclaration failled $t")
                    coroutine.resume(Either.Left(DeclarationFailure.InternetConnection))
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if (response.code() != 204 ) {
                        Timber.v("deleteDeclaration failled $response")
                        coroutine.resume(Either.Left(DeclarationFailure.UnkonwError))
                    } else {
                        Timber.v("deleteDeclaration succeeded")
                        coroutine.resume(Either.Right(None()))
                    }
                }
            })
    }

    suspend fun fetchAnnounce(
        token: String,
        page: Int,
        announceFilter: AnnounceFilter,
        currentDate: String? = null
    ): Either<AnnounceFailure, RemoteAnnouncePage> = suspendCoroutine { coroutine->

        val call = when (announceFilter) {
            AnnounceFilter.CURRENT -> service.getAnnouncePage(token, page,
                startAtLess = currentDate,
                endAtGreater = currentDate
            )
            AnnounceFilter.UPCOMING -> service.getAnnouncePage(token, page,
                startAtGreater = currentDate
            )
            AnnounceFilter.OLD -> service.getAnnouncePage(token, page,
                endAtLess = currentDate
            )
            AnnounceFilter.ALL -> service.getAnnouncePage(token,page)
        }

        call.enqueue(object : Callback<RemoteAnnouncePage> {
            override fun onFailure(call: Call<RemoteAnnouncePage>, t: Throwable) {
                Timber.v("fetchAnnounce failed")
                coroutine.resume(Either.Left(AnnounceFailure.InternetConnection))
            }

            override fun onResponse(
                call: Call<RemoteAnnouncePage>,
                response: Response<RemoteAnnouncePage>
            ) {
                val body = response.body()
                if (body == null || !response.isSuccessful) {
                    Timber.v("fetchAnnounce failled $response")
                    coroutine.resume(Either.Left(AnnounceFailure.UnknownError))
                } else {
                    Timber.v("fetchAnnounce succeeded")
                    coroutine.resume(Either.Right(body))
                }
            }
        })
    }
}

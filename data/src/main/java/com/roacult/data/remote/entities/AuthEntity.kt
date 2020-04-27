package com.roacult.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.roacult.domain.usecases.auth.RegistrationParams

data class RemoteLoginParams(
    @SerializedName("email") val email : String,
    @SerializedName("password") val password : String
)

data class ResetPasswordResult(
    @SerializedName("detail") val detail: String
)

data class ResetPassword(
    @SerializedName("email") val email: String
)

data class RemoteRegistrationParams(
    @SerializedName("fullname") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("address") val address: String,
    @SerializedName("password") val password: String,
    @SerializedName("password2") val password2: String,
    @SerializedName("nationaid") val nationalid: String
)

fun RegistrationParams.toRemoteEntity() : RemoteRegistrationParams{
    return RemoteRegistrationParams(
        fullName,
        email,
        phone,
        address,
        password,
        password,
        nationalid
    )
}
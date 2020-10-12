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
    @SerializedName("first_name") val fullName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("address") val address: String,
    @SerializedName("password1") val password: String,
    @SerializedName("password2") val password2: String,
    @SerializedName("date_of_birth") val dateBirth: String,
    @SerializedName("national_id") val nationalid: String
)

fun RegistrationParams.toRemoteEntity() : RemoteRegistrationParams{
    return RemoteRegistrationParams(
        firstName,
        lastName,
        email,
        phone,
        address,
        password,
        password,
        dateBirth,
        nationalid
    )
}
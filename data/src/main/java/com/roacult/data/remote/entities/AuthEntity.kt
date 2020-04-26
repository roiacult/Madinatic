package com.roacult.data.remote.entities

import com.google.gson.annotations.SerializedName

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
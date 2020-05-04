package com.roacult.data.remote.entities


import com.google.gson.annotations.SerializedName
import com.roacult.domain.usecases.profile.ChangePasswordParam

data class RemoteUpdatePassword(
    @SerializedName("new_password1") val newPassword1: String,
    @SerializedName("new_password2") val newPassword2: String,
    @SerializedName("old_password") val oldPassword: String
)

fun ChangePasswordParam.toRemote() : RemoteUpdatePassword {
    return RemoteUpdatePassword(
        this.newPassword,
        this.newPassword,
        this.oldPassword
    )
}


package com.roacult.data.remote.entities


import com.google.gson.annotations.SerializedName
import com.roacult.domain.entities.Service

data class RemoteService(
    @SerializedName("uid") val uid: String,
    @SerializedName("email") val email: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("phone") val phone: String
) {

    fun toService() : Service {
        return Service(
            this.uid,
            this.email,
            this.firstName,
            this.lastName,
            this.phone
        )
    }
}

fun Service.toRemote() : RemoteService {
    return RemoteService(
        this.uid,
        this.email,
        this.firstName,
        this.lastName,
        this.phone
    )
}
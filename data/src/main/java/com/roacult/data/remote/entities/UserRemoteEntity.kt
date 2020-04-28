package com.roacult.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.roacult.domain.entities.User

class UserRemoteEntity(
    @SerializedName("pk") val idu : String,
    @SerializedName("username") val username : String,
    @SerializedName("first_name") val first_name : String,
    @SerializedName("last_name") val last_name : String,
    @SerializedName("image") val image : String?,
    @SerializedName("phone") val phone : String,
    @SerializedName("email") val email : String,
    @SerializedName("chariots") val chariots : List<String>,
    @SerializedName("likes") val likes : List<String>,
    @SerializedName("created_on") val created_on : String
) {
    fun toEntity() : User {
        return User(
            this.idu,
            this.username,
            this.first_name,
            this.last_name,
            this.image,
            this.phone,
            this.email,
            this.chariots,
            this.likes,
            this.created_on
        )
    }
}

fun User.toRemoteEntity() : UserRemoteEntity {
    return UserRemoteEntity(
        this.idu,
        this.username,
        this.first_name,
        this.last_name,
        this.image,
        this.phone,
        this.email,
        this.chariots,
        this.likes,
        this.created_on
    )
}
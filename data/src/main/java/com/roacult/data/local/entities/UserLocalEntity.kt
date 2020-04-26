package com.roacult.data.local.entities

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.roacult.data.utils.deserializeList
import com.roacult.data.utils.serializeList
import com.roacult.domain.entities.User

class UserLocalEntity (
    @SerializedName("idu") val idu : String,
    @SerializedName("username") val username : String,
    @SerializedName("first_name") val first_name : String,
    @SerializedName("last_name") val last_name : String,
    @SerializedName("image") val image : String?,
    @SerializedName("phone") val phone : String,
    @SerializedName("email") val email : String,
    @SerializedName("chariots") val chariots : String,
    @SerializedName("likes") val likes : String,
    @SerializedName("created_on") val created_on : String
) {

    fun toEntity(gson: Gson) : User {
        return User(
            this.idu,
            this.username,
            this.first_name,
            this.last_name,
            this.image,
            this.phone,
            this.email,
            this.chariots.deserializeList(gson),
            this.likes.deserializeList(gson),
            this.created_on
        )
    }
}

fun User.toLocalEntity(gson: Gson) : UserLocalEntity {
    return UserLocalEntity(
        this.idu,
        this.username,
        this.first_name,
        this.last_name,
        this.image,
        this.phone,
        this.email,
        this.chariots.serializeList(gson),
        this.likes.serializeList(gson),
        this.created_on
    )
}
package com.roacult.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.roacult.domain.entities.User

data class UserRemoteWrapper(
    @SerializedName("user") val user : UserRemoteEntity
)

data class UserRemoteEntity(
    @SerializedName("uid") val idu : String,
    @SerializedName("first_name") val first_name : String,
    @SerializedName("last_name") val last_name : String,
    @SerializedName("image") val image : String?,
    @SerializedName("phone") val phone : String,
    @SerializedName("date_of_birth") val dateBirth : String,
    @SerializedName("email") val email : String,
    @SerializedName("address") val address : String,
    @SerializedName("created_on") val created_on : String,
    @SerializedName("is_approved") val isApproved: Boolean
) {
    fun toEntity() : User {
        return User(
            this.idu,
            this.first_name,
            this.last_name,
            this.image,
            this.phone,
            this.dateBirth,
            this.email,
            this.address,
            this.created_on
        )
    }
}

fun User.toRemoteEntity() : UserRemoteEntity {
    return UserRemoteEntity(
        this.idu,
        this.first_name,
        this.last_name,
        this.image,
        this.phone,
        this.dateBirth,
        this.email,
        this.address,
        this.created_on,
        false
    )
}
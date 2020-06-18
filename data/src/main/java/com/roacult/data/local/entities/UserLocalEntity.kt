package com.roacult.data.local.entities

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.roacult.domain.entities.User

class UserLocalEntity (
    @SerializedName("uid") val idu : String,
    @SerializedName("first_name") val first_name : String,
    @SerializedName("last_name") val last_name : String,
    @SerializedName("image") val image : String?,
    @SerializedName("phone") val phone : String,
    @SerializedName("date_of_birth") val dateBirth : String,
    @SerializedName("email") val email : String,
    @SerializedName("address") val address : String,
    @SerializedName("created_on") val created_on : String
){

    fun toEntity(gson: Gson) : User {
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

fun User.toLocalEntity() : UserLocalEntity {
    return UserLocalEntity(
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
package com.roacult.domain.entities

data class User (
    val idu : String,
    val first_name : String,
    val last_name : String,
    val image : String?,
    val phone : String,
    val dateBirth : String,
    val email : String,
    val address : String,
    val created_on : String
)
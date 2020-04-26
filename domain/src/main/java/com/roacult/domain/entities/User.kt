package com.roacult.domain.entities

class User (
    val idu : String,
    val username : String,
    val first_name : String,
    val last_name : String,
    val image : String?,
    val phone : String,
    val email : String,
    val chariots : List<String>,
    val likes : List<String>,
    val created_on : String
)
package com.roacult.domain.entities

data class Declaration (
    val id: String,
    val title: String,
    val desc: String,
    val citizen: String,
    val address: String,
    val geo_cord: String,
    val categorie: String,
    val status: String?,
    val modified_at: String?,
    val validated_at: String?
)
package com.roacult.domain.entities

data class Announce(
    val aid: String,
    val title: String,
    val desc: String,
    val service: Service,
    val status: String,
    val createdOn: String,
    val startAt: String,
    val endAt: String,
    val image: String?
)
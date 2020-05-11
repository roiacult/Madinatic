package com.roacult.data.remote.entities


import com.google.gson.annotations.SerializedName

data class RemoteDocument(
    @SerializedName("declaration") val declaration: String,
    @SerializedName("filetype") val filetype: String,
    @SerializedName("src") val src: String
)
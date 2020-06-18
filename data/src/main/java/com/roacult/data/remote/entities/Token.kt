package com.roacult.data.remote.entities

import com.google.gson.annotations.SerializedName

data class Token (
    @SerializedName("key") val token : String
)
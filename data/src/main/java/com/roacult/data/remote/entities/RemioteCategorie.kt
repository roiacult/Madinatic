package com.roacult.data.remote.entities


import com.google.gson.annotations.SerializedName
import com.roacult.domain.entities.Categorie

data class RemoteCategorie(
    @SerializedName("dtid") val dtid: String,
    @SerializedName("name") val name: String,
    @SerializedName("created_on") val createdOn: String
) {
    fun toCategorie() : Categorie {
        return Categorie(
            this.dtid,
            this.name,
            this.createdOn
        )
    }
}

fun Categorie.toRemote() :RemoteCategorie {
    return RemoteCategorie(
        this.idc,
        this.name,
        this.createdOn
    )
}
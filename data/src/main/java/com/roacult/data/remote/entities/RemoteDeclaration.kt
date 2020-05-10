package com.roacult.data.remote.entities


import com.google.gson.annotations.SerializedName
import com.roacult.domain.entities.Declaration

data class RemoteDeclaration(
    @SerializedName("title") val title: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("citizen") val citizen: String,
    @SerializedName("address") val address: String,
    @SerializedName("geo_cord") val geoCord: String,
    @SerializedName("dtype") val categorie: String,
    @SerializedName("status") val status: String,
    @SerializedName("modified_at") val modifiedAt: String?,
    @SerializedName("validated_at") val validatedAt: String?
) {
    fun toDeclaration() : Declaration{
        return Declaration(
            this.title,
            this.desc,
            this.citizen,
            this.address,
            this.geoCord,
            this.categorie,
            this.status,
            this.modifiedAt,
            this.validatedAt
        )
    }
}

fun Declaration.toRemote() : RemoteDeclaration {
    return RemoteDeclaration(
        this.title,
        this.desc,
        this.citizen,
        this.address,
        this.geo_cord,
        this.categorie,
        this.status ?: "",
        this.modified_at,
        this.validated_at
    )
}
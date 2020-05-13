package com.roacult.data.remote.entities


import com.google.gson.annotations.SerializedName
import com.roacult.domain.entities.Declaration

data class RemoteDeclaration(
    @SerializedName("did") val id : String,
    @SerializedName("title") val title: String,
    @SerializedName("desc") val desc: String,
    @SerializedName("citizen") val citizen: String,
    @SerializedName("address") val address: String,
    @SerializedName("geo_cord") val geoCord: String,
    @SerializedName("dtype") val categorie: String,
    @SerializedName("status") val status: String,
    @SerializedName("modified_at") val modifiedAt: String?,
    @SerializedName("validated_at") val validatedAt: String?,
    @SerializedName("attachments") val remoteAttachments: List<RemoteAttachment>
) {
    fun toDeclaration() : Declaration{
        return Declaration(
            this.id,
            this.title,
            this.desc,
            this.citizen,
            this.address,
            this.geoCord,
            this.categorie,
            this.status,
            this.modifiedAt,
            this.validatedAt,
            this.remoteAttachments.map { it.toAttachment() }
        )
    }
}

fun Declaration.toRemote() : RemoteDeclaration {
    return RemoteDeclaration(
        this.id,
        this.title,
        this.desc,
        this.citizen,
        this.address,
        this.geo_cord,
        this.categorie,
        this.status ?: "",
        this.modified_at,
        this.validated_at,
        this.attachment.map { it.toRemote() }
    )
}
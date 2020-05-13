package com.roacult.data.remote.entities

import com.google.gson.annotations.SerializedName
import com.roacult.data.utils.toAttachmentType
import com.roacult.data.utils.toRemote
import com.roacult.domain.entities.Attachment

data class RemoteAttachment(
    @SerializedName("created_on") val createdOn: String,
    @SerializedName("declaration") val declaration: String,
    @SerializedName("dmid") val attachmentId: String,
    @SerializedName("filetype") val filetype: String,
    @SerializedName("src") val src: String
) {
    fun toAttachment() : Attachment {
        return Attachment(
            this.createdOn,
            this.declaration,
            this.attachmentId,
            this.filetype.toAttachmentType(),
            this.src
        )
    }
}

fun Attachment.toRemote(declaration: String = this.declaration) : RemoteAttachment{
    return RemoteAttachment(
        this.createdOn,
        declaration,
        this.attachmentId,
        this.filetype.toRemote(),
        this.src
    )
}
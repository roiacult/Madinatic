package com.roacult.domain.utils

import com.roacult.domain.entities.AttachmentType

fun String.toAttachmentType() : AttachmentType{
    return when(this) {
        REMOTE_ATTACHMENT_PDF -> AttachmentType.PDF
        REMOTE_ATTACHMENT_IMAGE -> AttachmentType.IMAGE
        else -> AttachmentType.OTHER
    }
}

fun AttachmentType.toRemote() : String {
    return when(this) {
        AttachmentType.PDF -> REMOTE_ATTACHMENT_PDF
        AttachmentType.IMAGE -> REMOTE_ATTACHMENT_IMAGE
        AttachmentType.OTHER -> REMOTE_ATTACHMENT_OTHER
    }
}


const val REMOTE_ATTACHMENT_PDF = "pdf"
const val REMOTE_ATTACHMENT_IMAGE = "image"
const val REMOTE_ATTACHMENT_OTHER = "other"
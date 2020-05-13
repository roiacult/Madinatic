package com.roacult.domain.entities

data class Attachment(
    val createdOn: String,
    val declaration: String,
    val attachmentId: String,
    val filetype: AttachmentType,
    val src: String
)

enum class AttachmentType {
    IMAGE, PDF, OTHER
}
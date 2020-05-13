package com.roacult.domain.entities

data class Declaration (
    val id: String,
    val title: String,
    val desc: String,
    val citizen: String,
    val address: String,
    val geo_cord: String,
    val categorie: String,
    val status: DeclarationState,
    val modified_at: String?,
    val validated_at: String?,
    val attachment: List<Attachment>
)

enum class DeclarationState{
    VALIDATED, NOT_VALIDATED, REFUSED, LACK_OF_INFO, UNDER_TREATMENT, TREATED, ARCHIVED
}
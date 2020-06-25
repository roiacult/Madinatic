package com.roacult.domain.entities

data class Declaration (
    val id: String,
    val title: String,
    val desc: String,
    val citizen: String,
    val address: String,
    val coordination: GeoCoordination,
    val categorie: String,
    val state: DeclarationState,
    val modified_at: String?,
    val validated_at: String?,
    val attachment: List<Attachment>
)

data class GeoCoordination(
    val lat: Double,
    val long: Double
)

enum class DeclarationState{
    VALIDATED, TREATED, ARCHIVED, NOT_VALIDATED, UNDER_TREATMENT, REFUSED, LACK_OF_INFO, DRAFT
}
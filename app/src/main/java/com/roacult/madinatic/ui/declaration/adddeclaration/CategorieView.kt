package com.roacult.madinatic.ui.declaration.adddeclaration

import com.roacult.domain.entities.Categorie

data class CategorieView(
    val idc : String,
    val name : String,
    val createdOn : String
) {
    override fun toString(): String {
        return this.name
    }

    fun toCategorie() : Categorie {
        return Categorie(
            this.idc,
            this.name,
            this.createdOn
        )
    }
}

fun Categorie.toView() : CategorieView {
    return CategorieView(
        this.idc,
        this.name,
        this.createdOn
    )
}

const val HINT_VIEW_ID = "com.roacult.madinatic:categorieHintID"
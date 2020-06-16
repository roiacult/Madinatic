package com.roacult.madinatic.utils

interface AddDeclarationCallback {
    fun addDocClick()

    fun onDocLongClick(doc: String): Boolean
}
package com.roacult.madinatic.ui.declaration.declarationdetails

import com.google.gson.Gson
import com.roacult.domain.entities.Declaration
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.states.Event

class DeclarationDetailsViewModel(
    private val gson: Gson
) : BaseViewModel<DeclarationDetailsState>(DeclarationDetailsState()) {


    fun formatJson(json: String): Declaration{
        return gson.fromJson(json,Declaration::class.java)
    }
}

data class DeclarationDetailsState(
    val errorMsg : Event<String>? = null
) : State
package com.roacult.madinatic.ui.declaration.declarationdetails

import com.google.gson.Gson
import com.roacult.domain.entities.Attachment
import com.roacult.domain.entities.Declaration
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.states.Event

class DeclarationDetailsViewModel(
    private val gson: Gson
) : BaseViewModel<DeclarationDetailsState>(DeclarationDetailsState()) {

    lateinit var declaration : Declaration

    fun formatJson(json: String){
        declaration = gson.fromJson(json,Declaration::class.java)
    }

    fun downloadDoc(attachment: Attachment){

    }
}

data class DeclarationDetailsState(
    val errorMsg : Event<String>? = null
) : State
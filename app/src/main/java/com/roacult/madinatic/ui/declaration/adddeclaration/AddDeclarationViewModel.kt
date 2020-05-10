package com.roacult.madinatic.ui.declaration.adddeclaration

import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.states.Async
import com.roacult.madinatic.utils.states.Event
import com.roacult.madinatic.utils.states.Uninitialized

class AddDeclarationViewModel  :BaseViewModel<AddDeclarationState>(AddDeclarationState()) {

    val images = arrayListOf("","","","","","")
    val files = ArrayList<String>()


    fun save(title: String, desc: String, categorie : String) {

    }

}

data class AddDeclarationState(
    val errorMsg : Event<String>? = null,
    val addDeclaration : Async<None> = Uninitialized
) : State
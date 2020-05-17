package com.roacult.madinatic.ui.profile.alldeclarations

import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.StringProvider
import com.roacult.madinatic.utils.states.Event

class AllDeclarationViewModel(
    private val stringProvider: StringProvider
) : BaseViewModel<AllDeclarationState>(AllDeclarationState()) {



}

data class AllDeclarationState(
    val errorMsg : Event<String>? = null
) : State
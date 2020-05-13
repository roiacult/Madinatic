package com.roacult.madinatic.ui.declaration

import com.roacult.domain.entities.Declaration
import com.roacult.domain.entities.GeoCoordination
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.states.Event

class DeclarationViewModel(

) : BaseViewModel<DeclarationViewState>(DeclarationViewState()) {

    fun readMoreClicked(declaration : Declaration) {
        //TODO
    }

    fun gpsClicked(localisation : GeoCoordination) {
        //TODO
    }
}

data class DeclarationViewState(
    val errorMsg : Event<String>? = null
) : State
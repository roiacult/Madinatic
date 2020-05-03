package com.roacult.madinatic.ui.profile

import com.roacult.domain.entities.User
import com.roacult.domain.usecases.profile.EditUserInfo
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.states.Event

class EditInfoViewModel(
    private val editInfo : EditUserInfo
) : BaseViewModel<EditInfoState>(EditInfoState()) {

    private var firstTime = true

    var image : String? = null
    lateinit var user : User

    fun setData(user : User){
        if( !firstTime ) return
        firstTime = false
        this.user = user
    }
}

data class EditInfoState(
    val erroMsg : Event<String>? = null
) : State
package com.roacult.madinatic.ui.profile

import com.roacult.domain.entities.User
import com.roacult.domain.usecases.profile.EditUserInfo
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.states.Async
import com.roacult.madinatic.utils.states.Event
import com.roacult.madinatic.utils.states.Uninitialized

class EditInfoViewModel(
    private val editInfo : EditUserInfo
) : BaseViewModel<EditInfoState>(EditInfoState()) {

    var firstTime = true

    var image : String? = null
    lateinit var user : User

    fun setData(user : User){

        if (!firstTime) return
        firstTime = false

        this.user = user
    }

    fun saveUserInfo() {

    }
}

data class EditInfoState(
    val erroMsg : Event<String>? = null,
    val saveUserData : Async<None> = Uninitialized
) : State
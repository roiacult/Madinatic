package com.roacult.madinatic.ui.profile

import com.roacult.domain.entities.User
import com.roacult.domain.usecases.profile.UserObservable
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.StringProvider
import com.roacult.madinatic.utils.states.Event

class ProfileViewModel(
    private val userObservable : UserObservable,
    private val stringProvider: StringProvider
) : BaseViewModel<ProfileState>(ProfileState()) {

    init {
        refresh()
    }

    fun refresh() {
        launchEitherObservableInteractor(userObservable, None(),{
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.unknown_error))) }
            //TODO launch interactor to fetch user entity here
        },{
            setState { copy(user=it) }
        })
    }

    fun clickEvent(clickEvent: ProfileClickEvent) {
        setState { copy(clickEvent = Event(clickEvent)) }
    }
}

data class ProfileState (
    val clickEvent: Event<ProfileClickEvent>? = null,
    val errorMsg : Event<String>? = null,
    val user : User? = null
) : State

enum class ProfileClickEvent{
    CHANGEPASSWORD,CHANGEINFO
}
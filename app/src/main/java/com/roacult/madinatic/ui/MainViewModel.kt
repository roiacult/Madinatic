package com.roacult.madinatic.ui

import com.roacult.madinatic.base.NavigationState
import com.roacult.madinatic.base.NavigationViewModel
import com.roacult.madinatic.utils.navigation.FragmentNavigation
import com.roacult.madinatic.utils.states.Event

class MainViewModel(

) : NavigationViewModel<MainState>(MainState()) {

    override fun MainState.newNavigationState(navigation: FragmentNavigation): MainState {
        return copy(navigationEvent = Event(navigation))
    }

    fun disableBottomNav() {
        setState {
            copy(bottomNavState = Event(false))
        }
    }

    fun showBottomNav() {
        setState {
            copy(bottomNavState = Event(true))
        }
    }
}

data class MainState(
    val bottomNavState : Event<Boolean>? = null,
    override val navigationEvent: Event<FragmentNavigation>? = null,
    val userState : Event<Boolean>? = null
) : NavigationState
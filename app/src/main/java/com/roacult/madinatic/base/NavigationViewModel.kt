package com.roacult.madinatic.base

import androidx.fragment.app.Fragment
import com.roacult.madinatic.utils.navigation.FragmentNavigation
import com.roacult.madinatic.utils.states.Event

interface NavigationState: State {
    val navigationEvent: Event<FragmentNavigation>?
}
abstract class NavigationViewModel<S: NavigationState>(state :S): BaseViewModel<S>(state){
    fun navigate(navigation: FragmentNavigation) {
        setState {
            newNavigationState(navigation)
        }
    }
    fun <F:Fragment>navigate(fragmentClass:Class<F>  , arguments: FragmentNavigation.()-> FragmentNavigation = {this}){
        val fragmentNavigation = FragmentNavigation(fragmentClass)
        navigate(fragmentNavigation.arguments())
    }
    abstract fun S.newNavigationState(navigation: FragmentNavigation):S
}
package com.roacult.madinatic.ui.auth

import com.roacult.domain.exceptions.AuthFailure
import com.roacult.domain.usecases.auth.Register
import com.roacult.domain.usecases.auth.RegistrationParams
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.kero.team7.jstarter_domain.interactors.launchInteractor
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.StringProvider
import com.roacult.madinatic.utils.extensions.isValidEmail
import com.roacult.madinatic.utils.states.Async
import com.roacult.madinatic.utils.states.Event
import com.roacult.madinatic.utils.states.Loading

class RegisterViewModel(
    private val register: Register,
    private val stringProvider: StringProvider
) :BaseViewModel<RegisterState>(RegisterState()){
    
    
    fun register(
        fullName:String,
        email:String,
        phone:String,
        address:String,
        password:String,
        password2:String,
        nationalID:String
    ){
        if(fullName.isEmpty()) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.fullname_fail))) }
            return
        }

        if(!email.isValidEmail()) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.invalid_email))) }
            return
        }

        if(phone.isEmpty() || phone.length<10) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.invalid_phone))) }
            return
        }

        if(address.isEmpty()) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.invalid_address))) }
            return
        }

        if(password.length<8) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.invalid_password))) }
            return
        }

        if(password != password2) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.password_match_failure))) }
            return
        }

        if(nationalID.isEmpty()) {
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.invalid_nationalID))) }
            return
        }
        setState { copy(registration = Loading()) }

        scope.launchInteractor(register, RegistrationParams(
            fullName,email,phone,address,password,nationalID
        )){
            it.either(::handleRegistrationFailed,::handleRegistrationSucceded)
        }

    }

    private fun handleRegistrationFailed(authFailure: AuthFailure){
        //TODO
    }

    private fun handleRegistrationSucceded(none: None) {
        //TODO
    }
}


data class RegisterState(
    val errorMsg : Event<String>? = null,
    val registration : Async<None>? = null
) : State
package com.roacult.madinatic.ui.auth

import com.roacult.domain.exceptions.AuthFailure
import com.roacult.domain.usecases.auth.Login
import com.roacult.domain.usecases.auth.LoginParams
import com.roacult.domain.usecases.auth.ResetPassword
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.kero.team7.jstarter_domain.interactors.launchInteractor
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.StringProvider
import com.roacult.madinatic.utils.extensions.isValidEmail
import com.roacult.madinatic.utils.states.*
import timber.log.Timber

class LoginViewModel(
    private val login : Login,
    private val resetPassword : ResetPassword,
    private val stringProvider: StringProvider
) : BaseViewModel<LoginState>(LoginState()){



    fun login(email:String, password:String){

        if(state.value!!.login is Loading){
            setState{copy(errorMsg=Event(stringProvider.getStringFromResource(R.string.plz_patient)))}
            return
        }

        if(!email.isValidEmail()){
            setState { copy(errorMsg=Event(stringProvider.getStringFromResource(R.string.invalid_email))) }
            return
        }

        if(password.length<8){
            setState { copy(errorMsg = Event(stringProvider.getStringFromResource(R.string.invalid_password))) }
            return
        }

        setState { copy(login=Loading()) }
        scope.launchInteractor(login, LoginParams(email, password)){
            it.either(::onLoginFailed,::onLoginSucceeded)
        }
    }

    fun resetPassword(email:String) {
        if(state.value!!.resetPassword is Loading) {
            setState { copy(showMsg = Event(stringProvider.getStringFromResource(R.string.please_wait))) }
            return
        }

        if(!email.isValidEmail()){
            setState { copy(showMsg = Event(stringProvider.getStringFromResource(R.string.invalid_email))) }
            return
        }

        setState { copy(resetPassword = Loading()) }
        scope.launchInteractor(resetPassword,email){
            it.either(::handleResetPasswordFailure,::handleResetPasswordSucceeded)
        }
    }

    fun finishResetPassword() {
        setState { copy(resetPassword = null) }
    }


    private fun onLoginSucceeded(none: None) {
        Timber.v("login succeeded")
        setState { copy(login = Success(none)) }
    }

    private fun onLoginFailed(authFailure: AuthFailure) {
        Timber.v("login succeeded $authFailure")
        val erroMsg = when(authFailure){
            AuthFailure.InternetConnection -> stringProvider.getStringFromResource(R.string.internet_prblm)
            AuthFailure.InvalidEmail -> stringProvider.getStringFromResource(R.string.invalid_email)
            AuthFailure.AuthFailed -> stringProvider.getStringFromResource(R.string.login_failure)
            AuthFailure.UserNotApproved -> stringProvider.getStringFromResource(R.string.userNotApproved)
            else -> stringProvider.getStringFromResource(R.string.unknown_error)
        }
        setState { copy(errorMsg = Event(erroMsg), login = Fail(authFailure)) }
    }

    private fun handleResetPasswordSucceeded(none: None) {
        setState { copy(resetPassword = Success(none)) }
    }

    private fun handleResetPasswordFailure(authFailure: AuthFailure) {

        val msg = when(authFailure){
            AuthFailure.InternetConnection -> stringProvider.getStringFromResource(R.string.internet_prblm)
            AuthFailure.InvalidEmail -> stringProvider.getStringFromResource(R.string.invalid_email)
            else -> stringProvider.getStringFromResource(R.string.unknown_error)
        }

        setState { copy(resetPassword = Fail(authFailure),showMsg = Event(msg)) }
    }
}


data class LoginState(
    val login : Async<None>? = null,
    val resetPassword : Async<None>? = null,
    val errorMsg : Event<String>?=null,
    val showMsg : Event<String>? = null
) : State
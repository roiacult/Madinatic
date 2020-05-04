package com.roacult.madinatic.ui.profile

import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.domain.usecases.profile.ChangePassword
import com.roacult.domain.usecases.profile.ChangePasswordParam
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.kero.team7.jstarter_domain.interactors.launchInteractor
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.StringProvider
import com.roacult.madinatic.R
import com.roacult.madinatic.utils.states.*

class UpdatePasswordViewModel(
    private val changePassword: ChangePassword,
    private val stringProvider: StringProvider
) : BaseViewModel<UpdatePasswordState>(UpdatePasswordState()){


    fun updatePassword(oldPass:String,newpass:String,newPass2:String){

        if(newpass.length<8 ){
            setState{copy(erroMsg = Event(stringProvider.getStringFromResource(R.string.invalid_password)))}
            return
        }

        if(newpass != newPass2 ){
            setState{copy(erroMsg = Event(stringProvider.getStringFromResource(R.string.password_match_failure)))}
            return
        }

        setState { copy(updatePassword = Loading()) }
        scope.launchInteractor(changePassword, ChangePasswordParam(oldPass,newpass)){
            it.either(::onChangePassFailed,::onChangePassSucc)
        }
    }

    fun endUpdatePassword(){
        setState { copy(updatePassword = Uninitialized) }
    }

    fun onChangePassSucc(none : None) {
        setState { copy(updatePassword = Success(none)) }
    }

    fun onChangePassFailed(fail : ProfileFailures){
        val msg = when(fail){
            ProfileFailures.PasswordInvalid -> stringProvider.getStringFromResource(R.string.invalid_pass_failure)
            ProfileFailures.InternetConnection -> stringProvider.getStringFromResource(R.string.internet_prblm)
            else -> stringProvider.getStringFromResource(R.string.unknown_error)
        }

        setState { copy(erroMsg = Event(msg), updatePassword = Fail(fail)) }
    }
}

data class UpdatePasswordState (
    val erroMsg : Event<String>? = null,
    val updatePassword : Async<None> = Uninitialized
) : State
package com.roacult.madinatic.ui.profile

import com.roacult.domain.entities.User
import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.domain.usecases.profile.EditInfoParams
import com.roacult.domain.usecases.profile.EditUserInfo
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.kero.team7.jstarter_domain.interactors.launchInteractor
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseViewModel
import com.roacult.madinatic.base.State
import com.roacult.madinatic.utils.StringProvider
import com.roacult.madinatic.utils.extensions.isDateValid
import com.roacult.madinatic.utils.extensions.isValidEmail
import com.roacult.madinatic.utils.states.*
import timber.log.Timber

class EditInfoViewModel(
    private val editInfo : EditUserInfo,
    private val stringProvider: StringProvider
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
        if(!user.dateBirth.isDateValid()){
            setState { copy(erroMsg = Event(stringProvider.getStringFromResource(R.string.invalid_date))) }
            return
        }

        if(user.first_name.isEmpty()){
            setState{copy(erroMsg= Event(stringProvider.getStringFromResource(R.string.invalid_firstname)))}
            return
        }


        if(user.last_name.isEmpty()){
            setState{copy(erroMsg= Event(stringProvider.getStringFromResource(R.string.invalid_lastname)))}
            return
        }

        if(!user.email.isValidEmail()){
            setState{copy(erroMsg= Event(stringProvider.getStringFromResource(R.string.invalid_email)))}
            return
        }

        if(user.phone.isEmpty()){
            setState{copy(erroMsg= Event(stringProvider.getStringFromResource(R.string.invalid_phone)))}
            return
        }

        if(user.address.isEmpty()){
            setState{copy(erroMsg= Event(stringProvider.getStringFromResource(R.string.invalid_address)))}
            return
        }

        setState { copy(saveUserData = Loading()) }
        scope.launchInteractor(
            editInfo,
            EditInfoParams(image,user.first_name,user.last_name,user.email,user.phone,user.address,user.dateBirth)
        ){
            it.either(::onEditinfoFailed,::onEditInfoSuccssed)
        }
    }

    fun finishSaveUserData(){
        setState{copy(saveUserData = Uninitialized)}
    }

    private fun onEditInfoSuccssed(none: None) {
        setState { copy(saveUserData = Success(none)) }
    }

    private fun onEditinfoFailed(profileFailures: ProfileFailures) {
        Timber.v("login succeeded $profileFailures")
        val erroMsg = when(profileFailures){
            ProfileFailures.InternetConnection -> stringProvider.getStringFromResource(R.string.internet_prblm)
            else -> stringProvider.getStringFromResource(R.string.unknown_error)
        }
        setState { copy(erroMsg = Event(erroMsg), saveUserData = Fail(profileFailures)) }
    }
}

data class EditInfoState(
    val erroMsg : Event<String>? = null,
    val saveUserData : Async<None> = Uninitialized
) : State
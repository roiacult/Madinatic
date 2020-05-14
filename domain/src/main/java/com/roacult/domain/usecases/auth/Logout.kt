package com.roacult.domain.usecases.auth

import com.roacult.domain.repos.AuthRepo
import com.roacult.domain.utils.SyncInteractor
import com.roacult.kero.team7.jstarter_domain.interactors.None

class Logout(
    private val repo : AuthRepo
) : SyncInteractor<None,None>() {

    override fun invoke(executeParam: None): None {
        return repo.logout()
    }
}
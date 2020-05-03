package com.roacult.domain.usecases.auth

import com.roacult.domain.repos.AuthRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.interactors.Interactor
import com.roacult.kero.team7.jstarter_domain.interactors.None

class UserState(
    dispatcher: CouroutineDispatchers,
    private val repo : AuthRepo
) : Interactor<None,Boolean>(dispatcher) {
    override suspend fun invoke(executeParams: None): Boolean {
        return repo.getUserState()
    }
}
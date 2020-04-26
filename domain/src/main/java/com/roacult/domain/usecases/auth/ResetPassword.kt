package com.roacult.domain.usecases.auth

import com.roacult.domain.exceptions.AuthFailure
import com.roacult.domain.repos.AuthRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor
import com.roacult.kero.team7.jstarter_domain.interactors.None

class ResetPassword (
    dispathcher : CouroutineDispatchers,
    private val repo : AuthRepo
) : EitherInteractor<String, None, AuthFailure>(dispathcher) {

    override suspend fun invoke(executeParams: String): Either<AuthFailure, None> {
        return repo.resetPassword(executeParams)
    }
}
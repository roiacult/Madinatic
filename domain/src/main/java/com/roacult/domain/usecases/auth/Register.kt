package com.roacult.domain.usecases.auth

import com.roacult.domain.exceptions.AuthFailure
import com.roacult.domain.repos.AuthRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor
import com.roacult.kero.team7.jstarter_domain.interactors.None

class Register (
    coroutineDispatchers: CouroutineDispatchers,
    private val repo : AuthRepo
) : EitherInteractor<RegistrationParams,None,AuthFailure>(coroutineDispatchers){

    override suspend fun invoke(executeParams: RegistrationParams): Either<AuthFailure, None> {
        return repo.register(executeParams)
    }
}


data class RegistrationParams(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val address: String,
    val password: String,
    val dateBirth : String,
    val nationalid: String
)
package com.roacult.domain.usecases.auth

import com.roacult.domain.exceptions.AuthFailure
import com.roacult.domain.repos.AuthRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor
import com.roacult.kero.team7.jstarter_domain.interactors.None
import kotlinx.coroutines.delay

class Register (
    coroutineDispatchers: CouroutineDispatchers,
    private val repo : AuthRepo
) : EitherInteractor<RegistrationParams,None,AuthFailure>(coroutineDispatchers){

    override suspend fun invoke(executeParams: RegistrationParams): Either<AuthFailure, None> {
//        return repo.register(executeParams)
        delay(4000)
        return Either.Right(None())
    }
}


data class RegistrationParams(
    val fullName: String,
    val email: String,
    val phone: String,
    val address: String,
    val password: String,
    val nationalid: String
)
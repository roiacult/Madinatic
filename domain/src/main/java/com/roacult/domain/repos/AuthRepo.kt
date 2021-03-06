package com.roacult.domain.repos

import com.roacult.domain.exceptions.AuthFailure
import com.roacult.domain.usecases.auth.LoginParams
import com.roacult.domain.usecases.auth.RegistrationParams
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.None

interface AuthRepo {
    suspend fun logUserIn(loginParams: LoginParams): Either<AuthFailure, None>

    suspend fun resetPassword(email: String): Either<AuthFailure, None>

    suspend fun register(registrationParams: RegistrationParams): Either<AuthFailure, None>

    suspend fun getUserState() : Boolean

    fun logout(): None
}

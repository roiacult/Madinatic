package com.roacult.domain.repos

import com.roacult.domain.exceptions.AuthFailure
import com.roacult.domain.usecases.auth.LoginParams
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.None

interface AuthRepo {
    suspend fun logUserIn(loginParams: LoginParams): Either<AuthFailure, None>

}

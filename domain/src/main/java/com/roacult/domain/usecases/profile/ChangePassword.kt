package com.roacult.domain.usecases.profile

import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.domain.repos.MainRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor
import com.roacult.kero.team7.jstarter_domain.interactors.None

class ChangePassword(
    dispatcher: CouroutineDispatchers,
    private val repo : MainRepo
) : EitherInteractor<ChangePasswordParam, None, ProfileFailures>(dispatcher) {

    override suspend fun invoke(executeParams: ChangePasswordParam): Either<ProfileFailures, None> {
        return repo.changePassword(executeParams)
    }
}

data class ChangePasswordParam(
    val oldPassword : String,
    val newPassword : String
)
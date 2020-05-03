package com.roacult.domain.usecases.profile

import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.domain.repos.MainRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor
import com.roacult.kero.team7.jstarter_domain.interactors.None

class EditUserInfo(
    dispatcher: CouroutineDispatchers,
    private val repo : MainRepo
) : EitherInteractor<EditInfoParams,None,ProfileFailures>(dispatcher) {

    override suspend fun invoke(executeParams: EditInfoParams): Either<ProfileFailures, None> {
        return repo.editInfo(executeParams)
    }
}

data class EditInfoParams(
    val image : String?,
    val firstName : String,
    val lastName : String,
    val email : String ,
    val phoneNumber : String,
    val address : String,
    val dateBirth : String
)
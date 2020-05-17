package com.roacult.domain.usecases.profile

import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.repos.MainRepo
import com.roacult.domain.usecases.declaration.DeclarationPage
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor

class UserUnfinishedDeclaration(
    dispathcher : CouroutineDispatchers,
    private val repo : MainRepo
) : EitherInteractor<Int, DeclarationPage, DeclarationFailure>(dispathcher) {

    override suspend fun invoke(executeParams: Int): Either<DeclarationFailure, DeclarationPage> {
        return repo.fetchUnfinishedUserDeclrations(executeParams)
    }
}
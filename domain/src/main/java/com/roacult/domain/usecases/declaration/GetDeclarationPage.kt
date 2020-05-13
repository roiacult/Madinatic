package com.roacult.domain.usecases.declaration

import com.roacult.domain.entities.Declaration
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.repos.MainRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor

class GetDeclarationPage (
    dispathcher : CouroutineDispatchers,
    private val repo : MainRepo
) : EitherInteractor<Int, DeclarationPage, DeclarationFailure>(dispathcher) {

    override suspend fun invoke(executeParams: Int): Either<DeclarationFailure, DeclarationPage> {
        return repo.fetchDeclrations(executeParams)
    }
}

data class DeclarationPage(
    val count: Int,
    val next: Int?,
    val previous: Int?,
    val declarations : List<Declaration>
)
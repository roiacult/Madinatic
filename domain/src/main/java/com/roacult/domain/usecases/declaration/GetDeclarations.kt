package com.roacult.domain.usecases.declaration

import com.roacult.domain.entities.Declaration
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.repos.MainRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor

class GetDeclarations (
    dispathcher : CouroutineDispatchers,
    private val repo : MainRepo
) : EitherInteractor<Int, List<Declaration>, DeclarationFailure>(dispathcher) {

    override suspend fun invoke(executeParams: Int): Either<DeclarationFailure, List<Declaration>> {
        return repo.fetchDeclrations(executeParams)
    }
}
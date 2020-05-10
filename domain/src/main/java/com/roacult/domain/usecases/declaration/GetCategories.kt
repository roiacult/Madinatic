package com.roacult.domain.usecases.declaration

import com.roacult.domain.entities.Categorie
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.repos.MainRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor
import com.roacult.kero.team7.jstarter_domain.interactors.None

class GetCategories (
    dispathcher : CouroutineDispatchers,
    private val repo : MainRepo
) : EitherInteractor<None, List<Categorie>,DeclarationFailure >(dispathcher) {

    override suspend fun invoke(executeParams: None): Either<DeclarationFailure, List<Categorie>> {
        return repo.fetchCategories()
    }
}


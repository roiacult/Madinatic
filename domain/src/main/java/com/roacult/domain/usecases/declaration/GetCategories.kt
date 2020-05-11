package com.roacult.domain.usecases.declaration

import com.roacult.domain.entities.Categorie
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.repos.MainRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor
import com.roacult.kero.team7.jstarter_domain.interactors.None
import kotlinx.coroutines.delay

class GetCategories (
    dispathcher : CouroutineDispatchers,
    private val repo : MainRepo
) : EitherInteractor<None, List<Categorie>,DeclarationFailure >(dispathcher) {

    override suspend fun invoke(executeParams: None): Either<DeclarationFailure, List<Categorie>> {
//        return repo.fetchCategories()
        val list = ArrayList<Categorie>()

        list.add(Categorie("id1","categories test 1", ""))
        list.add(Categorie("id2","categories test 2", ""))
        list.add(Categorie("id3","categories test 3", ""))
        list.add(Categorie("id4","categories test 4", ""))

        delay(2000)
        return Either.Right(list)
    }
}


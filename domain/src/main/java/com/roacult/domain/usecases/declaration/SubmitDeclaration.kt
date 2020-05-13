package com.roacult.domain.usecases.declaration

import com.roacult.domain.entities.Declaration
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.repos.MainRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor
import com.roacult.kero.team7.jstarter_domain.interactors.None

class SubmitDeclaration(
    dispathcher : CouroutineDispatchers,
    private val repo : MainRepo
) : EitherInteractor<Declaration,None,DeclarationFailure>(dispathcher) {

    override suspend fun invoke(executeParams: Declaration): Either<DeclarationFailure, None> {
        return repo.submitDeclaration(executeParams)
    }
}


data class SubmitionParams(
    val declaration : Declaration,
    val submitionDocs : List<String>
)
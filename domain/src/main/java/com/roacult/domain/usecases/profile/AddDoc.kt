package com.roacult.domain.usecases.profile

import com.roacult.domain.entities.Attachment
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.repos.MainRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor
import com.roacult.kero.team7.jstarter_domain.interactors.None

class AddDoc (
    dispatcher: CouroutineDispatchers,
    private val repo : MainRepo
) : EitherInteractor<AddDocumentsParams, None, DeclarationFailure>(dispatcher) {

    override suspend fun invoke(executeParams: AddDocumentsParams): Either<DeclarationFailure, None> {
        return repo.postDocuments(executeParams)
    }
}

data class AddDocumentsParams(
    val delcaration : String,
    val docs : List<Attachment>
)
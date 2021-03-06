package com.roacult.domain.usecases.profile

import com.roacult.domain.entities.Attachment
import com.roacult.domain.entities.DeclarationState
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.repos.MainRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor
import com.roacult.kero.team7.jstarter_domain.interactors.None

class UpdateDeclaration (
    dispatcher: CouroutineDispatchers,
    private val repo : MainRepo
) : EitherInteractor<AddDocumentsParams, None, DeclarationFailure>(dispatcher) {

    override suspend fun invoke(executeParams: AddDocumentsParams): Either<DeclarationFailure, None> {
        return repo.postDocuments(executeParams)
    }
}

data class AddDocumentsParams(
    val delcaration : DeclarationUpdate,
    val docs : List<Attachment>
)

data class DeclarationUpdate(
    val id: String,
    val title: String,
    val categorie: String,
    val desc: String,
    val state: DeclarationState,
    val address: String,
    val lat: Double,
    val long: Double
)
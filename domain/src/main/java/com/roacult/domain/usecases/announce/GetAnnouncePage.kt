package com.roacult.domain.usecases.announce

import com.roacult.domain.entities.Announce
import com.roacult.domain.exceptions.DeclarationFailure
import com.roacult.domain.repos.MainRepo
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.EitherInteractor

class GetAnnouncePage(
    dispathcher : CouroutineDispatchers,
    private val repo : MainRepo
) : EitherInteractor<AnnouncePageParam, AnnouncePage, DeclarationFailure>(dispathcher) {

    override suspend fun invoke(executeParams: AnnouncePageParam): Either<DeclarationFailure, AnnouncePage> {
        return repo.fetchAnnouncePage(executeParams)
    }
}

data class AnnouncePage(
    val count: Int,
    val next: Int?,
    val previous: Int?,
    val declarations : List<Announce>
)

data class AnnouncePageParam(
    val page: Int,
    val announceFilter: AnnounceFilter,
    val currentDate: String?
)

enum class AnnounceFilter{
    CURRENT, UPCOMING, OLD, ALL
}
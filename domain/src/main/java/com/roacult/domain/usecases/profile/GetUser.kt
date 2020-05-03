package com.roacult.domain.usecases.profile

import com.roacult.domain.entities.User
import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.domain.repos.MainRepo
import com.roacult.domain.usecases.ObservableEitherInteractor
import com.roacult.kero.team7.jstarter_domain.functional.AppRxSchedulers
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.None
import io.reactivex.Observable

class GetUser (
    appRxSchedulers: AppRxSchedulers,
    private val repo : MainRepo
) : ObservableEitherInteractor<User, None,ProfileFailures>(appRxSchedulers) {

    override fun buildObservable(p: None): Observable<Either<ProfileFailures, User>> {
        return repo.getUserObservable()
    }

}
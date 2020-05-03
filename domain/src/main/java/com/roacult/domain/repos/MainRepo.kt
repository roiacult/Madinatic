package com.roacult.domain.repos

import com.roacult.domain.entities.User
import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.kero.team7.jstarter_domain.functional.Either
import io.reactivex.Observable

interface MainRepo {

    fun getUserObservable(): Observable<Either<ProfileFailures, User>>

}
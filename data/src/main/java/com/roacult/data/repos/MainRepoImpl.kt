package com.roacult.data.repos

import com.roacult.data.local.MainLocal
import com.roacult.domain.entities.User
import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.domain.repos.MainRepo
import com.roacult.kero.team7.jstarter_domain.functional.Either
import io.reactivex.Observable

class MainRepoImpl(
    private val mainLocal : MainLocal
) : MainRepo {

    /**
     * get user entity local observable
     * */
    override fun getUserObservable(): Observable<Either<ProfileFailures, User>> {
        return mainLocal.getUserObservable()
    }
}
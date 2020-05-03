package com.roacult.domain.repos

import com.roacult.domain.entities.User
import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.domain.usecases.profile.EditUserInfo
import com.roacult.kero.team7.jstarter_domain.functional.Either
import com.roacult.kero.team7.jstarter_domain.interactors.None
import io.reactivex.Observable

interface MainRepo {

    fun getUserObservable(): Observable<Either<ProfileFailures, User>>

    suspend fun editInfo(editUserInfo: EditUserInfo): Either<ProfileFailures, None>

}
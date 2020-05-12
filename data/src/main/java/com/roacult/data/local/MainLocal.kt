package com.roacult.data.local

import android.content.SharedPreferences
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.google.gson.Gson
import com.roacult.data.local.entities.UserLocalEntity
import com.roacult.data.utils.PREFRENCES
import com.roacult.domain.entities.User
import com.roacult.domain.exceptions.ProfileFailures
import com.roacult.kero.team7.jstarter_domain.functional.Either
import io.reactivex.Observable

class MainLocal(
    private val rxPreference: RxSharedPreferences,
    private val preferences: SharedPreferences,
    private val gson : Gson
){

    fun getUserObservable(): Observable<Either<ProfileFailures, User>> {

        return rxPreference.getString(PREFRENCES.USER).asObservable().map {
            val user = gson.fromJson(it, UserLocalEntity::class.java)
            if(user != null) Either.Right(user.toEntity(gson))
            else Either.Left(ProfileFailures.UserNotStored)
        }

    }

    fun getUser() : User {
        return gson.fromJson(
            preferences.getString(PREFRENCES.USER,""),
            UserLocalEntity::class.java
        ).toEntity(gson)
    }

}

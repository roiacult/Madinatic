package com.roacult.data

import com.google.gson.Gson
import com.roacult.data.repos.AuthRepoImpl
import com.roacult.data.repos.MainRepoImpl
import com.roacult.domain.repos.AuthRepo
import com.roacult.domain.repos.MainRepo
import org.koin.dsl.module

val dataModule = module {

    //provide json parser Gson
    single { Gson() }

    //add all  repositories here as singleton
    single<AuthRepo>{ AuthRepoImpl(get(),get(),get()) }
    single<MainRepo>{ MainRepoImpl(get(),get(),get(),get()) }
}
package com.roacult.data

import com.roacult.data.repos.AuthRepoImpl
import com.roacult.domain.repos.AuthRepo
import org.koin.dsl.module

val dataModule = module {
    //add all  repositories here as singleton
    // single instance of HelloRepository
    // single<HelloRepository> { HelloRepositoryImpl() }

    single <AuthRepo>{ AuthRepoImpl(get(),get(),get()) }
}
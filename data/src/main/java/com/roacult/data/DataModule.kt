package com.roacult.data

import org.koin.dsl.module

val dataModule = module {
    //add all  repositories here as singleton
    // single instance of HelloRepository
    // single<HelloRepository> { HelloRepositoryImpl() }
}
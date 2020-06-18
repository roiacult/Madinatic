package com.roacult.madinatic.di

import com.roacult.data.dataModule
import com.roacult.data.local.localModule
import com.roacult.data.remote.remoteModule
import com.roacult.domain.domainModule
import org.koin.core.module.Module

/**
 * add any new module here
 * */
fun getModules() : List<Module>{
    return listOf(
        domainModule,
        dataModule,
        localModule,
        remoteModule,
        appModule,
        viewModelModule
    )
}
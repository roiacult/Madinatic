package com.roacult.madinatic.di

import android.content.Context
import com.roacult.kero.team7.jstarter_domain.functional.AppRxSchedulers
import com.roacult.kero.team7.jstarter_domain.functional.CouroutineDispatchers
import com.roacult.madinatic.MadinaticApp
import com.roacult.madinatic.utils.StringProvider
import com.roacult.madinatic.utils.schedulers.AppCoroutineDispatchers
import com.roacult.madinatic.utils.schedulers.AppRxSchedulersImpl
import org.koin.dsl.module

val appModule = module {

    //schedulers
    single<AppRxSchedulers>{ AppRxSchedulersImpl() }
    single<CouroutineDispatchers>{ AppCoroutineDispatchers() }

    //application or context are provided by default
    single <StringProvider>{ (get<Context>() as MadinaticApp) }
}
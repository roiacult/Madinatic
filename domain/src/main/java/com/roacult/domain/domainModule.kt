package com.roacult.domain

import com.roacult.domain.usecases.auth.Login
import com.roacult.domain.usecases.auth.Register
import com.roacult.domain.usecases.auth.ResetPassword
import org.koin.dsl.module

val domainModule = module {
    //usecases
//    single { UserState(get(),get()) }
    single { Login(get(),get()) }
    single { ResetPassword(get(),get()) }
    single { Register(get(),get()) }
}
package com.roacult.domain

import com.roacult.domain.usecases.auth.Login
import com.roacult.domain.usecases.auth.Register
import com.roacult.domain.usecases.auth.ResetPassword
import com.roacult.domain.usecases.auth.UserState
import com.roacult.domain.usecases.declaration.GetCategories
import com.roacult.domain.usecases.declaration.SubmitDeclaration
import com.roacult.domain.usecases.profile.ChangePassword
import com.roacult.domain.usecases.profile.EditUserInfo
import com.roacult.domain.usecases.profile.UserObservable
import org.koin.dsl.module

val domainModule = module {
    //usecases
//    single { UserState(get(),get()) }
    single { Login(get(),get()) }
    single { ResetPassword(get(),get()) }
    single { Register(get(),get()) }
    single { UserState(get(),get()) }

    single { UserObservable(get(),get()) }
    single { EditUserInfo(get(),get()) }

    single { ChangePassword(get(),get()) }
    single { GetCategories(get(),get()) }
    single { SubmitDeclaration(get(),get()) }
}
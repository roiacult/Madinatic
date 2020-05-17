package com.roacult.domain

import com.roacult.domain.usecases.auth.*
import com.roacult.domain.usecases.declaration.GetCategories
import com.roacult.domain.usecases.declaration.GetDeclarationPage
import com.roacult.domain.usecases.declaration.SubmitDeclaration
import com.roacult.domain.usecases.profile.*
import org.koin.dsl.module

val domainModule = module {
    //usecases
//    single { UserState(get(),get()) }
    single { Login(get(),get()) }
    single { ResetPassword(get(),get()) }
    single { Register(get(),get()) }
    single { UserState(get(),get()) }
    single { Logout(get()) }

    single { UserObservable(get(),get()) }
    single { EditUserInfo(get(),get()) }

    single { ChangePassword(get(),get()) }
    single { GetCategories(get(),get()) }
    single { SubmitDeclaration(get(),get()) }
    single { GetDeclarationPage(get(),get()) }
    single { UserDeclarationPage(get(),get()) }
    single { UserUnfinishedDeclaration(get(),get()) }
}
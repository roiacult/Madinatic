package com.roacult.madinatic.di

import com.roacult.madinatic.ui.MainViewModel
import com.roacult.madinatic.ui.auth.LoginViewModel
import com.roacult.madinatic.ui.auth.RegisterViewModel
import com.roacult.madinatic.ui.profile.ProfileViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =  module{

    //provide all viewModel here
    viewModel { LoginViewModel(get(),get(),get()) }
    viewModel { MainViewModel(get()) }
    viewModel { RegisterViewModel(get(),get()) }
    viewModel { ProfileViewModel(get(),get()) }
}
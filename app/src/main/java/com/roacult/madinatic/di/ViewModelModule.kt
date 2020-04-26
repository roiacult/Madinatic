package com.roacult.madinatic.di

import com.roacult.madinatic.ui.MainViewModel
import com.roacult.madinatic.ui.auth.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =  module{

    //provide all viewModel here
//    viewModel { AuthActivityViewModel(get()) }
    viewModel { LoginViewModel(get(),get(),get()) }
    viewModel { MainViewModel() }
}
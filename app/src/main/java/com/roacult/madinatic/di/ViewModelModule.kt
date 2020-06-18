package com.roacult.madinatic.di

import com.roacult.madinatic.ui.MainViewModel
import com.roacult.madinatic.ui.announce.AnnounceViewModel
import com.roacult.madinatic.ui.auth.LoginViewModel
import com.roacult.madinatic.ui.auth.RegisterViewModel
import com.roacult.madinatic.ui.declaration.DeclarationViewModel
import com.roacult.madinatic.ui.declaration.adddeclaration.AddDeclarationViewModel
import com.roacult.madinatic.ui.declaration.declarationdetails.DeclarationDetailsViewModel
import com.roacult.madinatic.ui.profile.editinfo.EditInfoViewModel
import com.roacult.madinatic.ui.profile.ProfileViewModel
import com.roacult.madinatic.ui.profile.alldeclarations.AllDeclarationViewModel
import com.roacult.madinatic.ui.profile.unfinished.UnfinishedDeclarationViewModel
import com.roacult.madinatic.ui.profile.unfinished.update_declaration.UpdateDeclarationViewModel
import com.roacult.madinatic.ui.profile.updatepass.UpdatePasswordViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =  module{

    //provide all viewModel here
    viewModel { LoginViewModel(get(),get(),get()) }
    viewModel { MainViewModel(get()) }
    viewModel { RegisterViewModel(get(),get()) }
    viewModel { ProfileViewModel(get(),get(),get()) }
    viewModel { EditInfoViewModel(get(), get()) }
    viewModel { UpdatePasswordViewModel(get(), get()) }
    viewModel { AddDeclarationViewModel(get(),get(),get()) }
    viewModel { DeclarationViewModel(get(),get(),get())}
    viewModel { DeclarationDetailsViewModel(get()) }
    viewModel { AllDeclarationViewModel(get(),get(),get()) }
    viewModel { UnfinishedDeclarationViewModel(get(),get(),get()) }
    viewModel { UpdateDeclarationViewModel(get(),get(),get(),get(),get()) }
    viewModel { AnnounceViewModel(get(),get()) }
}
package com.roacult.madinatic.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.LoginBinding
import com.roacult.madinatic.databinding.ResetPasswordBinding
import com.roacult.madinatic.ui.profile.ProfileFragment
import com.roacult.madinatic.utils.navigation.FragmentNavigation
import com.roacult.madinatic.utils.navigation.NavigationOption
import com.roacult.madinatic.utils.states.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class LoginFragment : FullScreenFragment<LoginBinding>() {
    override val layutIdRes = R.layout.login

    private val viewModel : LoginViewModel by viewModel()

    private lateinit var dialogue : AlertDialog
    private lateinit var resetPasswordBinding : ResetPasswordBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        viewModel.observe(this){
            it.errorMsg?.getContentIfNotHandled()?.apply {
                onError(this)
            }
            it.showMsg?.getContentIfNotHandled()?.apply {
                showMessage(this)
            }
            it.login?.apply { handleLoginState(this) }
            it.resetPassword?.apply { handleResetPassword(this) }
        }
    }

    private fun initViews() {
        binding.viewModel = viewModel
        binding.resetPass.setOnClickListener{ forgetPasswordDialogue() }
        binding.register.setOnClickListener {
            vm.navigate(
                FragmentNavigation(destinationClass = RegistrationFragment::class.java)
            )
        }
    }

    private fun handleLoginState(async: Async<None>) {
        //TODO change it later
        Timber.v("handling login state ....")
        when (async) {
            is Loading -> binding.state = ViewState.LOADING
            is Fail<*, *> -> binding.state = ViewState.EMPTY
            is Success -> {
                binding.state = ViewState.SUCCESS
                vm.navigate(FragmentNavigation(isReplace = true,
                    destinationClass = ProfileFragment::class.java,
                    navigationOption = NavigationOption(
                        addToBackStack = false,
                        popUpBackStack = true
                    )
                ))
            }
        }
    }

    private fun forgetPasswordDialogue() {
        dialogue = AlertDialog.Builder(context!!).apply {
            resetPasswordBinding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.reset_password,
                null,
                false
            )
            setView(resetPasswordBinding.root)
            resetPasswordBinding.viewModel = viewModel
        }.create()
        dialogue.show()
    }


    private fun handleResetPassword(async: Async<None>) {
        when(async){
            is Success -> {
                dialogue.hide()
                showMessage(getString(R.string.resetpassword_succ))
                viewModel.finishResetPassword()
            }
            is Loading -> {
                resetPasswordBinding.state = ViewState.LOADING
            }
            is Fail<*,*> -> {
                resetPasswordBinding.state = ViewState.EMPTY
            }
        }
    }
}
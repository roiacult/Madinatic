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
import com.roacult.madinatic.utils.states.*
import org.koin.androidx.viewmodel.ext.android.viewModel

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
    }

    private fun handleLoginState(async: Async<None>) {
        //TODO change it later
        when (async) {
            is Loading -> binding.state = ViewState.LOADING
            is Fail<*, *> -> binding.state = ViewState.EMPTY
            is Success -> {
                //TODO go to main page here
                showMessage("TODO go to main here")
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
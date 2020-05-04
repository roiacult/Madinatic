package com.roacult.madinatic.ui.profile

import android.os.Bundle
import android.view.View
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.UpdatePasswordBinding
import com.roacult.madinatic.utils.states.Async
import com.roacult.madinatic.utils.states.Fail
import com.roacult.madinatic.utils.states.Loading
import com.roacult.madinatic.utils.states.Success
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdatePasswordFragment : FullScreenFragment<UpdatePasswordBinding>(){

    override val layutIdRes = R.layout.update_password
    private val viewModel : UpdatePasswordViewModel by viewModel()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        viewModel.observe(this){
            it.erroMsg?.getContentIfNotHandled()?.let(::onError)
            handleUpdatePssword(it.updatePassword)
        }
    }

    private fun handleUpdatePssword(updatePassword: Async<None>) {
        when(updatePassword){
            is Loading -> showLoading(true)
            is Fail<*, *> -> showLoading(false)
            is Success -> {
                showLoading(false)
                showMessage(getString(R.string.updatepass_succ))
                viewModel.endUpdatePassword()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.oldpass.isEnabled = !show
        binding.newpass.isEnabled = !show
        binding.newpass2.isEnabled = !show
        binding.cancel.isEnabled = !show
        binding.save.isEnabled = !show
    }

    private fun initViews() {
        binding.viewModel =viewModel

        binding.cancel.setOnClickListener {
            activity?.onBackPressed()
        }
    }
}
package com.roacult.madinatic.ui.auth

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.RegisterBinding
import com.roacult.madinatic.utils.states.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.text.SimpleDateFormat

class RegistrationFragment : FullScreenFragment<RegisterBinding>() {

    override val layutIdRes = R.layout.register

    private val viewModel : RegisterViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        viewModel.observe(this){
            it.errorMsg?.getContentIfNotHandled()?.apply {
                onError(this)
            }

            it.registration?.apply {
                handleRegistration(this)
            }
        }
    }

    private fun initViews() {
        binding.viewModel = viewModel
        binding.login.setOnClickListener { activity?.onBackPressed() }
        binding.dateBirth.setOnClickListener {showDatePicker()}
        binding.register.setOnClickListener {
            viewModel.register(
                binding.firstname.text.toString(),
                binding.lastname.text.toString(),
                binding.email.text.toString(),
                binding.phone.text.toString(),
                binding.address.text.toString(),
                binding.password.text.toString(),
                binding.password2.text.toString(),
                binding.nationalid.text.toString(),
                binding.dateBirth.text.toString()
            )
        }
    }

    private fun handleRegistration(async: Async<None>) {
        when(async) {
            is Loading -> binding.state = ViewState.LOADING
            is Fail<*,*> -> binding.state = ViewState.EMPTY
            is Success -> {
                binding.state = ViewState.SUCCESS
                viewModel.endRegistration()
                showSuccDialgue()
            }
        }
    }

    private fun showDatePicker() {
        DatePickerDialog(context!!,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                binding.dateBirth.text = "$year-${month+1}-$dayOfMonth"
            },2020,1,1).apply {
            this.datePicker.maxDate = 1024358400000L
            show()
        }
    }

    private fun showSuccDialgue() {
        AlertDialog
            .Builder(context!!)
            .apply {
                setTitle(R.string.reg_succ_title)
                setMessage(R.string.regi_succ_msg)
                setPositiveButton(R.string.close){_,_->
                    this@RegistrationFragment.activity?.onBackPressed()
                }
                setOnDismissListener {
                    this@RegistrationFragment.activity?.onBackPressed()
                }
            }.show()
    }
}
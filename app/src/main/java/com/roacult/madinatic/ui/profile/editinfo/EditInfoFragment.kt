package com.roacult.madinatic.ui.profile.editinfo

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.roacult.kero.team7.jstarter_domain.interactors.None
import com.roacult.madinatic.R
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.ProfileChangeInfoBinding
import com.roacult.madinatic.utils.extensions.toUser
import com.roacult.madinatic.utils.states.Async
import com.roacult.madinatic.utils.states.Fail
import com.roacult.madinatic.utils.states.Loading
import com.roacult.madinatic.utils.states.Success
import com.squareup.picasso.Picasso
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class EditInfoFragment : FullScreenFragment<ProfileChangeInfoBinding>() {
    override val layutIdRes = R.layout.profile_change_info

    private val viewModel : EditInfoViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        viewModel.observe(this){
            it.erroMsg?.getContentIfNotHandled()?.let(::onError)
            handleSaveUserData(it.saveUserData)
        }

    }

    private fun handleSaveUserData(saveUserData: Async<None>) {
        when(saveUserData){
            is Loading -> showLoading(true)
            is Fail<*,*> -> showLoading(false)
            is Success -> {
                showLoading(false)
                showMessage(getString(R.string.editinfo_succ))
                viewModel.finishSaveUserData()
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.firstName.isEnabled = !show
        binding.lastName.isEnabled= !show
        binding.email.isEnabled= !show
        binding.phoneNumber.isEnabled= !show
        binding.address.isEnabled= !show
        binding.materialButton.isEnabled = !show
        binding.save.isEnabled = !show
        binding.cancel.isEnabled = !show
        binding.ediBtn.isEnabled = !show
    }

    private fun initViews() {
        syncData()

        binding.ediBtn.setOnClickListener {
            CropImage.activity()
                .setCropShape(CropImageView.CropShape.OVAL)
                .setAspectRatio(1, 1)
                .start(context!!, this)
        }

        binding.save.setOnClickListener {
            saveData()
            viewModel.saveUserInfo()
        }

        binding.cancel.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.materialButton.setOnClickListener {
            showDatePicker()
        }

    }

    private fun syncData() {
        viewModel.setData(arguments!!.toUser())
        val user = viewModel.user
        binding.firstName.setText(user.first_name)
        binding.lastName.setText(user.last_name)
        binding.email.setText(user.email)
        binding.phoneNumber.setText(user.phone)
        binding.address.setText(user.address)
        binding.materialButton.text = user.dateBirth
        if(viewModel.image != null ){
            binding.avatarImage.setImageURI(Uri.fromFile(File(viewModel.image!!)))
        }else if(user.image != null) {
            Picasso.get().load(user.image!!.replace("http","https")).into(binding.avatarImage)
        }
    }

    fun saveData(){
        viewModel.user = viewModel.user.copy(
            first_name = binding.firstName.text.toString(),
            last_name = binding.lastName.text.toString(),
            email = binding.email.text.toString(),
            phone = binding.phoneNumber.text.toString(),
            address = binding.address.text.toString(),
            dateBirth = binding.materialButton.text.toString()
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                val result = CropImage.getActivityResult(data)
                viewModel.image = result.uri.path
                binding.avatarImage.setImageURI(result.uri)
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        saveData()
    }

    @SuppressLint("SetTextI18n")
    private fun showDatePicker() {
        DatePickerDialog(context!!,
            DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                binding.materialButton.text = "$year-${month+1}-$dayOfMonth"
            },2020,1,1).show()
    }
}
package com.roacult.madinatic.ui.profile

import android.os.Bundle
import android.view.View
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseFragment
import com.roacult.madinatic.databinding.ProfileBinding
import com.roacult.madinatic.utils.navigation.FragmentNavigation
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<ProfileBinding>() {

    override val layutIdRes = R.layout.profile

    private val viewModel : ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        viewModel.observe(this){
            it.errorMsg?.getContentIfNotHandled()?.let(::onError)
            it.clickEvent?.getContentIfNotHandled()?.let(::handlClickEvent)
            it.user?.apply { binding.user = this }
        }
    }

    private fun initViews() {
        binding.viewModel = viewModel
    }

    private fun handlClickEvent(profileClickEvent: ProfileClickEvent) {
        when(profileClickEvent) {
            ProfileClickEvent.CHANGEINFO -> {
                vm.navigate(FragmentNavigation(destinationClass = EditInfoFragment::class.java))
            }
            ProfileClickEvent.CHANGEPASSWORD -> {
                //TODO
            }
        }
    }
}
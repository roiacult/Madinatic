package com.roacult.madinatic.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.google.android.material.appbar.AppBarLayout
import com.roacult.madinatic.R
import com.roacult.madinatic.base.BaseFragment
import com.roacult.madinatic.databinding.ProfileBinding
import com.roacult.madinatic.ui.MainActivity
import com.roacult.madinatic.ui.profile.alldeclarations.AllDeclarationFragment
import com.roacult.madinatic.ui.profile.editinfo.EditInfoFragment
import com.roacult.madinatic.ui.profile.unfinished.UnfinishedDeclarationFragment
import com.roacult.madinatic.ui.profile.unfinished.UnfinishedDeclarationViewModel
import com.roacult.madinatic.ui.profile.updatepass.UpdatePasswordFragment
import com.roacult.madinatic.utils.AppBarStateChangeListener
import com.roacult.madinatic.utils.extensions.toBunndle
import com.roacult.madinatic.utils.navigation.FragmentNavigation
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : BaseFragment<ProfileBinding>() {

    override val layutIdRes = R.layout.profile

    private val viewModel : ProfileViewModel by viewModel()

    private val appBarStateListener = object : AppBarStateChangeListener() {
        override fun onStateChanged(appBarLayout: AppBarLayout, state: State) {
            when (state) {
                State.COLLAPSED -> {
                    setToolbarTitle(false)
                }
                State.EXPANDED -> {
                    setToolbarTitle(true)
                }
                State.IDLE -> {
                    setToolbarTitle(true)
                }
            }
        }
    }

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
        binding.appbar.addOnOffsetChangedListener(appBarStateListener)
        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.edit_info -> {
                    handlClickEvent(ProfileClickEvent.CHANGEINFO)
                    true
                }
                R.id.changepass -> {
                    handlClickEvent(ProfileClickEvent.CHANGEPASSWORD)
                    true
                }
                R.id.logout -> {
                    handlClickEvent(ProfileClickEvent.LOGOUT)
                    true
                }

                else -> false
            }
        }
    }

    private fun setToolbarTitle(show: Boolean) {
        if (show) {
            binding.collapse.title = ""
            return
        }
        viewModel.withState {
            binding.collapse.title = binding.user?.first_name+" , "+binding.user?.last_name
        }
    }

    private fun handlClickEvent(profileClickEvent: ProfileClickEvent) {
        when(profileClickEvent) {
            ProfileClickEvent.CHANGEINFO -> {
                viewModel.withState {
                    if (it.user != null ){
                        vm.navigate(
                            FragmentNavigation(
                                destinationClass = EditInfoFragment::class.java,
                                fragmentArguments = it.user.toBunndle()
                            )
                        )
                    }
                }
            }
            ProfileClickEvent.CHANGEPASSWORD -> {
                vm.navigate(FragmentNavigation(destinationClass = UpdatePasswordFragment::class.java))
            }

            ProfileClickEvent.LOGOUT -> {
                viewModel.logout()
                val intent = Intent(context, MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                startActivity(intent)
            }
            ProfileClickEvent.ALLDECLARATIONS -> {
                vm.navigate(FragmentNavigation(destinationClass = AllDeclarationFragment::class.java))
            }

            ProfileClickEvent.UPDATES -> {
                vm.navigate(FragmentNavigation(destinationClass = UnfinishedDeclarationFragment::class.java))
            }
        }
    }
}
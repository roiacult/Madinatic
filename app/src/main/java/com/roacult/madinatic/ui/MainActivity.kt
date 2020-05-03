package com.roacult.madinatic.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.roacult.madinatic.R
import com.roacult.madinatic.base.ActivityNavigator
import com.roacult.madinatic.databinding.MainBinding
import com.roacult.madinatic.ui.auth.LoginFragment
import com.roacult.madinatic.ui.profile.ProfileFragment
import com.roacult.madinatic.utils.navigation.FragmentNavigation
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ActivityNavigator<MainViewModel>(){

    override val viewModel: MainViewModel by viewModel()
    override val container = R.id.main_container
    private lateinit var binding : MainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.main)
        viewModel.observe(this){
            it.bottomNavState?.getContentIfNotHandled()?.apply {
                handleBottomNavState(this)
            }
        }
        viewModel.navigate(FragmentNavigation(
            destinationClass = ProfileFragment::class.java
        ))
    }

    private fun handleBottomNavState(show: Boolean) {
        if (show) binding.motion.transitionToEnd()
        else binding.motion.transitionToStart()
    }
}
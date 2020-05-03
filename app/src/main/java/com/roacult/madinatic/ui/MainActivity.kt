package com.roacult.madinatic.ui

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.roacult.madinatic.R
import com.roacult.madinatic.base.ActivityNavigator
import com.roacult.madinatic.databinding.MainBinding
import com.roacult.madinatic.ui.announce.AnounceFragment
import com.roacult.madinatic.ui.declaration.DeclarationFragment
import com.roacult.madinatic.ui.profile.ProfileFragment
import com.roacult.madinatic.utils.MainFragemtnsTags
import com.roacult.madinatic.utils.extensions.inTransaction
import com.roacult.madinatic.utils.navigation.FragmentNavigation
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ActivityNavigator<MainViewModel>(){

    private val declarationFragment : DeclarationFragment by lazy {
        val fragment = supportFragmentManager.findFragmentByTag(MainFragemtnsTags.DECLARATION)
        if (fragment != null) {
            fragment as DeclarationFragment
        } else {
            val dec = DeclarationFragment()
            supportFragmentManager.inTransaction {
                add(R.id.main_container, dec, MainFragemtnsTags.DECLARATION)
                hide(dec)
            }
            dec
        }
    }

    private val anounceFragment : AnounceFragment by lazy {
        val fragment = supportFragmentManager.findFragmentByTag(MainFragemtnsTags.ANNOUNCE)
        if (fragment != null) {
            fragment as AnounceFragment
        } else {
            val ann = AnounceFragment()
            supportFragmentManager.inTransaction {
                add(R.id.main_container, ann, MainFragemtnsTags.ANNOUNCE)
                hide(ann)
            }
            ann
        }
    }

    private val profileFragment : ProfileFragment by lazy {
        val fragment = supportFragmentManager.findFragmentByTag(MainFragemtnsTags.PROFILE)
        if (fragment != null) {
            fragment as ProfileFragment
        } else {
            val homeFragment = ProfileFragment()
            supportFragmentManager.inTransaction {
                add(R.id.main_container, homeFragment, MainFragemtnsTags.PROFILE)
                hide(homeFragment)
            }
            homeFragment
        }
    }

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
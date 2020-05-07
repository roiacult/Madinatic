package com.roacult.madinatic.ui

import android.os.Bundle
import android.provider.ContactsContract
import androidx.databinding.DataBindingUtil
import com.roacult.madinatic.R
import com.roacult.madinatic.base.ActivityNavigator
import com.roacult.madinatic.base.BaseFragment
import com.roacult.madinatic.base.FullScreenFragment
import com.roacult.madinatic.databinding.MainBinding
import com.roacult.madinatic.ui.announce.AnounceFragment
import com.roacult.madinatic.ui.auth.LoginFragment
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
    private var currentFragment : BaseFragment<*>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme_NoActionBar)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.main)

        if (viewModel.homeStarted) startHome(viewModel.selectedFragment)

        viewModel.observe(this){
            it.bottomNavState?.getContentIfNotHandled()?.let(::handleBottomNavState)
            it.userState?.getContentIfNotHandled()?.let(::handleUserState)
        }
    }

    private fun handleBottomNavState(show: Boolean) {
        if (show) binding.motion.transitionToEnd()
        else binding.motion.transitionToStart()
    }

    private fun handleUserState(authState: Boolean) {
        if (authState) {
            startHome()
        } else supportFragmentManager.inTransaction {
            add(R.id.main_container, LoginFragment(), MainFragemtnsTags.LOGIN)
        }
    }

    private fun startHome(selectedFragment: Int = R.id.declaration) {

        viewModel.homeStarted = true
        val fr = supportFragmentManager.findFragmentByTag(MainFragemtnsTags.LOGIN)
        if (fr != null) supportFragmentManager.inTransaction {
            remove(fr)
        }

        viewModel.selectedFragment = selectedFragment
        viewModel.showBottomNav()
        // initialise current fragment

        currentFragment = when (viewModel.selectedFragment) {
            R.id.declaration -> declarationFragment
            R.id.announce -> anounceFragment
            R.id.profile -> profileFragment
            else -> declarationFragment
        }

        // show current active fragment
        supportFragmentManager.inTransaction {
            show(currentFragment!!)
        }

        binding.bottomNav.selectedItemId = viewModel.selectedFragment
        binding.bottomNav.setOnNavigationItemSelectedListener {
            setFragment(it.itemId)
        }
    }

    private fun setFragment(itemId: Int): Boolean {
        viewModel.selectedFragment = itemId
        when (itemId) {
            R.id.declaration -> {
                if (currentFragment is DeclarationFragment) return false
                supportFragmentManager.inTransaction {
                    hide(currentFragment!!)
                    show(declarationFragment)
                }
                currentFragment = declarationFragment
            }
            R.id.announce -> {
                if (currentFragment is AnounceFragment) return false
                supportFragmentManager.inTransaction {
                    hide(currentFragment!!)
                    show(anounceFragment)
                }
                currentFragment = anounceFragment
            }
            R.id.profile -> {
                if (currentFragment is ProfileFragment) return false
                supportFragmentManager.inTransaction {
                    hide(currentFragment!!)
                    show(profileFragment)
                    attach(profileFragment)
                }
                currentFragment = profileFragment
            }
        }
        return true
    }

    override fun onBackPressed() {
        if (viewModel.homeStarted && supportFragmentManager.backStackEntryCount == 0 && viewModel.selectedFragment != R.id.declaration) {
            binding.bottomNav.selectedItemId = R.id.declaration
        } else super.onBackPressed()
        updateBottomNavState()
    }

    fun updateBottomNavState() {
        if (viewModel.homeStarted) {
            val fr = supportFragmentManager.findFragmentById(R.id.main_container)
            if (fr is FullScreenFragment<*>) viewModel.disableBottomNav()
            else if (fr is BaseFragment<*>) viewModel.showBottomNav()
        }
    }

    override fun navigate(navigation: FragmentNavigation) {
        val selected = when(navigation.destinationClass){
            DeclarationFragment::class.java -> R.id.declaration
            AnounceFragment::class.java -> R.id.announce
            ProfileFragment::class.java ->R.id.profile
            else -> null
        }
        if(selected != null) {
          startHome(selected)
        } else super.navigate(navigation)
    }
}
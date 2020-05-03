package com.roacult.madinatic.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.fragment.app.Fragment
import com.roacult.madinatic.utils.extensions.inTransaction
import com.roacult.madinatic.utils.navigation.FragmentNavigation
import timber.log.Timber

abstract class ActivityNavigator<VM: NavigationViewModel<*>> : BaseActivity(){

    abstract val viewModel :VM
    abstract val container :Int
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this){
            it.navigationEvent?.getContentIfNotHandled()?.apply { handleNavigation(this) }
        }
    }



    private fun handleNavigation(navigation: FragmentNavigation) {
        preNavigation(navigation)
        navigate(navigation)
    }


    open fun preNavigation(navigation : FragmentNavigation) {

        if(navigation.navigationOption.popUpBackStack){
            // remove all added Fragment
            for (i in 0 until supportFragmentManager.backStackEntryCount) {
                supportFragmentManager.popBackStack()
            }
        }
    }

    open fun navigate(navigation: FragmentNavigation) {
        if (navigation.isADialogueFragment) {
            try {
                val fr = navigation.destinationClass.newInstance() as AppCompatDialogFragment
                if (!navigation.fragmentArguments.isEmpty) {
                    fr.arguments = navigation.fragmentArguments
                }
                Timber.e("we are about to show")
                fr.show(supportFragmentManager, null)
            } catch (ex: Exception) {
                throw IllegalArgumentException(
                    "class ${navigation.destinationClass.name} not found or " +
                            "class doesn't have an empty constructor"
                )
            }
            return
        }

        supportFragmentManager.inTransaction {

            // instantiate Fragment class
            // i didn't passe Fragment instance throw ViewModel State
            // to avoid leaking of fragment
            val fragment = try {
                navigation.destinationClass.newInstance() as Fragment
            } catch (ex: Exception) {
                throw IllegalArgumentException(
                    "class ${navigation.destinationClass.name} not found or " +
                            "class doesn't have an empty constructor"
                )
            }

            // set arguments
            fragment.arguments = navigation.fragmentArguments

            // set navigation Options
            val options = navigation.navigationOption
            if (options.addToBackStack) addToBackStack(navigation.destinationClass.name)
            setCustomAnimations(options.entreAnime, options.exitAnim, options.popEntreAnime, options.popExitAnime)

            // add fragments
            if (navigation.isReplace) {
                replace(container, fragment)
            } else {
                add(container, fragment)
            }
        }
    }
}
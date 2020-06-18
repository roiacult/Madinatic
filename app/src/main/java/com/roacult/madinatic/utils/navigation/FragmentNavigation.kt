package com.roacult.madinatic.utils.navigation

import android.os.Bundle

data class FragmentNavigation(
    val destinationClass: Class<*>,
    val fragmentArguments: Bundle = Bundle(),
    val navigationOption: NavigationOption = NavigationOption(),
    val isADialogueFragment: Boolean = false,
    val isReplace: Boolean = false
)

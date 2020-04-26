package com.roacult.madinatic.utils.navigation

data class NavigationOption(
    val addToBackStack: Boolean = true,
    val popUpBackStack: Boolean = false,
    val entreAnime: Int = 0,
    val exitAnim: Int = 0,
    val popEntreAnime: Int = 0,
    val popExitAnime: Int = 0
)
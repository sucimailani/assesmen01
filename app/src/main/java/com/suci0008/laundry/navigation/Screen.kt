package com.suci0008.laundry.navigation

sealed class Screen(val route: String) {
    data object Home: Screen("mainScreen")
    data object About: Screen("AboutScreen")
}
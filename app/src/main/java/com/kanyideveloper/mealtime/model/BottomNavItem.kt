package com.kanyideveloper.mealtime.model

import com.kanyideveloper.mealtime.R
import com.kanyideveloper.mealtime.screens.destinations.*

sealed class BottomNavItem(var title: String, var icon: Int, var destination: Destination) {
    object Home : BottomNavItem(
        title = "Home",
        icon = R.drawable.ic_home,
        destination = HomeScreenDestination
    )
    object Search: BottomNavItem(
        title = "Search",
        icon = R.drawable.ic_search,
        destination = SearchScreenDestination
    )
    object Favorites: BottomNavItem(
        title = "Favorites",
        icon = R.drawable.ic_favorites,
        destination = FavoritesScreenDestination
    )
    object Settings: BottomNavItem(
        title = "Settings",
        icon = R.drawable.ic_settings,
        destination = SettingsScreenDestination
    )
}
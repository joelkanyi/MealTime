/*
 * Copyright 2022 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kanyideveloper.core.domain.model

import com.kanyideveloper.mealtime.R
import com.kanyideveloper.mealtime.screens.destinations.Destination
import com.kanyideveloper.mealtime.screens.destinations.FavoritesScreenDestination
import com.kanyideveloper.mealtime.screens.destinations.HomeScreenDestination
import com.kanyideveloper.mealtime.screens.destinations.SearchScreenDestination
import com.kanyideveloper.mealtime.screens.destinations.SettingsScreenDestination

sealed class BottomNavItem(var title: String, var icon: Int, var destination: Destination) {
    object Home : BottomNavItem(
        title = "Home",
        icon = R.drawable.ic_home,
        destination = HomeScreenDestination
    )

    object Search : BottomNavItem(
        title = "Search",
        icon = R.drawable.ic_search,
        destination = SearchScreenDestination
    )

    object Favorites : BottomNavItem(
        title = "Favorites",
        icon = R.drawable.ic_favorites,
        destination = FavoritesScreenDestination
    )

    object Settings : BottomNavItem(
        title = "Settings",
        icon = R.drawable.ic_settings,
        destination = SettingsScreenDestination
    )
}

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
package com.kanyideveloper.mealtime.navigation

import android.net.Uri
import androidx.navigation.NavController
import com.kanyideveloper.addmeal.presentation.addmeal.AddMealNavigator
import com.kanyideveloper.addmeal.presentation.addmeal.destinations.AddMealScreenDestination
import com.kanyideveloper.addmeal.presentation.addmeal.destinations.NextAddMealScreenDestination
import com.kanyideveloper.destinations.DetailsScreenDestination
import com.kanyideveloper.destinations.HomeScreenDestination
import com.kanyideveloper.favorites.presentation.favorites.FavoritesNavigator
import com.kanyideveloper.home.presentation.home.HomeNavigator
import com.kanyideveloper.search.presentation.search.SearchNavigator
import com.kanyideveloper.settings.presentation.SettingsNavigator
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import timber.log.Timber

class CoreFeatureNavigator(
    private val navGraph: NavGraphSpec,
    private val navController: NavController
) : HomeNavigator, SearchNavigator, FavoritesNavigator, SettingsNavigator, AddMealNavigator {
    override fun openFavorites(showId: Long) {
        Timber.d("Favorites")
    }

    override fun openAddMeal() {
        navController.navigate(AddMealScreenDestination within navGraph)
    }

    override fun openSearch(showId: Long) {
        Timber.d("Search")
    }

    override fun openSettings(showId: Long) {
        Timber.d("Settings")
    }

    override fun openMealDetails() {
        navController.navigate(DetailsScreenDestination within navGraph)
    }

    override fun openNextAddMealScreen(
        imageUri: Uri,
        mealName: String,
        category: String,
        complexity: String,
        cookingTime: Int,
        servingPeople: Int
    ) {
        navController.navigate(
            NextAddMealScreenDestination(
                imageUri = imageUri,
                mealName = mealName,
                cookingTime = cookingTime,
                servingPeople = servingPeople,
                complexity = complexity,
                category = category
            ) within navGraph
        )
    }

    override fun popBackStack() {
        navController.popBackStack()
    }

    override fun navigateBackToHome() {
        navController.navigate(HomeScreenDestination within navGraph)
        navController.clearBackStack("home")
    }
}

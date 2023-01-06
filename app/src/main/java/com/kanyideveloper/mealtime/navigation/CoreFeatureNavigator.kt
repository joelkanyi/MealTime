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
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.favorites.presentation.favorites.presentation.FavoritesNavigator
import com.kanyideveloper.mealplanner.MealPlannerNavigator
import com.kanyideveloper.mealplanner.destinations.AllergiesScreenDestination
import com.kanyideveloper.mealplanner.destinations.MealPlannerScreenDestination
import com.kanyideveloper.mealplanner.destinations.MealTypesScreenDestination
import com.kanyideveloper.mealplanner.destinations.NumberOfPeopleScreenDestination
import com.kanyideveloper.presentation.destinations.DetailsScreenDestination
import com.kanyideveloper.presentation.destinations.HomeScreenDestination
import com.kanyideveloper.presentation.destinations.OnlineMealDetailsScreenDestination
import com.kanyideveloper.presentation.home.HomeNavigator
import com.kanyideveloper.randommeal.RandomMealNavigator
import com.kanyideveloper.randommeal.destinations.RandomMealScreenDestination
import com.kanyideveloper.search.presentation.search.SearchNavigator
import com.kanyideveloper.settings.presentation.SettingsNavigator
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec
import timber.log.Timber

class CoreFeatureNavigator(
    private val navGraph: NavGraphSpec,
    private val navController: NavController
) : HomeNavigator,
    SearchNavigator,
    FavoritesNavigator,
    SettingsNavigator,
    AddMealNavigator,
    RandomMealNavigator,
    MealPlannerNavigator {
    override fun openAddMeal() {
        navController.navigate(AddMealScreenDestination within navGraph)
    }

    override fun openSearch(showId: Long) {
        Timber.d("Search")
    }

    override fun openSettings(showId: Long) {
        Timber.d("Settings")
    }

    override fun openMealDetails(meal: Meal) {
        navController.navigate(DetailsScreenDestination(meal = meal) within navGraph)
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

    override fun openAllergiesScreen() {
        navController.navigate(AllergiesScreenDestination within navGraph)
    }

    override fun openNoOfPeopleScreen(allergies: String) {
        navController.navigate(
            NumberOfPeopleScreenDestination(allergies = allergies) within navGraph
        )
    }

    override fun openMealTypesScreen(allergies: String, noOfPeople: String) {
        navController.navigate(
            MealTypesScreenDestination(
                allergies = allergies,
                numberOfPeople = noOfPeople
            ) within navGraph
        )
    }

    override fun openMealPlanner() {
        navController.navigate(MealPlannerScreenDestination within navGraph)
    }

    override fun openOnlineMealDetails(mealId: String) {
        navController.navigate(OnlineMealDetailsScreenDestination(mealId = mealId) within navGraph)
    }

    override fun openRandomMeals() {
        navController.navigate(RandomMealScreenDestination within navGraph)
    }

    override fun navigateBackToHome() {
        navController.navigate(HomeScreenDestination within navGraph)
        navController.clearBackStack("home")
    }
}

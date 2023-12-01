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
import com.joelkanyi.admeal.presentation.AddMealNavigator
import com.joelkanyi.admeal.presentation.destinations.AddMealScreenDestination
import com.joelkanyi.admeal.presentation.destinations.NextAddMealScreenDestination
import com.joelkanyi.auth.presentation.AuthNavigator
import com.joelkanyi.auth.presentation.destinations.ForgotPasswordScreenDestination
import com.joelkanyi.auth.presentation.destinations.SignInScreenDestination
import com.joelkanyi.auth.presentation.destinations.SignUpScreenDestination
import com.joelkanyi.presentation.favorites.FavoritesNavigator
import com.joelkanyi.presentation.home.HomeNavigator
import com.joelkanyi.presentation.home.destinations.DetailsScreenDestination
import com.joelkanyi.presentation.home.destinations.HomeScreenDestination
import com.joelkanyi.presentation.search.SearchNavigator
import com.joelkanyi.presentation.search.destinations.SearchScreenDestination
import com.kanyideveloper.settings.presentation.settings.SettingsNavigator
import com.kanyideveloper.settings.presentation.settings.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.dynamic.within
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.NavGraphSpec

class CoreFeatureNavigator(
    private val navGraph: NavGraphSpec,
    private val navController: NavController,
) : HomeNavigator,
    SearchNavigator,
    FavoritesNavigator,
    SettingsNavigator,
    AddMealNavigator,
    AuthNavigator {
    override fun openAddMeal() {
        navController.navigate(AddMealScreenDestination within navGraph)
    }

    override fun openHome() {
        navController.navigate(HomeScreenDestination within navGraph)
    }

    override fun openMealDetails(mealId: String?) {
        navController.navigate(DetailsScreenDestination(mealId = mealId) within navGraph)
    }

    override fun switchNavGraphRoot() {
        navController.navigate(
            NavGraphs.home
        ) {
            popUpTo(NavGraphs.auth.route) {
                inclusive = false
            }
        }
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

    override fun onSearchClick() {
        navController.navigate(SearchScreenDestination within navGraph)
    }

    override fun subscribe() {
    }

    override fun openSettings() {
        navController.navigate(SettingsScreenDestination within navGraph)
    }

    override fun logout() {
        navController.navigate(
            NavGraphs.auth
        ) {
            popUpTo(NavGraphs.root(false).route) {
                inclusive = false
            }
        }
    }

    override fun navigateBackToHome() {
        navController.navigate(HomeScreenDestination within navGraph)
        navController.clearBackStack("home")
    }

    override fun openForgotPassword() {
        navController.navigate(ForgotPasswordScreenDestination within navGraph)
    }

    override fun openSignUp() {
        navController.navigate(SignUpScreenDestination within navGraph)
    }

    override fun openSignIn() {
        navController.navigate(SignInScreenDestination within navGraph)
    }
}

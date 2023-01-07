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

import com.kanyideveloper.addmeal.presentation.addmeal.destinations.AddMealScreenDestination
import com.kanyideveloper.addmeal.presentation.addmeal.destinations.NextAddMealScreenDestination
import com.kanyideveloper.favorites.presentation.favorites.presentation.destinations.FavoritesScreenDestination
import com.kanyideveloper.mealplanner.destinations.AllergiesScreenDestination
import com.kanyideveloper.mealplanner.destinations.MealPlannerScreenDestination
import com.kanyideveloper.mealplanner.destinations.MealTypesScreenDestination
import com.kanyideveloper.mealplanner.destinations.NumberOfPeopleScreenDestination
import com.kanyideveloper.presentation.destinations.DetailsScreenDestination
import com.kanyideveloper.presentation.destinations.HomeScreenDestination
import com.kanyideveloper.presentation.destinations.MyMealScreenDestination
import com.kanyideveloper.presentation.destinations.OnlineMealDetailsScreenDestination
import com.kanyideveloper.presentation.destinations.OnlineMealScreenDestination
import com.kanyideveloper.randommeal.destinations.RandomMealScreenDestination
import com.kanyideveloper.search.presentation.search.destinations.SearchScreenDestination
import com.kanyideveloper.settings.presentation.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.dynamic.routedIn
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec

object NavGraphs {

    val home = object : NavGraphSpec {
        override val route = "home"

        override val startRoute = HomeScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            HomeScreenDestination,
            MyMealScreenDestination,
            OnlineMealScreenDestination,
            OnlineMealDetailsScreenDestination,
            DetailsScreenDestination,
            AddMealScreenDestination,
            NextAddMealScreenDestination,
            RandomMealScreenDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val search = object : NavGraphSpec {
        override val route = "search"

        override val startRoute = SearchScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            SearchScreenDestination,
            OnlineMealDetailsScreenDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val favorites = object : NavGraphSpec {
        override val route = "favorites"

        override val startRoute = FavoritesScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            FavoritesScreenDestination,
            OnlineMealDetailsScreenDestination,
            DetailsScreenDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val mealPlanner = object : NavGraphSpec {
        override val route = "meal_planner"

        override val startRoute = MealPlannerScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            MealPlannerScreenDestination,
            AllergiesScreenDestination,
            NumberOfPeopleScreenDestination,
            MealTypesScreenDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val settings = object : NavGraphSpec {
        override val route = "settings"

        override val startRoute = SettingsScreenDestination routedIn this

        override val destinationsByRoute = listOf<DestinationSpec<*>>(
            SettingsScreenDestination
        ).routedIn(this)
            .associateBy { it.route }
    }

    val root = object : NavGraphSpec {
        override val route = "root"
        override val startRoute = home
        override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()
        override val nestedNavGraphs = listOf(
            home,
            search,
            mealPlanner,
            favorites,
            settings
        )
    }
}

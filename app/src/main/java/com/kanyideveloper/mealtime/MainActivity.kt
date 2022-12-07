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
package com.kanyideveloper.mealtime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.systemuicontroller.SystemUiController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.kanyideveloper.compose_ui.theme.MealTimeTheme
import com.kanyideveloper.destinations.HomeScreenDestination
import com.kanyideveloper.favorites.presentation.favorites.destinations.FavoritesScreenDestination
import com.kanyideveloper.mealtime.component.StandardScaffold
import com.kanyideveloper.mealtime.component.navGraph
import com.kanyideveloper.mealtime.navigation.CoreFeatureNavigator
import com.kanyideveloper.mealtime.navigation.NavGraphs
import com.kanyideveloper.search.presentation.search.destinations.SearchScreenDestination
import com.kanyideveloper.settings.presentation.destinations.SettingsScreenDestination
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.rememberNavHostEngine
import com.ramcosta.composedestinations.scope.DestinationScope
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var systemUiController: SystemUiController

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MealTimeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    systemUiController = rememberSystemUiController()

                    val navController = rememberAnimatedNavController()

                    val newBackStackEntry by navController.currentBackStackEntryAsState()

                    val route = newBackStackEntry?.destination?.route

                    StandardScaffold(
                        navController = navController,
                        showBottomBar = route in listOf(
                            "home/${HomeScreenDestination.route}",
                            "search/${SearchScreenDestination.route}",
                            "favorites/${FavoritesScreenDestination.route}",
                            "settings/${SettingsScreenDestination.route}"
                        )
                    ) { innerPadding ->
                        Box(modifier = Modifier.padding(innerPadding)) {
                            AppNavigation(
                                navController = navController,
                                modifier = Modifier
                                    .fillMaxSize(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    DestinationsNavHost(
        engine = rememberNavHostEngine(),
        navController = navController,
        navGraph = NavGraphs.root,
        modifier = modifier,
        dependenciesContainerBuilder = {
            dependency(currentNavigator())
        }
    )
}

fun DestinationScope<*>.currentNavigator(/*openSettings: () -> Unit*/): CoreFeatureNavigator {
    return CoreFeatureNavigator(
        navBackStackEntry.destination.navGraph(),
        navController
    )
}
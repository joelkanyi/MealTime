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
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.joelkanyi.kitchen_timer.presentation.destinations.KitchenTimerScreenDestination
import com.kanyideveloper.compose_ui.theme.MealTimeTheme
import com.kanyideveloper.compose_ui.theme.Theme
import com.kanyideveloper.core.state.SubscriptionStatusUiState
import com.kanyideveloper.core.util.Constants.PURCHASE_ID
import com.kanyideveloper.favorites.presentation.favorites.presentation.destinations.FavoritesScreenDestination
import com.kanyideveloper.mealplanner.destinations.MealPlannerScreenDestination
import com.kanyideveloper.mealtime.component.StandardScaffold
import com.kanyideveloper.mealtime.component.navGraph
import com.kanyideveloper.mealtime.navigation.CoreFeatureNavigator
import com.kanyideveloper.mealtime.navigation.NavGraphs
import com.kanyideveloper.mealtime.navigation.scaleInEnterTransition
import com.kanyideveloper.mealtime.navigation.scaleInPopEnterTransition
import com.kanyideveloper.mealtime.navigation.scaleOutExitTransition
import com.kanyideveloper.mealtime.navigation.scaleOutPopExitTransition
import com.kanyideveloper.presentation.destinations.HomeScreenDestination
import com.kanyideveloper.settings.presentation.destinations.SettingsScreenDestination
import com.qonversion.android.sdk.Qonversion
import com.qonversion.android.sdk.dto.QEntitlement
import com.qonversion.android.sdk.dto.QonversionError
import com.qonversion.android.sdk.listeners.QonversionEntitlementsCallback
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.NestedNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.dependency
import com.ramcosta.composedestinations.scope.DestinationScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {
            setKeepOnScreenCondition(
                condition = {
                    viewModel.subscriptionStatusUiState
                }
            )
        }
        setContent {
            val isLogged = viewModel.user.value != null
            val isSubscribed = viewModel.isSubscribed.collectAsState().value

            val themeValue by viewModel.theme.collectAsState(
                initial = Theme.FOLLOW_SYSTEM.themeValue,
                context = Dispatchers.Main.immediate
            )

            when (isSubscribed) {
                SubscriptionStatusUiState.Loading -> {}
                is SubscriptionStatusUiState.Success -> {
                    MealTimeTheme(
                        theme = themeValue
                    ) {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colorScheme.background
                        ) {
                            val navController = rememberAnimatedNavController()
                            val newBackStackEntry by navController.currentBackStackEntryAsState()
                            val route = newBackStackEntry?.destination?.route

                            val bottomBarItems = if (isSubscribed.isSubscribed) {
                                listOf(
                                    BottomNavItem.Home,
                                    BottomNavItem.KitchenTimer,
                                    BottomNavItem.MealPlanner,
                                    BottomNavItem.Favorites,
                                    BottomNavItem.Settings
                                )
                            } else {
                                listOf(
                                    BottomNavItem.Home,
                                    BottomNavItem.MealPlanner,
                                    BottomNavItem.Favorites,
                                    BottomNavItem.Settings
                                )
                            }

                            StandardScaffold(
                                navController = navController,
                                items = bottomBarItems,
                                isLoggedIn = isLogged,
                                showBottomBar = route in listOf(
                                    "home/${HomeScreenDestination.route}",
                                    "kitchen-timer/${KitchenTimerScreenDestination.route}",
                                    "meal_planner/${MealPlannerScreenDestination.route}",
                                    "favorites/${FavoritesScreenDestination.route}",
                                    "settings/${SettingsScreenDestination.route}"
                                )
                            ) { innerPadding ->
                                Box(modifier = Modifier.padding(innerPadding)) {
                                    AppNavigation(
                                        navController = navController,
                                        isLoggedIn = isLogged,
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        subscribe = {
                                            subscribeUser(onSuccess = {
                                                Qonversion.shared.syncPurchases()
                                                viewModel.updateSubscriptionStatus()
                                            })
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun subscribeUser(onSuccess: () -> Unit) {
        Qonversion.shared.purchase(
            this@MainActivity,
            id = PURCHASE_ID,
            object : QonversionEntitlementsCallback {
                override fun onError(error: QonversionError) {
                    Toast
                        .makeText(
                            this@MainActivity,
                            "Purchase failed: ${error.description}, ${error.additionalMessage}",
                            Toast.LENGTH_LONG
                        )
                        .show()
                }

                override fun onSuccess(entitlements: Map<String, QEntitlement>) {
                    onSuccess()
                    Toast
                        .makeText(
                            this@MainActivity,
                            "Purchase successful",
                            Toast.LENGTH_LONG
                        )
                        .show()
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    subscribe: () -> Unit,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    isLoggedIn: Boolean,
) {
    val navHostEngine = rememberAnimatedNavHostEngine(
        navHostContentAlignment = Alignment.TopCenter,
        rootDefaultAnimations = RootNavGraphDefaultAnimations.ACCOMPANIST_FADING, // default `rootDefaultAnimations` means no animations
        defaultAnimationsForNestedNavGraph = mapOf(
            NavGraphs.home to NestedNavGraphDefaultAnimations(
                enterTransition = {
                    scaleInEnterTransition()
                },
                exitTransition = {
                    scaleOutExitTransition()
                },
                popEnterTransition = {
                    scaleInPopEnterTransition()
                },
                popExitTransition = {
                    scaleOutPopExitTransition()
                }
            ),
            NavGraphs.search to NestedNavGraphDefaultAnimations(
                enterTransition = {
                    scaleInEnterTransition()
                },
                exitTransition = {
                    scaleOutExitTransition()
                },
                popEnterTransition = {
                    scaleInPopEnterTransition()
                },
                popExitTransition = {
                    scaleOutPopExitTransition()
                }
            ),
            NavGraphs.favorites to NestedNavGraphDefaultAnimations(
                enterTransition = {
                    scaleInEnterTransition()
                },
                exitTransition = {
                    scaleOutExitTransition()
                },
                popEnterTransition = {
                    scaleInPopEnterTransition()
                },
                popExitTransition = {
                    scaleOutPopExitTransition()
                }
            ),
            NavGraphs.settings to NestedNavGraphDefaultAnimations(
                enterTransition = {
                    scaleInEnterTransition()
                },
                exitTransition = {
                    scaleOutExitTransition()
                },
                popEnterTransition = {
                    scaleInPopEnterTransition()
                },
                popExitTransition = {
                    scaleOutPopExitTransition()
                }
            ),
            NavGraphs.mealPlanner to NestedNavGraphDefaultAnimations(
                enterTransition = {
                    scaleInEnterTransition()
                },
                exitTransition = {
                    scaleOutExitTransition()
                },
                popEnterTransition = {
                    scaleInPopEnterTransition()
                },
                popExitTransition = {
                    scaleOutPopExitTransition()
                }
            ),
            NavGraphs.auth to NestedNavGraphDefaultAnimations(
                enterTransition = {
                    scaleInEnterTransition()
                },
                exitTransition = {
                    scaleOutExitTransition()
                },
                popEnterTransition = {
                    scaleInPopEnterTransition()
                },
                popExitTransition = {
                    scaleOutPopExitTransition()
                }
            )
        )
    )

    DestinationsNavHost(
        engine = navHostEngine,
        navController = navController,
        navGraph = NavGraphs.root(isLoggedIn = isLoggedIn),
        modifier = modifier,
        dependenciesContainerBuilder = {
            dependency(
                currentNavigator(
                    subscribe = subscribe,
                    isLoggedIn = isLoggedIn,
                )
            )
        }
    )
}

fun DestinationScope<*>.currentNavigator(
    isLoggedIn: Boolean,
    subscribe: () -> Unit,
): CoreFeatureNavigator {
    return CoreFeatureNavigator(
        navGraph = navBackStackEntry.destination.navGraph(isLoggedIn),
        navController = navController,
        subscribe = subscribe
    )
}

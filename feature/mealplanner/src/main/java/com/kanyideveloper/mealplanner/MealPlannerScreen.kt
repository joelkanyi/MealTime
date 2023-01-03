/*
 * Copyright 2023 Joel Kanyi.
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
package com.kanyideveloper.mealplanner

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.core.components.EmptyStateComponent
import com.kanyideveloper.core.util.UiEvents
import com.kanyideveloper.mealplanner.model.Day
import com.kanyideveloper.mealplanner.model.MealType
import com.kanyideveloper.mealplanner.presentation.components.DayItemCard
import com.kanyideveloper.mealplanner.presentation.components.MealPlanItem
import com.kanyideveloper.mealplanner.presentation.components.SelectMealDialog
import com.kanyideveloper.mealtime.core.R
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.collectLatest

interface MealPlannerNavigator {
    fun popBackStack()
    fun openAllergiesScreen()
    fun openNoOfPeopleScreen()
    fun openMealTypesScreen()
    fun openMealPlanner()
}

@Destination
@Composable
fun MealPlannerScreen(
    navigator: MealPlannerNavigator,
    viewModel: MealPlannerViewModel = hiltViewModel()
) {
    val hasMealPlan = viewModel.hasMealPlan
    val shouldShowMealsDialog = viewModel.shouldShowMealsDialog.value

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(key1 = true) {
        viewModel.eventsFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
                }
            }
        }
    }

    if (shouldShowMealsDialog) {
        SelectMealDialog(
            mealType = viewModel.mealType.value,
            meals = viewModel.searchMeals.value.meals,
            currentSearchString = viewModel.searchString.value,
            sources = listOf("Online", "My Meals", "My Favorites"),
            searchOptions = listOf("Name", "Ingredient", "Category"),
            onDismiss = {
                viewModel.setShouldShowMealsDialogState(!viewModel.shouldShowMealsDialog.value)
            },
            onClickAdd = { meal ->
                viewModel.insertMealToPlan(meal = meal)
            },
            onSearchValueChange = {
                viewModel.setSearchStringState(it)
            },
            onSearchClicked = {
                viewModel.searchMeal()
            },
            onSearchByChange = { searchBy ->
                viewModel.setSearchByState(searchBy)
            },
            onSourceChange = { source ->
                viewModel.setSourceState(source)
            }
        )
    }

    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigate = {
                navigator.popBackStack()
            },
            title = {
                Text(text = "Meal Planner", fontSize = 18.sp)
            },
            showBackArrow = false,
            navActions = {
            }
        )

        MealPlannerScreenContent(
            hasMealPlan = hasMealPlan,
            navigator = navigator,
            days = viewModel.days,
            mealsAndTheirTypes = viewModel.mealTypes,
            onClickAdd = { mealType ->
                viewModel.setMealTypeState(mealType)
                viewModel.setShouldShowMealsDialogState(!viewModel.shouldShowMealsDialog.value)
            }
        )
    }
}

@Composable
private fun MealPlannerScreenContent(
    hasMealPlan: Boolean,
    navigator: MealPlannerNavigator,
    onClickAdd: (String) -> Unit,
    days: List<Day>,
    mealsAndTheirTypes: List<MealType>
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (!hasMealPlan) {
            EmptyStateComponent(
                anim = R.raw.women_thinking,
                message = "Looks like you haven't created a meal plan yet",
                content = {
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(onClick = {
                        navigator.openAllergiesScreen()
                    }) {
                        Text(text = "Get Started")
                    }
                }
            )
        }

        if (hasMealPlan) {
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
                item {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        items(days) { day ->
                            DayItemCard(day = day.name, date = day.date)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(mealsAndTheirTypes) { type ->
                    MealPlanItem(
                        mealType = type,
                        onClickAdd = onClickAdd
                    )
                }
            }
        }
    }
}

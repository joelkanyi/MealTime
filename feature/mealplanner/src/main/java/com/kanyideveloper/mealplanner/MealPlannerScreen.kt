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

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joelkanyi.horizontalcalendar.HorizontalCalendarView
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.core.components.EmptyStateComponent
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.util.UiEvents
import com.kanyideveloper.mealplanner.model.MealPlan
import com.kanyideveloper.mealplanner.presentation.components.MealPlanItem
import com.kanyideveloper.mealplanner.presentation.components.SelectMealDialog
import com.kanyideveloper.mealtime.core.R
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.collectLatest

interface MealPlannerNavigator {
    fun popBackStack()
    fun openAllergiesScreen(editMealPlanPreference: Boolean = false)
    fun openNoOfPeopleScreen(allergies: String, editMealPlanPreference: Boolean = false)
    fun openMealTypesScreen(
        allergies: String,
        noOfPeople: String,
        editMealPlanPreference: Boolean = false
    )

    fun openMealPlanner()
    fun openMealDetails(meal: Meal)
    fun openOnlineMealDetails(mealId: String)

    fun navigateToSettings()
}

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun MealPlannerScreen(
    navigator: MealPlannerNavigator,
    viewModel: MealPlannerViewModel = hiltViewModel()
) {
    val hasMealPlan = viewModel.hasMealPlanPrefs.value
    val shouldShowMealsDialog = viewModel.shouldShowMealsDialog.value
    val planMeals = viewModel.getPlanMeals().observeAsState().value

    val snackbarHostState = remember { SnackbarHostState() }

    val meal = viewModel.singleMeal.observeAsState().value?.observeAsState()?.value

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventsFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }
                else -> {}
            }
        }
    }

    if (shouldShowMealsDialog) {
        SelectMealDialog(
            mealType = viewModel.mealType.value,
            meals = viewModel.searchMeals.value.meals?.observeAsState()?.value ?: emptyList(),
            currentSearchString = viewModel.searchString.value,
            sources = listOf("Online", "My Meals", "My Favorites"),
            searchOptions = listOf("Name", "Ingredient", "Category"),
            currentSource = viewModel.source.value,
            isLoading = viewModel.searchMeals.value.isLoading,
            error = viewModel.searchMeals.value.error,
            onDismiss = {
                viewModel.setShouldShowMealsDialogState(!viewModel.shouldShowMealsDialog.value)
            },
            onClickAdd = { meall, type ->

                val allergies = viewModel.allergies.value

                val allergy = allergies.find { it.lowercase() in meall.name.lowercase() }

                if (allergy != null) {
                    Toast.makeText(
                        context,
                        "You indicated that you are allergic to $allergy, if not you can go to settings and make changes",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@SelectMealDialog
                }

                viewModel.insertMealToPlan(
                    meal = meall,
                    mealTypePlan = type,
                    date = viewModel.selectedDate.value
                )
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
                if (source == "My Meals" || source == "My Favorites") {
                    viewModel.searchMeal()
                }
            },
            onMealClick = { _, _, _ -> }
        )
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
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
        }
    ) { paddingValues ->
        MealPlannerScreenContent(
            modifier = Modifier.padding(paddingValues),
            hasMealPlan = hasMealPlan,
            navigator = navigator,
            mealTypes = viewModel.types.value,
            mealsAndTheirTypes = planMeals ?: emptyList(),
            onClickAdd = { mealType ->
                viewModel.setMealTypeState(mealType)
                viewModel.setShouldShowMealsDialogState(!viewModel.shouldShowMealsDialog.value)
            },
            onClickDay = { fullDate ->
                viewModel.setSelectedDateState(fullDate)
                viewModel.getPlanMeals(filterDay = fullDate)
            },
            onRemoveClick = { id ->
                id?.let { viewModel.removeMealFromPlan(id = it) }
            },
            onMealClick = { localMealId, onlineMealId, isOnline ->
                if (isOnline) {
                    onlineMealId?.let { navigator.openOnlineMealDetails(mealId = it) }
                } else {
                    if (localMealId != null) {
                        viewModel.getASingleMeal(id = localMealId)

                        if (meal != null) {
                            navigator.openMealDetails(meal = meal)
                        }
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("FrequentlyChangedStateReadInComposition")
@Composable
private fun MealPlannerScreenContent(
    modifier: Modifier = Modifier,
    hasMealPlan: Boolean,
    navigator: MealPlannerNavigator,
    onClickAdd: (String) -> Unit,
    mealsAndTheirTypes: List<MealPlan>,
    mealTypes: List<String>,
    onClickDay: (String) -> Unit,
    onRemoveClick: (Int?) -> Unit,
    onMealClick: (Int?, String?, Boolean) -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {
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
                    HorizontalCalendarView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItemPlacement(),
                        onDayClick = { day ->
                            onClickDay(day.fullDate)
                        },
                        selectedCardColor = MaterialTheme.colorScheme.primary,
                        unSelectedCardColor = MaterialTheme.colorScheme.surfaceVariant,
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        unSelectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(mealTypes) { type ->
                    MealPlanItem(
                        onClickAdd = onClickAdd,
                        type = type,
                        onRemoveClick = onRemoveClick,
                        onMealClick = onMealClick,
                        meals = mealsAndTheirTypes.filter { it.mealTypeName == type }
                            .map { it.mapMealPlanToMeals() }.flatten()
                    )
                }
            }
        }
    }
}

fun MealPlan.mapMealPlanToMeals(): List<Meal> {
    return this.meals.map { meal ->
        Meal(
            name = meal.name,
            imageUrl = meal.imageUrl,
            cookingTime = meal.cookingTime,
            servingPeople = meal.servingPeople,
            category = meal.category,
            cookingDifficulty = meal.cookingDifficulty,
            ingredients = meal.ingredients,
            cookingDirections = meal.cookingDirections,
            isFavorite = meal.isFavorite,
            onlineMealId = meal.onlineMealId,
            localMealId = meal.localMealId,
            mealPlanId = this.id
        )
    }
}

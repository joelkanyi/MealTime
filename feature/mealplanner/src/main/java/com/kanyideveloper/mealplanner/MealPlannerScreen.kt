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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.core.components.EmptyStateComponent
import com.kanyideveloper.core.util.UiEvents
import com.kanyideveloper.mealplanner.model.Day
import com.kanyideveloper.mealplanner.model.MealPlan
import com.kanyideveloper.mealplanner.presentation.components.DayItemCard
import com.kanyideveloper.mealplanner.presentation.components.MealPlanItem
import com.kanyideveloper.mealplanner.presentation.components.SelectMealDialog
import com.kanyideveloper.mealtime.core.R
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.collectLatest

interface MealPlannerNavigator {
    fun popBackStack()
    fun openAllergiesScreen()
    fun openNoOfPeopleScreen(allergies: String)
    fun openMealTypesScreen(allergies: String, noOfPeople: String)
    fun openMealPlanner()
}

@Destination
@Composable
fun MealPlannerScreen(
    navigator: MealPlannerNavigator,
    viewModel: MealPlannerViewModel = hiltViewModel()
) {
    val hasMealPlan = viewModel.hasMealPlanPrefs.value
    val shouldShowMealsDialog = viewModel.shouldShowMealsDialog.value
    val planMeals = viewModel.getPlanMeals().observeAsState().value

    val scaffoldState = rememberScaffoldState()

    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventsFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    scaffoldState.snackbarHostState.showSnackbar(message = event.message)
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
            onClickAdd = { meal, type ->
                viewModel.insertMealToPlan(
                    meal = meal,
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
            days = viewModel.days.collectAsLazyPagingItems(),
            mealTypes = viewModel.types.value,
            mealsAndTheirTypes = planMeals ?: emptyList(),
            onClickAdd = { mealType ->
                viewModel.setMealTypeState(mealType)
                viewModel.setShouldShowMealsDialogState(!viewModel.shouldShowMealsDialog.value)
            },
            isDaySelected = { fullDate ->
                viewModel.selectedDate.value == fullDate
            },
            onClickDay = { fullDate ->
                viewModel.setSelectedDateState(fullDate)
                viewModel.getPlanMeals(filterDay = viewModel.selectedDate.value)
            },
            onRemoveClick = { onlineMealId, localMealId, mealType, isOnline ->
                /*if (isOnline){
                    viewModel.removeOnlineMealFromPlan(onlineMealId = onlineMealId, mealType = mealType)
                }else{
                    viewModel.removeLocalMealFromPlan(localMealId = localMealId, mealType = mealType)
                }*/
                Toast.makeText(context, "Feature in development", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@SuppressLint("FrequentlyChangedStateReadInComposition")
@Composable
private fun MealPlannerScreenContent(
    hasMealPlan: Boolean,
    navigator: MealPlannerNavigator,
    onClickAdd: (String) -> Unit,
    days: LazyPagingItems<Day>,
    mealsAndTheirTypes: List<MealPlan>,
    mealTypes: List<String>,
    isDaySelected: (String) -> Boolean,
    onClickDay: (String) -> Unit,
    onRemoveClick: (Int?, String?, String, Boolean) -> Unit
) {
    val daysLazyRowState = rememberLazyListState()

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
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val monthAndYear = remember {
                            mutableStateOf("")
                        }
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = monthAndYear.value,
                            style = MaterialTheme.typography.titleLarge,
                            textAlign = TextAlign.End
                        )

                        LazyRow(
                            state = daysLazyRowState,
                            horizontalArrangement = Arrangement.spacedBy(2.dp),
                            content = {
                                itemsIndexed(days) { index, day ->
                                    if (daysLazyRowState.firstVisibleItemIndex == index) {
                                        monthAndYear.value = "${day?.displayMonth}, ${day?.year}"
                                    }
                                    DayItemCard(
                                        day = day!!.name,
                                        date = day.displayDate,
                                        fullDate = day.fullDate,
                                        isSelected = isDaySelected,
                                        onClick = onClickDay
                                    )
                                }
                            }
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                }

                items(mealTypes) { type ->
                    MealPlanItem(
                        meals = mealsAndTheirTypes.filter { it.mealTypeName == type }
                            .flatMap { it.meals },
                        onClickAdd = onClickAdd,
                        type = type,
                        onRemoveClick = onRemoveClick
                    )
                }
            }
        }
    }
}

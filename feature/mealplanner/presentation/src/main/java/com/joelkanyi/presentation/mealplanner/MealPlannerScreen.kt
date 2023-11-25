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
package com.joelkanyi.presentation.mealplanner

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.joelkanyi.domain.entity.MealPlan
import com.joelkanyi.horizontalcalendar.HorizontalCalendarView
import com.joelkanyi.presentation.mealplanner.components.MealPlanItem
import com.joelkanyi.presentation.mealplanner.components.SelectMealBottomSheet
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.core.components.EmptyStateComponent
import com.kanyideveloper.core.components.SwipeRefreshComponent
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.util.UiEvents
import com.kanyideveloper.core.util.calendarLocalDates
import com.kanyideveloper.core.util.prettyPrintedMonthAndYear
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun MealPlannerScreen(
    navigator: MealPlannerNavigator,
    viewModel: MealPlannerViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val shouldShowMealsDialog = viewModel.shouldShowMealsDialog.value
    val planMealsUiState = viewModel.mealsInPlanState.value
    val mealPlanPrefs = viewModel.mealPlanPrefs.collectAsState().value
    val meal = viewModel.singleMeal.observeAsState().value?.observeAsState()?.value
    val context = LocalContext.current
    val bottomSheetState = rememberModalBottomSheetState()
    val coroutineScope = rememberCoroutineScope()
    val calendarPagerState = rememberLazyListState()
    val selectedDay = viewModel.selectedDay.collectAsState().value


    LaunchedEffect(key1 = true, block = {
        calendarPagerState.animateScrollToItem(
            index = calendarLocalDates().indexOf(selectedDay),
            scrollOffset = 0,
        )
        viewModel.getPlanMeals()

        viewModel.eventsFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    snackbarHostState.showSnackbar(message = event.message)
                }

                else -> {}
            }
        }
    })

    if (shouldShowMealsDialog) {
        SelectMealBottomSheet(
            bottomSheetState = bottomSheetState,
            mealType = viewModel.mealType.value,
            meals = viewModel.searchMeals.value.meals,
            currentSearchString = viewModel.searchString.value,
            sources = listOf("Online", "My Meals", "My Favorites"),
            searchOptions = listOf("Name", "Ingredient", "Category"),
            currentSource = viewModel.source.value,
            isLoading = viewModel.searchMeals.value.isLoading,
            error = viewModel.searchMeals.value.error,
            onDismissRequest = {
                coroutineScope.launch {
                    viewModel.trackUserEvent("Dismiss select meal dialog")
                    viewModel.setShouldShowMealsDialogState(
                        !viewModel.shouldShowMealsDialog.value
                    )
                    bottomSheetState.hide()
                }
            },
            onClickAdd = { meall, type ->
                viewModel.trackUserEvent("Add meal to plan - ${meall.name}")

                val allergies = viewModel.allergies.value

                val allergy = allergies.find { it.lowercase() in meall.name.lowercase() }

                if (allergy != null) {
                    Toast.makeText(
                        context,
                        "You indicated that you are allergic to $allergy, if not you can go to settings and make changes",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@SelectMealBottomSheet
                }

                viewModel.insertMealToPlan(
                    meal = meall,
                    mealTypePlan = type,
                    date = viewModel.selectedDate.value,
                )
            },
            onSearchValueChange = {
                viewModel.setSearchStringState(it)
            },
            onSearchClicked = {
                viewModel.trackUserEvent("Meal Planner Search meal")
                viewModel.searchMeal()
            },
            onSearchByChange = { searchBy ->
                viewModel.trackUserEvent("Meal Planner Search by $searchBy")
                viewModel.setSearchByState(searchBy)
            },
            onSourceChange = { source ->
                viewModel.trackUserEvent("Meal Planner meals Source $source")
                viewModel.setSourceState(source)
                if (source == "My Meals" || source == "My Favorites") {
                    viewModel.searchMeal()
                }
            },
            onMealClick = { _, _, _ -> }
        )
    }

    MealPlannerScreenContent(
        mealPlanPrefs = mealPlanPrefs,
        calendarPagerState = calendarPagerState,
        mealsAndTheirTypes = planMealsUiState.meals,
        selectedDay = selectedDay,
        planMealsUiState = planMealsUiState,
        snackbarHostState = snackbarHostState,
        onClickAdd = { mealType ->
            viewModel.trackUserEvent("Show select meal dialog")
            viewModel.setMealTypeState(mealType)
            viewModel.setShouldShowMealsDialogState(
                !viewModel.shouldShowMealsDialog.value
            )
        },
        onClickDay = { fullDate ->
            viewModel.trackUserEvent("Meal Planner select date $fullDate")
            viewModel.setSelectedDay(fullDate)
            viewModel.getPlanMeals(filterDay = fullDate.toString())
        },
        onRemoveClick = { id ->
            viewModel.trackUserEvent("Remove meal from plan")
            id?.let {
                viewModel.removeMealFromPlan(id = it)
            }
        },
        onMealClick = { localMealId, onlineMealId, isOnline ->
            viewModel.trackUserEvent("Meal Planner meal click")
            if (isOnline) {
                onlineMealId?.let { navigator.openMealDetails(mealId = it) }
            } else {
                if (localMealId != null) {
                    viewModel.getASingleMeal(id = localMealId)

                    if (meal != null) {
                        navigator.openMealDetails(mealId = meal.mealId)
                    }
                }
            }
        },
        onRefreshData = {
            viewModel.trackUserEvent("Refresh Meal Planner")
            viewModel.getPlanMeals()
        },
        onClickNavigateBack = {
            viewModel.trackUserEvent("Navigate back from Meal Planner")
            navigator.popBackStack()
        },
        onClickGetStarted = {
            viewModel.trackUserEvent("No meal plan -Navigate to allergies screen")
            navigator.openAllergiesScreen()
        }
    )

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@SuppressLint("FrequentlyChangedStateReadInComposition")
@Composable
private fun MealPlannerScreenContent(
    calendarPagerState: LazyListState,
    mealPlanPrefs: MealPlanPrefsState,
    onClickAdd: (String) -> Unit,
    planMealsUiState: MealsInPlanState,
    mealsAndTheirTypes: List<MealPlan>,
    onClickDay: (LocalDate) -> Unit,
    onRemoveClick: (String?) -> Unit,
    onMealClick: (String?, String?, Boolean) -> Unit,
    snackbarHostState: SnackbarHostState,
    onRefreshData: () -> Unit,
    onClickNavigateBack: () -> Unit,
    onClickGetStarted: () -> Unit,
    selectedDay: LocalDate,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (mealPlanPrefs) {
            MealPlanPrefsState.Loading -> {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                )
            }

            is MealPlanPrefsState.Success -> {
                val hasMealPlan = mealPlanPrefs.data != null
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        StandardToolbar(
                            navigate = onClickNavigateBack,
                            title = {
                                Text(text = "Meal Planner", fontSize = 18.sp)
                            },
                            showBackArrow = false,
                            navActions = {
                            }
                        )
                    }
                ) { paddingValues ->
                    SwipeRefreshComponent(
                        isRefreshingState = planMealsUiState.isLoading,
                        onRefreshData = onRefreshData,
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(paddingValues)
                                .fillMaxSize()
                        ) {
                            if (hasMealPlan) {
                                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
                                    item {
                                        Text(
                                            modifier = Modifier.fillMaxWidth(),
                                            text = selectedDay.prettyPrintedMonthAndYear(),
                                            style = MaterialTheme.typography.labelLarge.copy(
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                textAlign = TextAlign.End,
                                            ),
                                        )
                                    }
                                    item {
                                        LazyRow(
                                            state = calendarPagerState,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        ) {
                                            items(calendarLocalDates()) { date ->
                                                Box(
                                                    modifier = Modifier
                                                        .size(64.dp)
                                                        .clipToBounds()
                                                        .background(
                                                            color = if (date == selectedDay) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                                                            shape = RoundedCornerShape(4.dp),
                                                        )
                                                        .clickable {
                                                            onClickDay(date)
                                                        },
                                                    contentAlignment = Alignment.Center,
                                                ) {
                                                    Column {
                                                        Text(
                                                            text = date.dayOfWeek.name.substring(
                                                                0,
                                                                3
                                                            ),
                                                            style = MaterialTheme.typography.labelMedium.copy(
                                                                color = if (date == selectedDay) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                                            ),
                                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                                        )
                                                        Text(
                                                            text = date.dayOfMonth.toString(),
                                                            style = MaterialTheme.typography.labelMedium.copy(
                                                                color = if (date == selectedDay) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                                            ),
                                                            modifier = Modifier.align(Alignment.CenterHorizontally),
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    item {
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }

                                    items(mealPlanPrefs.data?.dishTypes ?: emptyList()) { type ->
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
                            } else {
                                EmptyStateComponent(
                                    // anim = R.raw.women_thinking,
                                    message = "Looks like you haven't created a meal plan yet",
                                    content = {
                                        Spacer(modifier = Modifier.height(32.dp))
                                        Button(
                                            onClick = onClickGetStarted
                                        ) {
                                            Text(text = "Get Started")
                                        }
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

fun MealPlan.mapMealPlanToMeals(): List<Meal> {
    return this.meals.map { meal ->
        Meal(
            name = meal.name,
            imageUrl = meal.imageUrl,
            mealId = meal.mealId,
            category = meal.category
        )
    }
}

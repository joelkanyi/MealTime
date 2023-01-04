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

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.model.MealPlanPreference
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.core.util.UiEvents
import com.kanyideveloper.mealplanner.domain.repository.MealPlannerRepository
import com.kanyideveloper.mealplanner.model.Day
import com.kanyideveloper.mealplanner.presentation.state.DailyMealsState
import com.kanyideveloper.mealplanner.presentation.state.SearchMealState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    private val mealPlannerRepository: MealPlannerRepository
) : ViewModel() {

    private val _hasMealPlanPrefs = mutableStateOf(false)
    val hasMealPlanPrefs: State<Boolean> = _hasMealPlanPrefs

    init {
        viewModelScope.launch {
            mealPlannerRepository.hasMealPlanPref.collectLatest { result ->
                _hasMealPlanPrefs.value = result?.numberOfPeople != "0"
            }
        }
    }

    val days = listOf(
        Day(name = "Mon", date = "02"),
        Day(name = "Tue", date = "03"),
        Day(name = "Wed", date = "04"),
        Day(name = "Thur", date = "05"),
        Day(name = "Fri", date = "06"),
        Day(name = "Sat", date = "07"),
        Day(name = "Sun", date = "08")
    )

    private val _mealTypes = mutableStateOf(DailyMealsState())
    val mealTypes: State<DailyMealsState> = _mealTypes

    private val _mealType = mutableStateOf("")
    val mealType: State<String> = _mealType
    fun setMealTypeState(value: String) {
        _mealType.value = value
    }

    private val _searchString = mutableStateOf("")
    val searchString: State<String> = _searchString
    fun setSearchStringState(value: String) {
        _searchString.value = value
    }

    private val _source = mutableStateOf("")
    private val source: State<String> = _source
    fun setSourceState(value: String) {
        _source.value = value
    }

    private val _searchBy = mutableStateOf("")
    private val searchBy: State<String> = _searchBy
    fun setSearchByState(value: String) {
        _searchBy.value = value
    }

    private val _shouldShowMealsDialog = mutableStateOf(false)
    val shouldShowMealsDialog: State<Boolean> = _shouldShowMealsDialog
    fun setShouldShowMealsDialogState(value: Boolean) {
        _shouldShowMealsDialog.value = value
    }

    fun insertMealToPlan(meal: Meal) {
        viewModelScope.launch {
            mealPlannerRepository.saveMealToPlan(meal = meal)
        }
    }

    private val _searchMeals = mutableStateOf(SearchMealState())
    val searchMeals: State<SearchMealState> = _searchMeals

    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow

    fun searchMeal() {
        _searchMeals.value = searchMeals.value.copy(
            isLoading = true
        )

        viewModelScope.launch {
            when (
                val result = mealPlannerRepository.searchMeal(
                    source = source.value,
                    searchBy = searchBy.value
                )
            ) {
                is Resource.Error -> {
                    eventsFlow.emit(
                        UiEvents.SnackbarEvent(
                            message = result.message ?: "Unknown Error Occurred"
                        )
                    )
                    _searchMeals.value = searchMeals.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown Error Occurred"
                    )
                }
                is Resource.Success -> {
                    _searchMeals.value = searchMeals.value.copy(
                        isLoading = false,
                        meals = result.data ?: emptyList()
                    )
                }
                else -> {
                    searchMeals
                }
            }
        }
    }
}

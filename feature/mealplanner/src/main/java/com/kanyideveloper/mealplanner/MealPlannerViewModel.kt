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
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.core.util.UiEvents
import com.kanyideveloper.core.util.getTodaysDate
import com.kanyideveloper.mealplanner.data.paging.DayPagingSource
import com.kanyideveloper.mealplanner.domain.repository.MealPlannerRepository
import com.kanyideveloper.mealplanner.model.MealPlan
import com.kanyideveloper.mealplanner.presentation.state.SearchMealState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    private val mealPlannerRepository: MealPlannerRepository
) : ViewModel() {

    private val _types = mutableStateOf<List<String>>(emptyList())
    val types: State<List<String>> = _types

    private val _hasMealPlanPrefs = mutableStateOf(false)
    val hasMealPlanPrefs: State<Boolean> = _hasMealPlanPrefs

    private val _selectedDate = mutableStateOf(getTodaysDate())
    val selectedDate: State<String> = _selectedDate
    fun setSelectedDateState(value: String) {
        _selectedDate.value = value
    }

    fun getPlanMeals(filterDay: String = selectedDate.value): LiveData<List<MealPlan>> {
        return mealPlannerRepository.getMealsInMyPlan(filterDay = filterDay)
    }

    init {
        viewModelScope.launch {
            mealPlannerRepository.hasMealPlanPref.collectLatest { result ->
                _hasMealPlanPrefs.value = result?.numberOfPeople != "0"
            }
        }

        if (hasMealPlanPrefs.value) {
            getMealsTypes()
        }
    }

    private fun getMealsTypes() {
        viewModelScope.launch {
            mealPlannerRepository.hasMealPlanPref.collectLatest { result ->
                _types.value = result?.dishTypes ?: emptyList()
            }
        }
    }

    fun deleteAMealFromPlan(id: Int) {
        viewModelScope.launch {
            mealPlannerRepository.deleteAMealFromPlan(id = id)
        }
    }

    val days = Pager(
        config = PagingConfig(enablePlaceholders = false, pageSize = 10),
        pagingSourceFactory = {
            DayPagingSource()
        }
    ).flow

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

    fun insertMealToPlan(meal: Meal, mealTypePlan: String, date: String) {
        viewModelScope.launch {
            val existingMeals =
                mealPlannerRepository.getExistingMeals(mealType = mealTypePlan, date = date)
                    .toMutableList()
            existingMeals.add(element = meal)

            val newMealsList = existingMeals.toList()

            val plan = MealPlan(
                mealTypeName = mealTypePlan,
                date = selectedDate.value,
                meals = newMealsList
            )
            mealPlannerRepository.saveMealToPlan(mealPlan = plan)
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
                    searchBy = searchBy.value,
                    searchString = searchString.value
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

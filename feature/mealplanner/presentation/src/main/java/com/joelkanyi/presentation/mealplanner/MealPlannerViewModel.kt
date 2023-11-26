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

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joelkanyi.analytics.domain.usecase.TrackUserEventUseCase
import com.joelkanyi.common.model.Meal
import com.joelkanyi.common.model.MealDetails
import com.joelkanyi.common.util.Resource
import com.joelkanyi.common.util.UiEvents
import com.joelkanyi.common.util.getTodaysDate
import com.joelkanyi.domain.entity.MealPlan
import com.joelkanyi.domain.usecase.AddMealToMealPlanUseCase
import com.joelkanyi.domain.usecase.UserMealPlannerPrefsUseCase
import com.joelkanyi.domain.usecase.GetMealsInMealPlanUseCase
import com.joelkanyi.domain.usecase.RemoveMealFromMealPlanUseCase
import com.joelkanyi.domain.usecase.SearchMealsUseCase
import com.joelkanyi.settings.domain.model.MealPlanPreference
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject

@HiltViewModel
class MealPlannerViewModel @Inject constructor(
    private val trackUserEventUseCase: TrackUserEventUseCase,
    private val getMealsInMealPlanUseCase: GetMealsInMealPlanUseCase,
    private val removeMealFromMealPlanUseCase: RemoveMealFromMealPlanUseCase,
    private val addMealToMealPlanUseCase: AddMealToMealPlanUseCase,
    private val userMealPlannerPrefsUseCase: UserMealPlannerPrefsUseCase,
    private val searchMealsUseCase: SearchMealsUseCase,
) : ViewModel() {
    fun trackUserEvent(name: String) = trackUserEventUseCase(name)

    private val _selectedDay = MutableStateFlow(
        Clock.System.now().toLocalDateTime(
            TimeZone.currentSystemDefault(),
        ).date,
    )
    val selectedDay = _selectedDay.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Clock.System.now().toLocalDateTime(
            TimeZone.currentSystemDefault(),
        ).date,
    )

    fun setSelectedDay(date: kotlinx.datetime.LocalDate) {
        _selectedDay.value = date
    }

    private val _allergies = mutableStateOf<List<String>>(emptyList())
    val allergies: State<List<String>> = _allergies

    val mealPlanPrefs = userMealPlannerPrefsUseCase()
        .map {
            MealPlanPrefsState.Success(it)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = MealPlanPrefsState.Loading
        )

    private val _selectedDate = mutableStateOf(getTodaysDate())
    val selectedDate: State<String> = _selectedDate
    fun setSelectedDateState(value: String) {
        _selectedDate.value = value
    }

    private val _mealsInPlanState = mutableStateOf(MealsInPlanState())
    val mealsInPlanState: State<MealsInPlanState> = _mealsInPlanState

    fun getPlanMeals(filterDay: String = selectedDate.value) {
        viewModelScope.launch {
            getMealsInMealPlanUseCase(
                filterDay = filterDay,
            )
        }
    }

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
    val source: State<String> = _source
    fun setSourceState(value: String) {
        _source.value = value
        _searchMeals.value = searchMeals.value.copy(
            isLoading = false,
            error = null,
            meals = emptyList()
        )
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
            addMealToMealPlanUseCase(
                mealTypeName = mealTypePlan,
                mealName = meal.name,
                mealImageUrl = meal.imageUrl,
                mealId = meal.mealId,
                mealCategory = meal.category,
                date = date
            )
        }
    }

    private val _searchMeals = mutableStateOf(SearchMealState())
    val searchMeals: State<SearchMealState> = _searchMeals

    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow

    fun searchMeal() {
        _searchMeals.value = searchMeals.value.copy(
            isLoading = true,
            meals = emptyList(),
            error = null
        )

        viewModelScope.launch {
            when (
                val result = searchMealsUseCase(
                    searchOption = source.value,
                    searchParam = searchString.value.trim(),
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
                        meals = result.data ?: emptyList(),
                    )
                }

                else -> {
                    searchMeals
                }
            }
        }
    }

    fun removeMealFromPlan(id: String) {
        viewModelScope.launch {
            removeMealFromMealPlanUseCase(id)
        }
    }

    private val _singleMeal = MutableLiveData<LiveData<MealDetails?>>()
    val singleMeal: LiveData<LiveData<MealDetails?>> = _singleMeal
    fun getASingleMeal(id: String) {
        //  _singleMeal.value = mealsRepository.getMealDetails(id = id)
    }
}

data class MealsInPlanState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val meals: List<MealPlan> = emptyList()
)

data class SearchMealState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val meals: List<Meal> = emptyList()
)

sealed class MealPlanPrefsState {
    object Loading : MealPlanPrefsState()
    data class Success(val data: MealPlanPreference?) : MealPlanPrefsState()
}
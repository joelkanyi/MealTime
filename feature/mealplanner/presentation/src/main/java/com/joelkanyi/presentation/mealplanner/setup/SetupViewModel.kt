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
package com.joelkanyi.presentation.mealplanner.setup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.joelkanyi.common.util.Resource
import com.joelkanyi.common.util.UiEvents
import com.joelkanyi.domain.usecase.GetAllIngredientsUseCase
import com.joelkanyi.domain.usecase.SaveAllergiesUseCase
import com.joelkanyi.domain.usecase.SaveDishTypesUseCase
import com.joelkanyi.domain.usecase.SaveNumberOfPeopleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val trackUserEventUseCase: com.joelkanyi.analytics.domain.usecase.TrackUserEventUseCase,
    private val getAllIngredientsUseCase: GetAllIngredientsUseCase,
    private val saveAllergiesUseCase: SaveAllergiesUseCase,
    private val saveNumberOfPeopleUseCase: SaveNumberOfPeopleUseCase,
    private val saveDishTypesUseCase: SaveDishTypesUseCase
) : ViewModel() {
    fun trackUserEvent(name: String) = trackUserEventUseCase(name)

    val numberOfPeople = listOf("1", "2", "3", "10", "10+")
    val dishTypes = listOf("Breakfast", "Lunch", "Dinner", "Dessert")

    val gson = Gson()

    private val _ingredients = mutableStateOf(IngredientsState())
    val ingredients: State<IngredientsState> = _ingredients

    private val _saveMealPlanPrefsState = mutableStateOf(SaveMealPlanPrefsState())
    val saveMealPlanPrefsState: State<SaveMealPlanPrefsState> = _saveMealPlanPrefsState

    private val _allergicTo = mutableStateListOf<String>()
    val allergicTo: List<String> = _allergicTo

    fun insertAllergicTo(value: String) {
        if (allergicTo.contains(value)) {
            _allergicTo.remove(value)
            return
        }
        _allergicTo.add(value)
    }

    private val _selectedNumberOfPeople = mutableStateOf("")
    val selectedNumberOfPeople: State<String> = _selectedNumberOfPeople
    fun setSelectedNumberOfPeople(value: String) {
        _selectedNumberOfPeople.value = value
    }

    val selectedDishType = mutableStateListOf<String>()
    fun insertSelectedDishType(value: String) {
        if (selectedDishType.contains(value)) {
            selectedDishType.remove(value)
        }
        selectedDishType.add(value)
    }

    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow

    fun saveAllergies(allergies: List<String>) {
        viewModelScope.launch {
            _saveMealPlanPrefsState.value = SaveMealPlanPrefsState(
                isLoading = true
            )
            saveAllergiesUseCase(allergies)
        }
    }

    fun saveNumberOfPeople(numberOfPeople: String) {
        viewModelScope.launch {
            _saveMealPlanPrefsState.value = SaveMealPlanPrefsState(
                isLoading = true
            )
            saveNumberOfPeopleUseCase(numberOfPeople)
        }
    }

    fun saveDishTypes(dishTypes: List<String>) {
        viewModelScope.launch {
            _saveMealPlanPrefsState.value = SaveMealPlanPrefsState(
                isLoading = true
            )
            saveDishTypesUseCase(dishTypes)
        }
    }

    private fun getAllIngredients() {
        _ingredients.value = ingredients.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            when (val result = getAllIngredientsUseCase()) {
                is Resource.Error -> {
                    _ingredients.value = ingredients.value.copy(
                        isLoading = false,
                        error = result.message ?: "Unknown Error Occurred"
                    )

                    _eventsFlow.emit(
                        UiEvents.SnackbarEvent(
                            message = result.message ?: "Unknown Error Occurred"
                        )
                    )
                }

                is Resource.Success -> {
                    _ingredients.value = ingredients.value.copy(
                        isLoading = false,
                        ingredients = result.data ?: emptyList()
                    )
                }

                else -> {
                    ingredients
                }
            }
        }
    }

    init {
        getAllIngredients()
    }
}

data class IngredientsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val ingredients: List<String> = emptyList()
)

data class SaveMealPlanPrefsState(
    val isLoading: Boolean = false,
)
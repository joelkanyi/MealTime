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
package com.kanyideveloper.mealplanner.setup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.kanyideveloper.core.util.UiEvents
import com.kanyideveloper.mealplanner.destinations.MealPlannerScreenDestination
import com.kanyideveloper.mealplanner.domain.repository.MealPlannerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val mealPlannerRepository: MealPlannerRepository
) : ViewModel() {
    val gson = Gson()
    val allergies = listOf("Onions", "Tomatoes", "Chicken", "Pork")

    private val _allergicTo = mutableStateListOf<String>()
    val allergicTo: List<String> = _allergicTo

    fun insertAllergicTo(value: String) {
        if (allergicTo.contains(value)) {
            _allergicTo.remove(value)
        }
        _allergicTo.add(value)
    }

    val numberOfPeople = listOf("1", "2", "3", "10", "10+")

    private val _selectedNumberOfPeople = mutableStateOf("")
    val selectedNumberOfPeople: State<String> = _selectedNumberOfPeople
    fun setSelectedNumberOfPeople(value: String) {
        _selectedNumberOfPeople.value = value
    }

    val dishTypes = listOf("Breakfast", "Lunch", "Dinner", "Dessert")

    val selectedDishType = mutableStateListOf<String>()
    fun insertSelectedDishType(value: String) {
        if (selectedDishType.contains(value)) {
            selectedDishType.remove(value)
        }
        selectedDishType.add(value)
    }

    val hasMealPlanPrefs = mealPlannerRepository.hasMealPlanPref

    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow

    fun saveMealPlanPreferences(
        allergies: List<String>,
        numberOfPeople: String,
        dishTypes: List<String>
    ) {
        viewModelScope.launch {
            mealPlannerRepository.saveMealPlannerPreferences(
                allergies = allergies,
                numberOfPeople = numberOfPeople,
                dishTypes = dishTypes
            )

            _eventsFlow.emit(
                UiEvents.NavigationEvent(
                    route = MealPlannerScreenDestination.route
                )
            )
        }
    }
}

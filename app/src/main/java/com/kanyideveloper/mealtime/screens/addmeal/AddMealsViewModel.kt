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
package com.kanyideveloper.mealtime.screens.addmeal

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.kanyideveloper.mealtime.data.repository.HomeRepository
import com.kanyideveloper.mealtime.screens.state.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddMealsViewModel @Inject constructor(
    private val repository: HomeRepository
) : ViewModel() {

    private val _ingredient = mutableStateOf(TextFieldState())
    val ingredient: State<TextFieldState> = _ingredient
    fun setIngredientState(value: String, error: String? = null) {
        _ingredient.value = ingredient.value.copy(
            text = value,
            error = error
        )
    }

    private val _ingredientsList = mutableStateListOf<String>()
    val ingredientsList: List<String> = _ingredientsList

    fun insertIngredients(value: String) {
        if (value.isEmpty()) {
            _ingredient.value = ingredient.value.copy(
                error = "Ingredient cannot be empty"
            )
            return
        }
        _ingredientsList.add(value)
        setIngredientState("")
    }

    fun removeIngredient(value: String) {
        _ingredientsList.remove(value)
    }

    private val _direction = mutableStateOf(TextFieldState())
    val direction: State<TextFieldState> = _direction
    fun setDirectionState(value: String, error: String? = null) {
        _direction.value = direction.value.copy(
            text = value,
            error = error
        )
    }

    private val _directionsList = mutableStateListOf<String>()
    val directionsList: List<String> = _directionsList

    fun insertDirections(value: String) {
        if (value.isEmpty()) {
            _direction.value = direction.value.copy(
                error = "Direction cannot be empty"
            )
            return
        }
        _directionsList.add(value)
        setDirectionState("")
    }

    fun removeDirections(value: String) {
        _directionsList.remove(value)
    }
}

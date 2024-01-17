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
package com.joelkanyi.admeal.presentation

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joelkanyi.admeal.domain.usecase.SaveMealUseCase
import com.joelkanyi.admeal.domain.usecase.UploadMealImageUseCase
import com.joelkanyi.admeal.presentation.state.SaveMealState
import com.joelkanyi.analytics.domain.usecase.TrackUserEventUseCase
import com.joelkanyi.common.model.Ingredient
import com.joelkanyi.common.state.TextFieldState
import com.joelkanyi.common.util.Resource
import com.joelkanyi.common.util.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddMealsViewModel @Inject constructor(
    private val saveMealUseCase: SaveMealUseCase,
    private val uploadMealImageUseCase: UploadMealImageUseCase,
    private val trackUserEventUseCase: TrackUserEventUseCase,
) : ViewModel() {
    fun trackUserEvent(event: String) {
        trackUserEventUseCase(event)
    }

    private val _mealImageUri = mutableStateOf<Uri?>(null)
    val mealImageUri: State<Uri?> = _mealImageUri
    fun setMealImageUri(value: Uri?) {
        _mealImageUri.value = value
    }

    private val _mealName = mutableStateOf(TextFieldState())
    val mealName: State<TextFieldState> = _mealName
    fun setMealNameState(value: String = "", error: String? = null) {
        _mealName.value = mealName.value.copy(
            text = value,
            error = error
        )
    }

    private val _category = mutableStateOf(TextFieldState())
    val category: State<TextFieldState> = _category
    fun setCategory(value: String = "", error: String? = null) {
        _category.value = category.value.copy(
            text = value,
            error = error
        )
    }

    private val _cookingComplexity = mutableStateOf(TextFieldState())
    val cookingComplexity: State<TextFieldState> = _cookingComplexity
    fun setCookingComplexity(value: String = "", error: String? = null) {
        _cookingComplexity.value = cookingComplexity.value.copy(
            text = value,
            error = error
        )
    }

    private val _cookingTime = mutableStateOf(TextFieldState())
    val cookingTime: State<TextFieldState> = _cookingTime
    fun setCookingTime(value: String = "", error: String? = null) {
        _cookingTime.value = cookingTime.value.copy(
            text = value,
            error = error
        )
    }

    private val _peopleServing = mutableStateOf(TextFieldState())
    val peopleServing: State<TextFieldState> = _peopleServing
    fun setPeopleServing(value: String = "", error: String? = null) {
        _peopleServing.value = peopleServing.value.copy(
            text = value,
            error = error
        )
    }

    private val _saveMeal = mutableStateOf(SaveMealState())
    val saveMeal: State<SaveMealState> = _saveMeal

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _ingredient = mutableStateOf(TextFieldState())
    val ingredient: State<TextFieldState> = _ingredient
    fun setIngredientState(value: String, error: String? = null) {
        _ingredient.value = ingredient.value.copy(
            text = value,
            error = error
        )
    }

    fun saveMeal(
        imageBitmap: Bitmap,
        calories: Int,
        category: String,
        complexity: String,
        cookingInstructions: List<String>,
        cookingTime: Int,
        description: String?,
        ingredients: List<Ingredient>,
        mealName: String,
        recipePrice: Double?,
        servingPeople: Int?,
        youtubeUrl: String?
    ) {
        viewModelScope.launch {
            if (ingredientsList.isEmpty()) {
                _eventFlow.emit(
                    UiEvents.SnackbarEvent(
                        message = "Please key in some ingredients"
                    )
                )

                return@launch
            }

            if (directionsList.isEmpty()) {
                _eventFlow.emit(
                    UiEvents.SnackbarEvent(
                        message = "Please key in some preparation instructions"
                    )
                )

                return@launch
            }

            _saveMeal.value = saveMeal.value.copy(
                isLoading = true
            )

            when (
                val uploadResult = uploadMealImageUseCase(
                    imageBitmap = imageBitmap,
                )
            ) {
                is Resource.Error -> {
                    _saveMeal.value = saveMeal.value.copy(
                        isLoading = false,
                        error = uploadResult.message
                    )

                    _eventFlow.emit(
                        UiEvents.SnackbarEvent(
                            message = uploadResult.message ?: "Unknown Error Occurred"
                        )
                    )
                }

                is Resource.Success -> {
                    saveMyMeal(
                        calories = calories,
                        category = category,
                        cookingDifficulty = complexity,
                        cookingInstructions = cookingInstructions,
                        cookingTime = cookingTime,
                        description = description,
                        imageUrl = uploadResult.data.toString(),
                        ingredients = ingredients,
                        name = mealName,
                        recipePrice = recipePrice,
                        serving = servingPeople,
                        youtubeUrl = youtubeUrl
                    )
                }

                else -> {
                    saveMeal
                }
            }
        }
    }

    private fun saveMyMeal(
        calories: Int,
        category: String,
        cookingDifficulty: String,
        cookingInstructions: List<String>,
        cookingTime: Int,
        description: String?,
        imageUrl: String,
        ingredients: List<Ingredient>,
        name: String,
        recipePrice: Double?,
        serving: Int?,
        youtubeUrl: String?
    ) {
        viewModelScope.launch {
            when (
                val result = saveMealUseCase(
                    calories = calories,
                    category = category,
                    cookingDifficulty = cookingDifficulty,
                    cookingInstructions = cookingInstructions,
                    cookingTime = cookingTime,
                    description = description,
                    imageUrl = imageUrl,
                    ingredients = ingredients,
                    name = name,
                    recipePrice = recipePrice,
                    serving = serving,
                    youtubeUrl = youtubeUrl
                )
            ) {
                is Resource.Error -> {
                    _eventFlow.emit(
                        UiEvents.SnackbarEvent(
                            message = result.message ?: "Unknown Error Occurred"
                        )
                    )
                }

                is Resource.Success -> {
                    _saveMeal.value = saveMeal.value.copy(
                        isLoading = false,
                        mealIsSaved = true
                    )

                    _eventFlow.emit(
                        UiEvents.SnackbarEvent(
                            message = "Meal Saved Successful"
                        )
                    )

                    _eventFlow.emit(
                        UiEvents.NavigationEvent(
                            route = ""
                        )
                    )
                }

                else -> {}
            }
        }
    }

    private val _ingredientsList = mutableStateListOf<Ingredient>()
    val ingredientsList: List<Ingredient> = _ingredientsList

    fun insertIngredients(value: Ingredient) {
        if (value.name.isEmpty()) {
            _ingredient.value = ingredient.value.copy(
                error = "Ingredient cannot be empty"
            )
            return
        }
        splitIngredients(value)
        setIngredientState("")
    }

    private fun splitIngredients(value: Ingredient) {
        val ingredients = value.name.split(".").filter {
            it.trim().isNotEmpty()
        }
        ingredients.forEach {
            if (it.trim().isNotEmpty()) {
                _ingredientsList.add(
                    Ingredient(
                        name = it.trim(),
                        quantity = value.quantity,
                        id = 1
                    )
                )
            }
        }
    }

    fun removeIngredient(value: Ingredient) {
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
        splitInstructions(value)
        setDirectionState("")
    }

    private fun splitInstructions(value: String) {
        val instructions = value.split(".").filter {
            it.trim().isNotEmpty()
        }
        instructions.forEach {
            if (it.trim().isNotEmpty()) {
                _directionsList.add(it.trim())
            }
        }
    }

    fun removeDirections(value: String) {
        _directionsList.remove(value)
    }

    val categories = listOf(
        "Food",
        "Breakfast",
        "Drinks",
        "Fruits",
        "Fast Food"
    )

    val cookingComplexities = listOf(
        "Easy",
        "Medium",
        "Hard"
    )
}

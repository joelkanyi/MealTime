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

import android.net.Uri
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.mealtime.data.repository.UploadImageRepository
import com.kanyideveloper.mealtime.screens.addmeal.state.SaveMealState
import com.kanyideveloper.mealtime.screens.state.TextFieldState
import com.kanyideveloper.mealtime.util.Resource
import com.kanyideveloper.mealtime.util.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class AddMealsViewModel @Inject constructor(
    private val uploadImageRepository: UploadImageRepository
) : ViewModel() {

    private val _saveMeal = mutableStateOf(SaveMealState())
    val saveMeal: State<SaveMealState> = _saveMeal

    private val _eventFlow = MutableSharedFlow<UiEvents>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _imageUri = mutableStateOf<Uri?>(null)
    val imageUri: State<Uri?> = _imageUri
    fun setProductImageUri(value: Uri?) {
        _imageUri.value = value
    }

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

    fun saveMeal(imageUri: Uri) {
        // TODO("Add Validations")

        _saveMeal.value = saveMeal.value.copy(
            isLoading = true
        )

        viewModelScope.launch {
            when (val uploadResult = uploadImageRepository.uploadImage(imageUri = imageUri)) {
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
                    Timber.d("Image Url: ${uploadResult.data}")

                    _saveMeal.value = saveMeal.value.copy(
                        isLoading = false,
                        data = uploadResult.data.toString()
                    )

                    _eventFlow.emit(
                        UiEvents.SnackbarEvent(
                            message = uploadResult.data ?: "Image Uploaded Successful"
                        )
                    )
                }
                else -> {
                    saveMeal
                }
            }
        }
    }
}

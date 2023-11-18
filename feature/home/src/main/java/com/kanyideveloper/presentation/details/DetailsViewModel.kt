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
package com.kanyideveloper.presentation.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.core.domain.FavoritesRepository
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.core.util.UiEvents
import com.kanyideveloper.domain.repository.OnlineMealsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val onlineMealsRepository: OnlineMealsRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow.asSharedFlow()

    private val _details = mutableStateOf(DetailsState())
    val details: State<DetailsState> = _details

    fun getDetails(mealId: String) {
        _details.value = details.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            when (val result = onlineMealsRepository.getMealDetails(mealId = mealId)) {
                is Resource.Error -> {
                    _details.value = details.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Resource.Success -> {
                    _details.value = details.value.copy(
                        isLoading = false,
                        mealDetails = result.data
                    )
                }
                else -> {
                    details
                }
            }
        }
    }

    private val _randomMeal = mutableStateOf(DetailsState())
    val randomMeal: State<DetailsState> = _randomMeal

    fun getRandomMeal() {
        _randomMeal.value = randomMeal.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            when (val result = onlineMealsRepository.getRandomMeal()) {
                is Resource.Error -> {
                    _randomMeal.value = randomMeal.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Resource.Success -> {
                    _randomMeal.value = randomMeal.value.copy(
                        isLoading = false,
                        mealDetails = result.data
                    )
                }
                else -> {
                    randomMeal
                }
            }
        }
    }

    fun inFavorites(id: String): LiveData<Boolean> {
        return favoritesRepository.isFavorite(id = id)
    }

    fun deleteALocalFavorite(mealId: String) {
        viewModelScope.launch {
            favoritesRepository.deleteAFavorite(mealId = mealId)
        }
    }

    fun deleteAnOnlineFavorite(mealId: String) {
        viewModelScope.launch {
            favoritesRepository.deleteAFavorite(mealId = mealId)
        }
    }

    fun insertAFavorite(
        mealId: String
    ) {
        viewModelScope.launch {
            when (
                val result = favoritesRepository.insertFavorite(mealId)
            ) {
                is Resource.Error -> {
                    _eventsFlow.emit(
                        UiEvents.SnackbarEvent(
                            message = result.message ?: "An error occurred"
                        )
                    )
                }
                is Resource.Success -> {
                    _eventsFlow.emit(
                        UiEvents.SnackbarEvent(
                            message = "Added to favorites"
                        )
                    )
                }
                else -> {}
            }
        }
    }
}

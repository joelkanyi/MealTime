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
package com.joelkanyi.presentation.home.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joelkanyi.domain.usecase.GetMealDetailsUseCase
import com.joelkanyi.domain.usecase.GetRandomMealUseCase
import com.kanyideveloper.analytics.domain.usecase.TrackUserEventUseCase
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.model.MealDetails
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.core.util.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val trackUserEventUseCase: TrackUserEventUseCase,
    private val deleteAFavoriteUseCase: com.joelkanyi.domain.usecase.DeleteAFavoriteUseCase,
    private val insertAFavoriteUseCase: com.joelkanyi.domain.usecase.InsertAFavoriteUseCase,
    private val getRandomMealUseCase: GetRandomMealUseCase,
    private val getMealDetailsUseCase: GetMealDetailsUseCase,
    getFavoritesUseCase: com.joelkanyi.domain.usecase.GetFavoritesUseCase,
) : ViewModel() {
    fun trackUserEvent(name: String) = trackUserEventUseCase(name)

    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow.asSharedFlow()

    private val _details = mutableStateOf(DetailsState())
    val details: State<DetailsState> = _details

    val favorites = getFavoritesUseCase()
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

    fun getDetails(mealId: String) {
        _details.value = details.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            when (val result = getMealDetailsUseCase(mealId = mealId)) {
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

    fun getRandomMeal() {
        _details.value = details.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            when (val result = getRandomMealUseCase()) {
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

    fun insertAFavorite(
        meal: Meal
    ) {
        viewModelScope.launch {
            when (
                val result = insertAFavoriteUseCase(
                    meal = meal
                )
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

    fun deleteAFavorite(mealId: String) {
        viewModelScope.launch {
            deleteAFavoriteUseCase(mealId)
        }
    }
}

data class DetailsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val mealDetails: MealDetails? = null
)

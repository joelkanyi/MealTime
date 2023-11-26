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
package com.joelkanyi.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joelkanyi.analytics.domain.usecase.TrackUserEventUseCase
import com.joelkanyi.common.model.Category
import com.joelkanyi.common.model.Meal
import com.joelkanyi.common.util.Resource
import com.joelkanyi.common.util.UiEvents
import com.joelkanyi.domain.usecase.GetCategoriesUseCase
import com.joelkanyi.domain.usecase.GetMealsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val trackUserEventUseCase: TrackUserEventUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val getMealsUseCase: GetMealsUseCase,
    getFavoritesUseCase: com.joelkanyi.domain.usecase.GetFavoritesUseCase,
    private val deleteAFavoriteUseCase: com.joelkanyi.domain.usecase.DeleteAFavoriteUseCase,
    private val insertAFavoriteUseCase: com.joelkanyi.domain.usecase.InsertAFavoriteUseCase,
) : ViewModel() {
    fun trackUserEvent(name: String) = trackUserEventUseCase(name)

    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow.asSharedFlow()

    private val _selectedCategory = mutableStateOf("All")
    val selectedCategory: State<String> = _selectedCategory
    fun setSelectedCategory(value: String) {
        _selectedCategory.value = value
    }

    val favorites = getFavoritesUseCase()
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )

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

    private val _categories = mutableStateOf(CategoriesState())
    val categories: State<CategoriesState> = _categories

    private val _mealsUiState = mutableStateOf(MealsUiState())
    val mealsUiState: State<MealsUiState> = _mealsUiState

    init {
        getCategories()
        getMeals(category = selectedCategory.value)
    }

    private fun getCategories() {
        _categories.value = categories.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            when (val result = getCategoriesUseCase()) {
                is Resource.Error -> {
                    _categories.value = categories.value.copy(
                        isLoading = false,
                        error = result.message,
                        categories = result.data ?: emptyList()
                    )
                    _eventsFlow.emit(
                        UiEvents.SnackbarEvent(
                            result.message ?: "An unknown error occurred"
                        )
                    )
                }

                is Resource.Success -> {
                    _categories.value = categories.value.copy(
                        isLoading = false,
                        categories = listOf(
                            Category(
                                categoryName = "All",
                                categoryId = -1
                            )
                        ) + (result.data ?: emptyList())

                    )
                }

                else -> {
                    categories
                }
            }
        }
    }

    fun getMeals(category: String) {
        viewModelScope.launch {
            _mealsUiState.value = mealsUiState.value.copy(
                isLoading = true,
            )
            when (val result = getMealsUseCase(category = category)) {
                is Resource.Error -> {
                    _mealsUiState.value = mealsUiState.value.copy(
                        isLoading = false,
                        error = result.message,
                        meals = result.data ?: emptyList()
                    )
                    _eventsFlow.emit(
                        UiEvents.SnackbarEvent(
                            result.message ?: "An unknown error occurred"
                        )
                    )
                }

                is Resource.Success -> {
                    _mealsUiState.value = mealsUiState.value.copy(
                        isLoading = false,
                        meals = result.data ?: emptyList()
                    )
                }

                else -> {
                    mealsUiState
                }
            }
        }
    }

    fun deleteAFavorite(mealId: String) {
        viewModelScope.launch {
            deleteAFavoriteUseCase(mealId)
        }
    }
}

data class MealsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val meals: List<Meal> = emptyList()
)

data class CategoriesState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val categories: List<Category> = emptyList()
)

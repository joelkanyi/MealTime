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
package com.kanyideveloper.presentation.home.onlinemeal

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.core.analytics.AnalyticsUtil
import com.kanyideveloper.core.data.MealTimePreferences
import com.kanyideveloper.core.domain.FavoritesRepository
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.core.util.UiEvents
import com.kanyideveloper.domain.repository.OnlineMealsRepository
import com.kanyideveloper.presentation.home.onlinemeal.state.CategoriesState
import com.kanyideveloper.presentation.home.onlinemeal.state.MealState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnlineMealViewModel @Inject constructor(
    private val onlineMealsRepository: OnlineMealsRepository,
    private val favoritesRepository: FavoritesRepository,
    private val analyticsUtil: AnalyticsUtil,
    private val mealTimePreferences: MealTimePreferences
) : ViewModel() {
    fun analyticsUtil() = analyticsUtil

    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow

    private val _categories = mutableStateOf(CategoriesState())
    val categories: State<CategoriesState> = _categories

    private val _meals = mutableStateOf(MealState())
    val meals: State<MealState> = _meals

    private val _selectedCategory = mutableStateOf("Beef")
    val selectedCategory: State<String> = _selectedCategory
    fun setSelectedCategory(value: String) {
        _selectedCategory.value = value
    }

    init {
        getCategories()
        getMeals(category = selectedCategory.value)
    }

    fun saveToken() {
        viewModelScope.launch {
            mealTimePreferences.saveAccessToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2Vsa2FueWk5OEBnbWFpbC5jb20iLCJpYXQiOjE2OTk5ODI0NTMsImV4cCI6MTcwMDA2ODg1M30.YHOFebYj86PfcnSjWs8RcWe67RHl8LqJHUsFqz_yhVA")
        }
    }

    private fun getCategories() {
        _categories.value = categories.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            when (val result = onlineMealsRepository.getMealCategories()) {
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
                        categories = result.data ?: emptyList()
                    )
                }

                else -> {
                    categories
                }
            }
        }
    }

    fun getMeals(category: String) {
        _meals.value = meals.value.copy(
            isLoading = true,
            meals = emptyList(),
            error = null
        )
        viewModelScope.launch {
            when (val result = onlineMealsRepository.getMeals(category = category)) {
                is Resource.Error -> {
                    _meals.value = meals.value.copy(
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
                    _meals.value = meals.value.copy(
                        isLoading = false,
                        meals = result.data ?: emptyList()
                    )
                }

                else -> {
                    categories
                }
            }
        }
    }

    fun inOnlineFavorites(id: String): LiveData<Boolean> {
        return favoritesRepository.isFavorite(id = id)
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

    fun deleteAnOnlineFavorite(mealId: String) {
        viewModelScope.launch {
            favoritesRepository.deleteAFavorite(mealId = mealId)
        }
    }
}

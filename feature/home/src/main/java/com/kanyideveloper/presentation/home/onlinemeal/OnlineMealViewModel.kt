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
import com.kanyideveloper.core.domain.FavoritesRepository
import com.kanyideveloper.core.model.Favorite
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.core.util.UiEvents
import com.kanyideveloper.domain.repository.OnlineMealsRepository
import com.kanyideveloper.presentation.home.onlinemeal.state.CategoriesState
import com.kanyideveloper.presentation.home.onlinemeal.state.MealState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OnlineMealViewModel @Inject constructor(
    private val onlineMealsRepository: OnlineMealsRepository,
    private val favoritesRepository: FavoritesRepository,
) : ViewModel() {

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

    private fun getCategories() {
        _categories.value = categories.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            when (val result = onlineMealsRepository.getMealCategories()) {
                is Resource.Error -> {
                    _categories.value = categories.value.copy(
                        isLoading = false,
                        error = result.message
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
                        error = result.message
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
        return favoritesRepository.isOnlineFavorite(id = id)
    }

    fun insertAFavorite(
        isOnline: Boolean,
        onlineMealId: String? = null,
        localMealId: String? = null,
        mealImageUrl: String,
        mealName: String,
    ) {
        viewModelScope.launch {
            val favorite = Favorite(
                onlineMealId = onlineMealId,
                localMealId = localMealId,
                mealName = mealName,
                mealImageUrl = mealImageUrl,
                online = isOnline,
                favorite = true
            )
            when (val result = favoritesRepository.insertFavorite(
                favorite = favorite,
                isSubscribed = true
            )) {
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

    fun deleteAnOnlineFavorite(onlineMealId: String) {
        viewModelScope.launch {
            favoritesRepository.deleteAnOnlineFavorite(
                onlineMealId = onlineMealId,
                isSubscribed = true
            )
        }
    }
}

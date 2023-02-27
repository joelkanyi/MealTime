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
package com.kanyideveloper.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.core.domain.FavoritesRepository
import com.kanyideveloper.core.domain.HomeRepository
import com.kanyideveloper.core.domain.SubscriptionRepository
import com.kanyideveloper.core.model.Favorite
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.state.SubscriptionStatusUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository,
    private val favoritesRepository: FavoritesRepository,
    subscriptionRepository: SubscriptionRepository,
) : ViewModel() {

    val isSubscribed: StateFlow<SubscriptionStatusUiState> =
        subscriptionRepository.isSubscribed
            .map(SubscriptionStatusUiState::Success)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = SubscriptionStatusUiState.Loading,
            )

    private val _myMeals = MutableLiveData<LiveData<List<Meal>>>()
    val myMeals: LiveData<LiveData<List<Meal>>> = _myMeals

    fun getMyMeals(filterCategory: String = "All") {
        _myMeals.value = if (filterCategory == "All") {
            homeRepository.getMyMeals()
        } else {
            Transformations.map(homeRepository.getMyMeals()) { meals ->
                meals.filter { it.category == filterCategory }
            }
        }
    }

    init {
        getMyMeals()
    }

    private val _isMyMeal = mutableStateOf(true)
    val isMyMeal: State<Boolean> = _isMyMeal
    fun setIsMyMeal(value: Boolean) {
        _isMyMeal.value = value
    }

    private val _selectedCategory = mutableStateOf("All")
    val selectedCategory: State<String> = _selectedCategory
    fun setSelectedCategory(value: String) {
        _selectedCategory.value = value
    }

    fun inFavorites(id: Int): LiveData<Boolean> {
        return favoritesRepository.isLocalFavorite(id = id)
    }

    fun insertAFavorite(
        isOnline: Boolean = false,
        onlineMealId: String? = null,
        localMealId: Int? = null,
        mealImageUrl: String,
        mealName: String,
    ) {
        viewModelScope.launch {
            val favorite = Favorite(
                onlineMealId = onlineMealId,
                localMealId = localMealId,
                mealName = mealName,
                mealImageUrl = mealImageUrl,
                isOnline = isOnline,
                isFavorite = true
            )
            favoritesRepository.insertFavorite(
                favorite = favorite
            )
        }
    }

    fun deleteALocalFavorite(localMealId: Int) {
        viewModelScope.launch {
            favoritesRepository.deleteALocalFavorite(localMealId = localMealId)
        }
    }
}

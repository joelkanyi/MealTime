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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.core.domain.FavoritesRepository
import com.kanyideveloper.core.domain.HomeRepository
import com.kanyideveloper.core.model.Favorite
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    homeRepository: HomeRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    val myMeals = homeRepository.getMyMeals()

    private val _isMyMeal = mutableStateOf(true)
    val isMyMeal: State<Boolean> = _isMyMeal
    fun setIsMyMeal(value: Boolean) {
        _isMyMeal.value = value
    }

    fun inFavorites(id: Int): LiveData<Boolean> {
        return favoritesRepository.inFavorites(id = id)
    }

    fun insertAFavorite(
        isOnline: Boolean = false,
        onlineMealId: String? = null,
        localMealId: Int? = null,
        mealImageUrl: String,
        mealName: String
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
}

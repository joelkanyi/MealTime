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
package com.kanyideveloper.favorites.presentation.favorites.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.domain.repository.HomeRepository
import com.kanyideveloper.favorites.presentation.favorites.domain.FavoritesRepository
import com.kanyideveloper.favorites.presentation.favorites.domain.model.Favorite
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val favoritesRepository: FavoritesRepository,
    private val homeRepository: HomeRepository
) : ViewModel() {

    val favorites = favoritesRepository.getFavorites()

    fun inFavorites(id: Int): LiveData<Boolean> {
        return favoritesRepository.inFavorites(id = id)
    }

    private val _singleMeal = MutableLiveData<Meal?>()
    val singleMeal: LiveData<Meal?> = _singleMeal
    fun getASingleMeal(id: Int) {
        _singleMeal.value = homeRepository.getMealById(id = id).value
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

    fun deleteAFavorite(favorite: Favorite) {
        viewModelScope.launch {
            favoritesRepository.deleteOneFavorite(favorite = favorite)
        }
    }

    fun deleteAllFavorites() {
        viewModelScope.launch {
            favoritesRepository.deleteAllFavorites()
        }
    }
}

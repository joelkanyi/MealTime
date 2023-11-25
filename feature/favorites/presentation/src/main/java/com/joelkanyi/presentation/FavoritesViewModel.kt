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
package com.joelkanyi.presentation

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joelkanyi.domain.entity.Favorite
import com.joelkanyi.domain.usecase.DeleteAllFavoritesUseCase
import com.kanyideveloper.analytics.domain.usecase.TrackUserEventUseCase
import com.kanyideveloper.core.model.MealDetails
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
class FavoritesViewModel @Inject constructor(
    private val trackUserEventUseCase: TrackUserEventUseCase,
    getFavoritesUseCase: com.joelkanyi.domain.usecase.GetFavoritesUseCase,
    private val deleteAFavoriteUseCase: com.joelkanyi.domain.usecase.DeleteAFavoriteUseCase,
    private val insertAFavoriteUseCase: com.joelkanyi.domain.usecase.InsertAFavoriteUseCase,
    private val deleteAllFavoritesUseCase: DeleteAllFavoritesUseCase,
) : ViewModel() {
    fun trackUserEvent(name: String) = trackUserEventUseCase(name)

    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow.asSharedFlow()

    private val _favoritesUiState = mutableStateOf(FavoritesUiState())
    val favoritesUiState = _favoritesUiState

    val favorites = getFavoritesUseCase()
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    /*
        fun getFavorites(isSubscribed: Boolean) {
            _favoritesUiState.value =
                _favoritesUiState.value.copy(
                    isLoading = true,
                    favorites = emptyList(),
                    error = null
                )
            viewModelScope.launch {
                favoritesRepository.getFavorites().collectLatest { result ->
                    when (result) {
                        is Resource.Error -> {
                            _eventsFlow.emit(
                                UiEvents.SnackbarEvent(
                                    message = result.message ?: "An unexpected error occurred"
                                )
                            )

                            _favoritesUiState.value = _favoritesUiState.value.copy(
                                isLoading = false,
                                favorites = emptyList(),
                                error = result.message ?: "An unexpected error occurred"
                            )
                        }

                        is Resource.Success -> {
                            _favoritesUiState.value = _favoritesUiState.value.copy(
                                isLoading = false,
                                favorites = result.data ?: emptyList(),
                                error = null
                            )
                        }

                        else -> {
                            favoritesUiState
                        }
                    }
                }
            }
        }
    */

    private val _singleMeal = MutableLiveData<LiveData<MealDetails?>>()
    val singleMeal: LiveData<LiveData<MealDetails?>> = _singleMeal


    fun deleteAFavorite(favorite: Favorite) {
        viewModelScope.launch {
            deleteAFavoriteUseCase(favorite.mealId)
        }
    }

    fun deleteAllFavorites() {
        viewModelScope.launch {
            deleteAllFavoritesUseCase()
        }
    }
}

data class FavoritesUiState(
    val favorites: List<com.joelkanyi.domain.entity.Favorite> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

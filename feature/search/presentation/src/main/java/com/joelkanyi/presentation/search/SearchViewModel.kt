/*
 * Copyright 2023 Joel Kanyi.
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
package com.joelkanyi.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joelkanyi.domain.usecase.DeleteAFavoriteUseCase
import com.joelkanyi.domain.usecase.GetFavoritesUseCase
import com.joelkanyi.domain.usecase.InsertAFavoriteUseCase
import com.joelkanyi.domain.usecase.SearchMealsUseCase
import com.kanyideveloper.analytics.domain.usecase.TrackUserEventUseCase
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.core.util.UiEvents
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val trackUserEventUseCase: TrackUserEventUseCase,
    getFavoritesUseCase: GetFavoritesUseCase,
    private val deleteAFavoriteUseCase: DeleteAFavoriteUseCase,
    private val insertAFavoriteUseCase: InsertAFavoriteUseCase,
    private val searchMealsUseCase: SearchMealsUseCase,
) : ViewModel() {
    fun trackUserEvent(name: String) = trackUserEventUseCase(name)

    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow

    val favorites = getFavoritesUseCase()
        .map { it }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList()
        )


    private val _selectedSearchOption = mutableStateOf("Meal Name")
    val selectedSearchOption: State<String> = _selectedSearchOption
    fun setSelectedSearchOption(value: String) {
        _selectedSearchOption.value = value
        _searchState.value = searchState.value.copy(
            error = null,
            searchData = emptyList()
        )
    }

    private val _searchString = mutableStateOf("")
    val searchString: State<String> = _searchString
    fun setSearchString(value: String) {
        _searchString.value = value
        _searchState.value = searchState.value.copy(
            error = null,
            searchData = emptyList()
        )
    }

    private val _searchState = mutableStateOf(SearchState())
    val searchState: State<SearchState> = _searchState

    fun search(searchParam: String) {
        viewModelScope.launch {
            if (searchParam.isBlank()) {
                Timber.e("Empty Search String")
                _eventsFlow.emit(
                    UiEvents.SnackbarEvent(
                        message = "Please input a search term"
                    )
                )

                return@launch
            }

            _searchState.value = searchState.value.copy(
                isLoading = true
            )

            when (
                val result = searchMealsUseCase(
                    searchOption = selectedSearchOption.value,
                    searchParam = searchParam.trim()
                )
            ) {
                is Resource.Error -> {
                    _searchState.value = searchState.value.copy(
                        isLoading = false,
                        error = result.error?.message ?: "Unknown Error Occurred"
                    )
                }

                is Resource.Success -> {
                    _searchState.value = searchState.value.copy(
                        isLoading = false,
                        searchData = result.data ?: emptyList()
                    )
                }

                else -> {
                    searchState
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

    fun deleteAnOnlineFavorite(
        mealId: String,
    ) {
        viewModelScope.launch {
            deleteAFavoriteUseCase(mealId)
        }
    }
}


data class SearchState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchData: List<Meal> = emptyList()
)

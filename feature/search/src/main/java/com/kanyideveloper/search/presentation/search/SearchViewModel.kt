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
package com.kanyideveloper.search.presentation.search

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.core.util.UiEvents
import com.kanyideveloper.search.domain.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _eventsFlow = MutableSharedFlow<UiEvents>()
    val eventsFlow = _eventsFlow

    private val _selectedSearchOption = mutableStateOf("Meal Name")
    val selectedSearchOption: State<String> = _selectedSearchOption
    fun setSelectedSearchOption(value: String) {
        _selectedSearchOption.value = value
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
                val result = searchRepository.search(
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
}

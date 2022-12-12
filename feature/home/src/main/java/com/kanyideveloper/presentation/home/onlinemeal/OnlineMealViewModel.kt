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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.domain.repository.OnlineMealsRepository
import com.kanyideveloper.presentation.home.onlinemeal.state.CategoriesState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class OnlineMealViewModel @Inject constructor(
    private val onlineMealsRepository: OnlineMealsRepository
) : ViewModel() {

    private val _categories = mutableStateOf(CategoriesState())
    val categories: State<CategoriesState> = _categories

    init {
        getCategories()
    }

    private val _selectedCategory = mutableStateOf("Beef")
    val selectedCategory: State<String> = _selectedCategory
    fun setSelectedCategory(value: String) {
        _selectedCategory.value = value
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
}

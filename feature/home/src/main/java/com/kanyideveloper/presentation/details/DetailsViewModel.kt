package com.kanyideveloper.presentation.details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.domain.repository.OnlineMealsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val onlineMealsRepository: OnlineMealsRepository,
) : ViewModel() {

    private val _details = mutableStateOf(DetailsState())
    val details: State<DetailsState> = _details

    fun getCategories(mealId: String) {
        _details.value = details.value.copy(
            isLoading = true
        )
        viewModelScope.launch {
            when (val result = onlineMealsRepository.getMealDetails(mealId = mealId)) {
                is Resource.Error -> {
                    _details.value = details.value.copy(
                        isLoading = false,
                        error = result.message
                    )
                }
                is Resource.Success -> {
                    _details.value = details.value.copy(
                        isLoading = false,
                        mealDetails = result.data ?: emptyList()
                    )
                }
                else -> {
                    details
                }
            }
        }
    }

}
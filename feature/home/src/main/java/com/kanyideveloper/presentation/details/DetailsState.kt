package com.kanyideveloper.presentation.details

import com.kanyideveloper.core.model.Meal

data class DetailsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val mealDetails: List<Meal> = emptyList()
)

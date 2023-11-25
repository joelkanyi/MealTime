package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.MealPlannerRepository
import javax.inject.Inject

class GetAllIngredientsUseCase @Inject constructor(
    private val repository: MealPlannerRepository
) {
    suspend operator fun invoke() = repository.getAllIngredients()
}
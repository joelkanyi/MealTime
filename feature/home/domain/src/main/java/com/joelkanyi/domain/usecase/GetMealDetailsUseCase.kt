package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.MealsRepository
import javax.inject.Inject

class GetMealDetailsUseCase @Inject constructor(
    private val mealsRepository: MealsRepository
) {
    suspend operator fun invoke(mealId: String) = mealsRepository.getMealDetails(mealId)
}

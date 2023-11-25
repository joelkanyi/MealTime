package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.MealPlannerRepository
import javax.inject.Inject

class RemoveMealFromMealPlanUseCase @Inject constructor(
    private val mealPlanRepository: MealPlannerRepository
) {
    suspend operator fun invoke(
        mealId: String,
    ) = mealPlanRepository.deleteAMealFromPlan(mealId)
}
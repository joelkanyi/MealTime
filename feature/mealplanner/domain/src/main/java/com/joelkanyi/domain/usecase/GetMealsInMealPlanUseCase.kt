package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.MealPlannerRepository
import javax.inject.Inject

class GetMealsInMealPlanUseCase @Inject constructor(
    private val mealPlanRepository: MealPlannerRepository
) {
    suspend operator fun invoke(filterDay: String) = mealPlanRepository.getMealsInMyPlan(filterDay)
}
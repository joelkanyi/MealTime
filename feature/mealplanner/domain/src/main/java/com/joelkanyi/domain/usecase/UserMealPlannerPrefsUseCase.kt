package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.MealPlannerRepository
import javax.inject.Inject

class UserMealPlannerPrefsUseCase @Inject constructor(
    private val mealPlanRepository: MealPlannerRepository
) {
    operator fun invoke() = mealPlanRepository.mealPlanPref()
}
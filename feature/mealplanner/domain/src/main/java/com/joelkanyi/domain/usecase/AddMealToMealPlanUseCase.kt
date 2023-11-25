package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.MealPlannerRepository
import javax.inject.Inject

class AddMealToMealPlanUseCase @Inject constructor(
    private val mealPlanRepository: MealPlannerRepository
) {
    suspend operator fun invoke(
        mealTypeName: String,
        mealName: String,
        mealImageUrl: String,
        mealId: String,
        mealCategory: String,
        date: String,
    ) = mealPlanRepository.saveMealToPlan(
        mealTypeName = mealTypeName,
        mealName = mealName,
        mealImageUrl = mealImageUrl,
        mealId = mealId,
        mealCategory = mealCategory,
        date = date
    )
}
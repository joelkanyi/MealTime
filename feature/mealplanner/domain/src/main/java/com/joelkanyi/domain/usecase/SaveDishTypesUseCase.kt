package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.MealPlannerRepository
import javax.inject.Inject

class SaveDishTypesUseCase @Inject constructor(
    private val repository: MealPlannerRepository
) {
    suspend operator fun invoke(dishTypes: List<String>)  = repository.saveDishTypes(dishTypes)
}
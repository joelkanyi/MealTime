package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.MealPlannerRepository
import javax.inject.Inject

class SaveAllergiesUseCase @Inject constructor(
    private val repository: MealPlannerRepository
) {
    suspend operator fun invoke(allergies: List<String>)  = repository.saveAllergies(allergies)
}
package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.MealsRepository
import javax.inject.Inject

class GetMealsUseCase @Inject constructor(
    private val mealsRepository: MealsRepository
) {
    suspend operator fun invoke(category: String) = mealsRepository.getMeals(category)
}
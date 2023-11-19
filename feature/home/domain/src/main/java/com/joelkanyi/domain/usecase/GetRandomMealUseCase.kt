package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.MealsRepository
import javax.inject.Inject

class GetRandomMealUseCase @Inject constructor(
    private val mealsRepository: MealsRepository
) {
    suspend operator fun invoke() = mealsRepository.getRandomMeal()
}
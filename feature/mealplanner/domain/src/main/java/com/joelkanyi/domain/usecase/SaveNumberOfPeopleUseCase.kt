package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.MealPlannerRepository
import javax.inject.Inject

class SaveNumberOfPeopleUseCase @Inject constructor(
    private val repository: MealPlannerRepository
) {
    suspend operator fun invoke(numberOfPeople: String) = repository.saveNumberOfPeople(numberOfPeople)
}
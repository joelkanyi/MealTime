package com.joelkanyi.domain.usecase

import com.joelkanyi.domain.repository.FavoritesRepository
import com.kanyideveloper.core.model.Meal
import javax.inject.Inject

class InsertAFavoriteUseCase @Inject constructor(
    private val favoritesRepository: FavoritesRepository
) {
    suspend operator fun invoke(meal: Meal) = favoritesRepository.insertFavorite(
        name = meal.name,
        category = meal.category,
        mealId = meal.mealId,
        imageUrl = meal.imageUrl,
    )
}
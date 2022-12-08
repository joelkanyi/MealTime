package com.kanyideveloper.addmeal.data.mapper

import com.kanyideveloper.addmeal.domain.model.Meal
import com.kanyideveloper.core_database.model.MealEntity

internal fun Meal.toMealEntity(): MealEntity {
    return MealEntity(
        name = name,
        imageUrl = imageUrl,
        cookingTime = cookingTime,
        category = category,
        cookingDifficulty = cookingDifficulty,
        ingredients = ingredients,
        cookingDirections = cookingDirections,
        isFavorite = isFavorite
    )
}

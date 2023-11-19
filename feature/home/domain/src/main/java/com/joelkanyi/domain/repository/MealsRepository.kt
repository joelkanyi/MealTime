package com.joelkanyi.domain.repository

import com.kanyideveloper.core.model.Category
import com.kanyideveloper.core.model.MealDetails
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.util.Resource

interface MealsRepository {
    suspend fun getMealCategories(): Resource<List<Category>>
    suspend fun getMeals(category: String): Resource<List<Meal>>
    suspend fun getMealDetails(mealId: String): Resource<MealDetails>
    suspend fun getRandomMeal(): Resource<MealDetails>
}

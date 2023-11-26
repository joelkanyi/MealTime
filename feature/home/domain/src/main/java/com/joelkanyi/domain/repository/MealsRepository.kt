package com.joelkanyi.domain.repository

import com.joelkanyi.common.model.Category
import com.joelkanyi.common.model.Meal
import com.joelkanyi.common.model.MealDetails
import com.joelkanyi.common.util.Resource


interface MealsRepository {
    suspend fun getMealCategories(): Resource<List<Category>>
    suspend fun getMeals(category: String): Resource<List<Meal>>
    suspend fun getMealDetails(mealId: String): Resource<MealDetails>
    suspend fun getRandomMeal(): Resource<MealDetails>
}

/*
 * Copyright 2022 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kanyideveloper.data.mapper

import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core_database.model.MealEntity
import com.kanyideveloper.core_network.model.CategoriesResponse
import com.kanyideveloper.core_network.model.MealsResponse
import com.kanyideveloper.domain.model.Category
import com.kanyideveloper.domain.model.OnlineMeal

internal fun MealEntity.toMeal(): Meal {
    return Meal(
        name = name,
        imageUrl = imageUrl,
        cookingTime = cookingTime,
        category = category,
        cookingDifficulty = cookingDifficulty,
        ingredients = ingredients,
        cookingDirections = cookingDirections,
        isFavorite = isFavorite,
        servingPeople = servingPeople
    )
}

internal fun CategoriesResponse.Category.toCategory(): Category {
    return Category(
        categoryId = idCategory,
        categoryName = strCategory,
        categoryDescription = strCategoryDescription,
        categoryImageUrl = strCategoryThumb
    )
}

internal fun MealsResponse.Meal.toMeal(): OnlineMeal {
    return OnlineMeal(
        name = strMeal,
        imageUrl = strMealThumb,
        mealId = idMeal
    )
}

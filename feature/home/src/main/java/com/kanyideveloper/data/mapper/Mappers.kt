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
import com.kanyideveloper.core_database.model.OnlineMealCategoryEntity
import com.kanyideveloper.core_database.model.OnlineMealEntity
import com.kanyideveloper.core_network.model.CategoriesResponseDto
import com.kanyideveloper.core_network.model.MealDetailsResponseDto
import com.kanyideveloper.core_network.model.MealsResponseDto
import com.kanyideveloper.domain.model.Category
import com.kanyideveloper.domain.model.OnlineMeal

internal fun MealEntity.toMeal(): Meal {
    return Meal(
        name = name,
        imageUrl = imageUrl,
        cookingTime = cookingTime,
        servingPeople = servingPeople,
        category = category,
        cookingDifficulty = cookingDifficulty,
        ingredients = ingredients,
        cookingDirections = cookingInstructions,
        favorite = isFavorite
    )
}

internal fun CategoriesResponseDto.toCategory(): Category {
    return Category(
        categoryId = id,
        categoryName = name,
    )
}

internal fun MealsResponseDto.toMeal(): OnlineMeal {
    return OnlineMeal(
        name = name,
        imageUrl = image,
        mealId = id
    )
}

internal fun CategoriesResponseDto.toEntity() = OnlineMealCategoryEntity(
    id = id,
    name = name,
)

internal fun OnlineMealCategoryEntity.toCategory() = Category(
    categoryId = id,
    categoryName = name,
)

internal fun MealsResponseDto.toEntity(category: String) = OnlineMealEntity(
    idMeal = id,
    strMeal = name,
    strMealThumb = image,
    strCategory = category
)

internal fun OnlineMealEntity.toMeal() = OnlineMeal(
    name = strMeal,
    imageUrl = strMealThumb,
    mealId = idMeal
)

internal fun MealDetailsResponseDto.toMeal(): Meal {
    return Meal(
        name = name,
        imageUrl = imageUrl,
        cookingTime = 0,
        servingPeople = 0,
        category = category,
        cookingDifficulty = "",
        ingredients = ingredients.map { it.toIngredient() },
        cookingDirections = cookingInstructions.map { it.instruction },
        favorite = false,
        mealId = id
    )
}

internal fun MealDetailsResponseDto.IngredientDto.toIngredient() = com.kanyideveloper.core.model.Ingredient(
    name = name,
    quantity = quantity,
    id = id
)

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
package com.joelkanyi.data.mapper

import com.kanyideveloper.core.model.MealDetails
import com.kanyideveloper.core_database.model.OnlineMealCategoryEntity
import com.kanyideveloper.core_database.model.OnlineMealEntity
import com.kanyideveloper.core_network.model.CategoriesResponseDto
import com.kanyideveloper.core_network.model.MealDetailsResponseDto
import com.kanyideveloper.core_network.model.MealsResponseDto
import com.kanyideveloper.core.model.Category
import com.kanyideveloper.core.model.Meal

internal fun CategoriesResponseDto.toEntity() = OnlineMealCategoryEntity(
    id = id,
    name = name,
)

internal fun OnlineMealCategoryEntity.toCategory() = Category(
    categoryId = id,
    categoryName = name,
)

internal fun MealsResponseDto.toEntity() = OnlineMealEntity(
    idMeal = id,
    strMeal = name,
    strMealThumb = image,
    strCategory = category
)

internal fun OnlineMealEntity.toMeal() = Meal(
    name = strMeal,
    imageUrl = strMealThumb,
    mealId = idMeal,
    category = strCategory
)

internal fun MealDetailsResponseDto.toMeal(): MealDetails {
    return MealDetails(
        name = name,
        imageUrl = imageUrl,
        cookingTime = cookingTime,
        servingPeople = serving,
        category = category,
        cookingDifficulty = cookingDifficulty,
        ingredients = ingredients.map { it.toIngredient() },
        cookingDirections = cookingInstructions.map { it.instruction },
        mealId = id,
        calories = calories,
        description = description,
        recipePrice = recipePrice,
        reviews = reviewDtos.map { it.toReview() },
        youtubeUrl = youtubeUrl,
        serving = serving,
        mealPlanId = null,
    )
}

internal fun MealDetailsResponseDto.IngredientDto.toIngredient() =
    com.kanyideveloper.core.model.Ingredient(
        name = name,
        quantity = quantity,
        id = id
    )

internal fun MealDetailsResponseDto.ReviewDto.toReview() = com.kanyideveloper.core.model.Review(
    comment = comment,
    rating = rating,
    user = user.toUser(),
    id = id
)

internal fun MealDetailsResponseDto.ReviewDto.UserDto.toUser() = com.kanyideveloper.core.model.User(
    email = email,
    firstName = firstName,
    id = id,
    lastName = lastName
)

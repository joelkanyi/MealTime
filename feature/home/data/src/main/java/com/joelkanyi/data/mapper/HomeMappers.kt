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

import com.joelkanyi.common.model.Category
import com.joelkanyi.common.model.Ingredient
import com.joelkanyi.common.model.Meal
import com.joelkanyi.common.model.MealDetails
import com.joelkanyi.common.model.Review
import com.joelkanyi.common.model.User
import com.joelkanyi.database.model.OnlineMealCategoryEntity
import com.joelkanyi.database.model.OnlineMealEntity
import com.joelkanyi.network.model.MealDetailsResponseDto

internal fun com.joelkanyi.network.model.CategoriesResponseDto.toEntity() = OnlineMealCategoryEntity(
    id = id,
    name = name,
)

internal fun OnlineMealCategoryEntity.toCategory() = Category(
    categoryId = id,
    categoryName = name,
)

internal fun com.joelkanyi.network.model.MealsResponseDto.toEntity() = OnlineMealEntity(
    idMeal = id,
    strMeal = name,
    strMealThumb = image,
    strCategory = category
)

internal fun com.joelkanyi.database.model.OnlineMealEntity.toMeal() = Meal(
    name = strMeal,
    imageUrl = strMealThumb,
    mealId = idMeal,
    category = strCategory
)

internal fun com.joelkanyi.network.model.MealDetailsResponseDto.toMeal(): MealDetails {
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
    Ingredient(
        name = name,
        quantity = quantity,
        id = id
    )

internal fun MealDetailsResponseDto.ReviewDto.toReview() = Review(
    comment = comment,
    rating = rating,
    user = user.toUser(),
    id = id
)

internal fun MealDetailsResponseDto.ReviewDto.UserDto.toUser() = User(
    email = email,
    firstName = firstName,
    id = id,
    lastName = lastName
)

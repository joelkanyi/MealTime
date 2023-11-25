/*
 * Copyright 2023 Joel Kanyi.
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

import com.joelkanyi.domain.entity.MealPlan
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core_database.model.MealEntity
import com.kanyideveloper.core_database.model.MealPlanEntity
import com.kanyideveloper.core_network.model.MealsResponseDto

internal fun MealPlan.toEntity(): MealPlanEntity {
    return MealPlanEntity(
        mealTypeName = mealTypeName,
        meals = meals,
        mealDate = date,
    )
}

internal fun MealPlanEntity.toMealPlan(): MealPlan {
    return MealPlan(
        mealTypeName = mealTypeName,
        meals = meals,
        date = mealDate,
        id = id.toString()
    )
}

internal fun MealsResponseDto.toOnlineMeal(): Meal {
    return Meal(
        name = name,
        imageUrl = image,
        mealId = id,
        category = category
    )
}

internal fun Meal.toGeneralMeal(): Meal {
    return Meal(
        name = name,
        imageUrl = imageUrl,
        mealId = mealId,
        category = category
    )
}

internal fun MealEntity.toMeal(): Meal {
    return Meal(
        name = name,
        imageUrl = imageUrl,
        category = category,
        mealId = mealId,
    )
}

/*
internal fun MealDetailsResponse.Meal.toMeal(): Meal {
    return Meal(
        name = strMeal,
        imageUrl = strMealThumb,
        cookingTime = 0,
        category = strCategory,
        cookingDifficulty = "",
        ingredients = listOf(
            strIngredient1,
            strIngredient2,
            strIngredient3,
            strIngredient4,
            strIngredient5,
            strIngredient6,
            strIngredient7,
            strIngredient8,
            strIngredient9,
            strIngredient10,
            strIngredient11,
            strIngredient12,
            strIngredient13,
            strIngredient14,
            strIngredient15,
            strIngredient16,
            strIngredient17,
            strIngredient18,
            strIngredient19,
            strIngredient20
        ).filter { !it.isNullOrEmpty() },
        cookingDirections = strInstructions.stringToList(),
        favorite = false,
        servingPeople = 0,
        onlineMealId = idMeal
    )
}
*/

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

import com.joelkanyi.common.model.Meal
import com.joelkanyi.domain.entity.MealPlan

internal fun MealPlan.toEntity(): com.joelkanyi.database.model.MealPlanEntity {
    return com.joelkanyi.database.model.MealPlanEntity(
        mealTypeName = mealTypeName,
        meals = meals,
        mealDate = date,
    )
}

internal fun com.joelkanyi.database.model.MealPlanEntity.toMealPlan(): MealPlan {
    return MealPlan(
        mealTypeName = mealTypeName,
        meals = meals,
        date = mealDate,
        id = id.toString()
    )
}

internal fun com.joelkanyi.network.model.MealsResponseDto.toOnlineMeal(): Meal {
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

internal fun com.joelkanyi.database.model.MealEntity.toMeal(): Meal {
    return Meal(
        name = name,
        imageUrl = imageUrl,
        category = category,
        mealId = mealId,
    )
}
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
package com.kanyideveloper.mealplanner.domain.repository

import androidx.lifecycle.LiveData
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.model.MealPlanPreference
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.mealplanner.model.MealPlan
import kotlinx.coroutines.flow.Flow

interface MealPlannerRepository {
    val hasMealPlanPref: Flow<MealPlanPreference?>

    suspend fun saveMealPlannerPreferences(
        allergies: List<String>,
        numberOfPeople: String,
        dishTypes: List<String>
    )

    fun getMealsInMyPlan(filterDay: String): LiveData<List<MealPlan>>

    fun getExistingMeals(mealType: String, date: String): List<Meal>

    suspend fun deleteAMealFromPlan(id: Int)

    suspend fun saveMealToPlan(mealPlan: MealPlan)

    suspend fun searchMeal(source: String, searchBy: String, searchString: String): Resource<LiveData<List<Meal>>>

    suspend fun removeOnlineMealFromPlan(onlineMealId: Int, mealType: String)

    suspend fun removeLocalMealFromPlan(localMealId: String, mealType: String)

    suspend fun getAllIngredients(): Resource<List<String>>
}

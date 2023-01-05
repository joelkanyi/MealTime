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
package com.kanyideveloper.mealplanner.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kanyideveloper.core.data.MealTimePreferences
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.model.MealPlanPreference
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.core_database.dao.MealPlanDao
import com.kanyideveloper.mealplanner.data.mapper.toEntity
import com.kanyideveloper.mealplanner.data.mapper.toMealPlan
import com.kanyideveloper.mealplanner.domain.repository.MealPlannerRepository
import com.kanyideveloper.mealplanner.model.MealPlan
import kotlinx.coroutines.flow.Flow

class MealPlannerRepositoryImpl(
    private val mealTimePreferences: MealTimePreferences,
    private val mealPlanDao: MealPlanDao
) : MealPlannerRepository {

    override suspend fun saveMealToPlan(mealPlan: MealPlan) {
        mealPlanDao.insertMealPlan(mealPlanEntity = mealPlan.toEntity())
    }

    override fun searchMeal(source: String, searchBy: String): Resource<List<Meal>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveMealPlannerPreferences(
        allergies: List<String>,
        numberOfPeople: String,
        dishTypes: List<String>
    ) {
        mealTimePreferences.saveMealPlanPreferences(
            allergies = allergies,
            numberOfPeople = numberOfPeople,
            dishTypes = dishTypes
        )
    }

    override val hasMealPlanPref: Flow<MealPlanPreference?>
        get() = mealTimePreferences.mealPlanPreferences

    override fun getMealsInMyPlan(filterDay: String): LiveData<List<MealPlan>> {
        return Transformations.map(mealPlanDao.getPlanMeals(filterDay = filterDay)) { meals ->
            meals.map { it.toMealPlan() }
        }
    }

    override fun getExistingMeals(mealType: String, date: String): List<Meal> {
        return emptyList()
        // mealPlanDao.getExistingMeals(mealType = mealType, date = date)
    }

    override suspend fun deleteAMealFromPlan(id: Int) {
        mealPlanDao.deleteAMealFromPlan(id = id)
    }
}

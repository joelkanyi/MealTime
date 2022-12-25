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
package com.kanyideveloper.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kanyideveloper.core.domain.HomeRepository
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core_database.dao.MealDao
import com.kanyideveloper.data.mapper.toMeal

class HomeRepositoryImpl(
    private val mealDao: MealDao
) : HomeRepository {
    override fun getMyMeals(): LiveData<List<Meal>> {
        return Transformations.map(mealDao.getAllMeals()) { mealEntities ->
            mealEntities.map { it.toMeal() }
        }
    }

    override fun getMealById(id: Int): LiveData<Meal?> {
        return Transformations.map(mealDao.getSingleMeal(id = id)) { mealEntity ->
            mealEntity?.toMeal()
        }
    }
}

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
package com.kanyideveloper.core_database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.kanyideveloper.core_database.model.MealEntity

@Dao
interface MealDao {

    @Insert
    suspend fun insertMeal(mealEntity: MealEntity)

    @Query("SELECT * FROM meal_table ORDER BY id DESC")
    fun getAllMeals(): LiveData<List<MealEntity>>

    @Query("SELECT * FROM meal_table WHERE id = :id")
    fun getSingleMeals(id: Int): LiveData<List<MealEntity>>

    @Delete
    suspend fun deleteMeal(mealEntity: MealEntity)
}

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
package com.kanyideveloper.core_database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.gson.Gson
import com.kanyideveloper.core_database.LivedataTestUtil.getValue
import com.kanyideveloper.core_database.converters.Converters
import com.kanyideveloper.core_database.dao.MealDao
import com.kanyideveloper.core_database.model.MealEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MealDaoTest {
    private lateinit var mealDao: MealDao
    private lateinit var db: MealTimeDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val gson = Gson()
        val converters = Converters(gson)

        db = Room.inMemoryDatabaseBuilder(
            context,
            MealTimeDatabase::class.java
        )
            .addTypeConverter(converters)
            .allowMainThreadQueries()
            .build()

        mealDao = db.mealDao
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun mealDao_delete_meal_by_id() = runBlocking {
        val testMeal1 = MealEntity(
            name = "",
            imageUrl = "",
            cookingTime = 0,
            category = "",
            cookingDifficulty = "",
            ingredients = listOf(),
            cookingDirections = listOf(),
            isFavorite = false,
            servingPeople = 0
        )

        val testMeal2 = MealEntity(
            name = "",
            imageUrl = "",
            cookingTime = 0,
            category = "",
            cookingDifficulty = "",
            ingredients = listOf(),
            cookingDirections = listOf(),
            isFavorite = false,
            servingPeople = 0
        )

        val expectedResult = 1

        mealDao.insertMeal(testMeal1)
        mealDao.insertMeal(testMeal2)

        mealDao.deleteMealById(1)

        val liveData: LiveData<List<MealEntity>> = mealDao.getAllMeals()
        val data: List<MealEntity>? = getValue(liveData)

        assertEquals(expectedResult, data?.size)
    }

    @get:Rule
    val instantTaskExecutorRule2 = InstantTaskExecutorRule()

    @Test
    fun mealDao_get_meal_by_id() = runBlocking {
        val testMeal1 = MealEntity(
            name = "Ugali",
            imageUrl = "",
            cookingTime = 0,
            category = "",
            cookingDifficulty = "",
            ingredients = listOf(),
            cookingDirections = listOf(),
            isFavorite = false,
            servingPeople = 0
        )

        val testMeal2 = MealEntity(
            name = "",
            imageUrl = "",
            cookingTime = 0,
            category = "",
            cookingDifficulty = "",
            ingredients = listOf(),
            cookingDirections = listOf(),
            isFavorite = false,
            servingPeople = 0
        )

        val expectedMealName = "Ugali"

        mealDao.insertMeal(testMeal1)
        mealDao.insertMeal(testMeal2)

        val liveData: LiveData<MealEntity> = mealDao.getSingleMeal(1)
        val data: MealEntity? = getValue(liveData)

        assertEquals(expectedMealName, data?.name)
    }
}

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
import com.kanyideveloper.core_database.dao.FavoritesDao
import com.kanyideveloper.core_database.model.FavoriteEntity
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class FavoritesDaoTest {
    private lateinit var favoritesDao: FavoritesDao
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

        favoritesDao = db.favoritesDao
    }

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun favoritesDao_delete_online_meal_by_id() = runBlocking {
        val testMeal1 = FavoriteEntity(
            onlineMealId = "21",
            localMealId = null,
            isOnline = false,
            mealName = "",
            mealImageUrl = "",
            isFavorite = false
        )

        val testMeal2 = FavoriteEntity(
            onlineMealId = "22",
            localMealId = null,
            isOnline = false,
            mealName = "",
            mealImageUrl = "",
            isFavorite = false
        )

        val expectedResult = 1

        favoritesDao.insertAFavorite(testMeal1)
        favoritesDao.insertAFavorite(testMeal2)

        favoritesDao.deleteAnOnlineFavorite("21")

        val liveData: LiveData<List<FavoriteEntity>> = favoritesDao.getFavorites()
        val data: List<FavoriteEntity>? = getValue(liveData)

        assertEquals(expectedResult, data?.size)
    }

    @get:Rule
    val instantTaskExecutorRule1 = InstantTaskExecutorRule()

    @Test
    fun favoritesDao_delete_a_local_meal_by_id() = runBlocking {
        val testMeal1 = FavoriteEntity(
            onlineMealId = null,
            localMealId = 32,
            isOnline = false,
            mealName = "",
            mealImageUrl = "",
            isFavorite = false
        )

        val testMeal2 = FavoriteEntity(
            onlineMealId = null,
            localMealId = 33,
            isOnline = false,
            mealName = "",
            mealImageUrl = "",
            isFavorite = false
        )

        val expectedResult = 1

        favoritesDao.insertAFavorite(testMeal1)
        favoritesDao.insertAFavorite(testMeal2)

        favoritesDao.deleteALocalFavorite(33)

        val liveData: LiveData<List<FavoriteEntity>> = favoritesDao.getFavorites()
        val data: List<FavoriteEntity>? = getValue(liveData)

        assertEquals(expectedResult, data?.size)
    }

    @get:Rule
    val instantTaskExecutorRule2 = InstantTaskExecutorRule()

    @Test
    fun favoritesDao_a_given_local_meal_is_in_favorites() = runBlocking {
        val testMeal1 = FavoriteEntity(
            onlineMealId = null,
            localMealId = 32,
            isOnline = false,
            mealName = "",
            mealImageUrl = "",
            isFavorite = false
        )

        val testMeal2 = FavoriteEntity(
            onlineMealId = null,
            localMealId = 33,
            isOnline = false,
            mealName = "",
            mealImageUrl = "",
            isFavorite = false
        )

        val testMeal3 = FavoriteEntity(
            onlineMealId = null,
            localMealId = 34,
            isOnline = false,
            mealName = "",
            mealImageUrl = "",
            isFavorite = false
        )

        val expectedResult = true

        favoritesDao.insertAFavorite(testMeal1)
        favoritesDao.insertAFavorite(testMeal2)
        favoritesDao.insertAFavorite(testMeal3)

        val liveData: LiveData<Boolean> = favoritesDao.localInFavorites(id = 34)
        val data: Boolean = getValue(liveData) == true

        assertEquals(expectedResult, data)
    }

    @get:Rule
    val instantTaskExecutorRule3 = InstantTaskExecutorRule()

    @Test
    fun favoritesDao_a_given_online_meal_is_in_favorites() = runBlocking {
        val testMeal1 = FavoriteEntity(
            onlineMealId = "43",
            localMealId = null,
            isOnline = false,
            mealName = "",
            mealImageUrl = "",
            isFavorite = false
        )

        val testMeal2 = FavoriteEntity(
            onlineMealId = "56",
            localMealId = null,
            isOnline = false,
            mealName = "",
            mealImageUrl = "",
            isFavorite = false
        )

        val testMeal3 = FavoriteEntity(
            onlineMealId = "10",
            localMealId = null,
            isOnline = false,
            mealName = "",
            mealImageUrl = "",
            isFavorite = false
        )

        val expectedResult = true

        favoritesDao.insertAFavorite(testMeal1)
        favoritesDao.insertAFavorite(testMeal2)
        favoritesDao.insertAFavorite(testMeal3)

        val liveData: LiveData<Boolean> = favoritesDao.onlineInFavorites(id = "10")
        val data: Boolean = getValue(liveData) == true

        assertEquals(expectedResult, data)
    }
}

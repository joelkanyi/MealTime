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
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.kanyideveloper.core.data.MealTimePreferences
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.model.MealPlanPreference
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.core.util.safeApiCall
import com.kanyideveloper.core_database.dao.FavoritesDao
import com.kanyideveloper.core_database.dao.MealDao
import com.kanyideveloper.core_database.dao.MealPlanDao
import com.kanyideveloper.core_network.MealDbApi
import com.kanyideveloper.mealplanner.data.mapper.toEntity
import com.kanyideveloper.mealplanner.data.mapper.toGeneralMeal
import com.kanyideveloper.mealplanner.data.mapper.toMeal
import com.kanyideveloper.mealplanner.data.mapper.toMealPlan
import com.kanyideveloper.mealplanner.data.mapper.toOnlineMeal
import com.kanyideveloper.mealplanner.domain.repository.MealPlannerRepository
import com.kanyideveloper.mealplanner.model.MealPlan
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

class MealPlannerRepositoryImpl(
    private val mealTimePreferences: MealTimePreferences,
    private val mealPlanDao: MealPlanDao,
    private val favoritesDao: FavoritesDao,
    private val mealDao: MealDao,
    private val mealDbApi: MealDbApi
) : MealPlannerRepository {

    override suspend fun saveMealToPlan(mealPlan: MealPlan) {
        mealPlanDao.insertMealPlan(mealPlanEntity = mealPlan.toEntity())
    }

    override suspend fun searchMeal(
        source: String,
        searchBy: String,
        searchString: String
    ): Resource<LiveData<List<Meal>>> {
        return when (source) {
            "Online" -> {
                when (searchBy) {
                    "Name" -> {
                        safeApiCall(Dispatchers.IO) {
                            val response = mealDbApi.searchMealsByName(query = searchString)
                            val mealsList = response.meals.map { it.toOnlineMeal().toGeneralMeal() }

                            val liveData = MutableLiveData<List<Meal>>()
                            liveData.postValue(mealsList)
                            liveData
                        }
                    }
                    "Ingredient" -> {
                        safeApiCall(Dispatchers.IO) {
                            val response = mealDbApi.searchMealsByIngredient(query = searchString)
                            val mealsList = response.meals.map { it.toOnlineMeal().toGeneralMeal() }

                            val liveData = MutableLiveData<List<Meal>>()
                            liveData.postValue(mealsList)
                            liveData
                        }
                    }
                    "Category" -> {
                        safeApiCall(Dispatchers.IO) {
                            val response = mealDbApi.searchMealsByCategory(query = searchString)
                            val mealsList = response.meals.map { it.toOnlineMeal().toGeneralMeal() }

                            val liveData = MutableLiveData<List<Meal>>()
                            liveData.postValue(mealsList)
                            liveData
                        }
                    }
                    else -> {
                        Resource.Error("Unknown online search by")
                    }
                }
            }
            "My Meals" -> {
                val a = Transformations.map(mealDao.getAllMeals()) { it ->
                    it.map { it.toMeal() }
                }
                Resource.Success(a)
            }
            "My Favorites" -> {
                val meals = Transformations.map(favoritesDao.getFavorites()) { favs ->
                    favs.map { it.toMeal() }
                }

                Resource.Success(meals)
            }
            else -> {
                Resource.Error("Invalid source: $source", null)
            }
        }
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
    }

    override suspend fun deleteAMealFromPlan(id: Int) {
        mealPlanDao.deleteAMealFromPlan(id = id)
    }
}

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
package com.joelkanyi.data.repository

import com.joelkanyi.common.model.Category
import com.joelkanyi.common.model.Meal
import com.joelkanyi.common.model.MealDetails
import com.joelkanyi.common.util.Resource
import com.joelkanyi.common.util.safeApiCall
import com.joelkanyi.data.mapper.toCategory
import com.joelkanyi.data.mapper.toEntity
import com.joelkanyi.data.mapper.toMeal
import com.joelkanyi.domain.repository.MealsRepository
import kotlinx.coroutines.Dispatchers
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MealsRepositoryImpl @Inject constructor(
    private val mealDbApi: com.joelkanyi.network.MealDbApi,
    private val onlineMealsDao: com.joelkanyi.database.dao.OnlineMealsDao,
) : MealsRepository {

    override suspend fun getMealCategories(): Resource<List<Category>> {
        val cachedCategories = onlineMealsDao.getOnlineMealCategories().map { it.toCategory() }
        return try {
            val response = mealDbApi.getCategories()
            onlineMealsDao.deleteOnlineMealCategories()
            onlineMealsDao.insertOnlineMealCategories(response.map { it.toEntity() })
            Resource.Success(
                data = onlineMealsDao.getOnlineMealCategories().map { it.toCategory() }
            )
        } catch (e: IOException) {
            return Resource.Error(
                "Couldn't reach the server. Check your internet connection",
                data = cachedCategories
            )
        } catch (e: HttpException) {
            return Resource.Error("Server error occurred", data = cachedCategories)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred", data = cachedCategories)
        }
    }

    override suspend fun getMeals(category: String): Resource<List<Meal>> {
        val cachedMeals = onlineMealsDao.getOnlineMeals().map { it.toMeal() }
        return try {
            val response = mealDbApi.getMeals()
            onlineMealsDao.deleteOnlineMeals()
            onlineMealsDao.insertOnlineMeals(response.map { it.toEntity() })
            Resource.Success(data = onlineMealsDao.getOnlineMeals().map { it.toMeal() })
        } catch (e: IOException) {
            return Resource.Error(
                "Couldn't reach the server. Check your internet connection",
                data = cachedMeals
            )
        } catch (e: HttpException) {
            return Resource.Error("Server error occurred", data = cachedMeals)
        } catch (e: Exception) {
            return Resource.Error("An unknown error occurred", data = cachedMeals)
        }
    }

    override suspend fun getMealDetails(mealId: String): Resource<MealDetails> {
        return safeApiCall(Dispatchers.IO) {
            val response = mealDbApi.getMealDetails(mealId = mealId)
            response.toMeal()
        }
    }

    override suspend fun getRandomMeal(): Resource<MealDetails> {
        return safeApiCall(Dispatchers.IO) {
            val response = mealDbApi.getRandomMeal()
            response.toMeal()
        }
    }
}

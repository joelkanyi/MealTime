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
package com.kanyideveloper.search.data.repository

import com.kanyideveloper.core.model.OnlineMeal
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.core.util.safeApiCall
import com.kanyideveloper.core_network.MealDbApi
import com.kanyideveloper.search.data.mapper.toOnlineMeal
import com.kanyideveloper.search.domain.SearchRepository
import kotlinx.coroutines.Dispatchers
import timber.log.Timber

class SearchRepositoryImpl(
    private val mealDbApi: MealDbApi
) : SearchRepository {
    override suspend fun search(
        searchOption: String,
        searchParam: String
    ): Resource<List<OnlineMeal>> {
        return when (searchOption) {
            "Meal Name" -> {
                return safeApiCall(Dispatchers.IO) {
                    val response = mealDbApi.searchMeals(name = searchParam)
                    Timber.e("Response for meal name: $response")
                    response.map { it.toOnlineMeal() } ?: emptyList()
                }
            }
            "Ingredient" -> {
                return safeApiCall(Dispatchers.IO) {
                    val response = mealDbApi.searchMeals(ingredient = searchParam)

                    response.map { it.toOnlineMeal() } ?: emptyList()
                }
            }
            "Meal Category" -> {
                return safeApiCall(Dispatchers.IO) {
                    val response = mealDbApi.searchMeals(category = searchParam)

                    response.map { it.toOnlineMeal() } ?: emptyList()
                }
            }
            else -> {
                Resource.Error("Unknown online search by")
            }
        }
    }
}

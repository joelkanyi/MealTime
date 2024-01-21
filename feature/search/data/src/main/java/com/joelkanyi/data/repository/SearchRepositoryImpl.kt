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
package com.joelkanyi.data.repository

import com.joelkanyi.common.model.Meal
import com.joelkanyi.common.util.Resource
import com.joelkanyi.data.mapper.toOnlineMeal
import com.joelkanyi.domain.repository.SearchRepository
import com.joelkanyi.network.utils.safeApiCall
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val mealtimeApiService: com.joelkanyi.network.MealtimeApiService
) : SearchRepository {
    override suspend fun search(
        searchOption: String,
        searchParam: String
    ): Resource<List<Meal>> {
        return when (searchOption) {
            "Meal Name" -> {
                return safeApiCall {
                    val response = mealtimeApiService.searchMeals(name = searchParam)
                    response.map { it.toOnlineMeal() }
                }
            }

            "Ingredient" -> {
                return safeApiCall {
                    val response = mealtimeApiService.searchMeals(ingredient = searchParam)
                    response.map { it.toOnlineMeal() }
                }
            }

            "Meal Category" -> {
                return safeApiCall {
                    val response = mealtimeApiService.searchMeals(category = searchParam)
                    response.map { it.toOnlineMeal() }
                }
            }

            else -> {
                Resource.Error("Unknown online search by")
            }
        }
    }
}

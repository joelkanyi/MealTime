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
package com.kanyideveloper.core_network

import com.kanyideveloper.core_network.model.CategoriesResponseDto
import com.kanyideveloper.core_network.model.CreateFavoriteRequestDto
import com.kanyideveloper.core_network.model.FavoritesResponseDto
import com.kanyideveloper.core_network.model.IngredientsResponseDto
import com.kanyideveloper.core_network.model.LoginResponse
import com.kanyideveloper.core_network.model.MealDetailsResponseDto
import com.kanyideveloper.core_network.model.MealsResponseDto
import com.kanyideveloper.core_network.model.RefreshTokenRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MealDbApi {
    @GET("categories")
    suspend fun getCategories(): List<CategoriesResponseDto>

    @GET("meals")
    suspend fun getMeals(): List<MealsResponseDto>

    @GET("meals/{mealId}")
    suspend fun getMealDetails(
        @Path("mealId") mealId: String
    ): MealDetailsResponseDto

    @GET("meals/search")
    suspend fun searchMeals(
        @Query("category") category: String? = null,
        @Query("name") name: String? = null,
        @Query("ingredient") ingredient: String? = null
    ): List<MealsResponseDto>

    @GET("meals/random")
    suspend fun getRandomMeal(): MealDetailsResponseDto

    @GET("meals/ingredients")
    suspend fun getAllIngredients(): List<IngredientsResponseDto>

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body refreshTokenRequestDto: RefreshTokenRequestDto
    ): Response<LoginResponse>

    @GET("favorite/{userId}")
    suspend fun getFavorites(
        @Path("userId") userId: String
    ): List<FavoritesResponseDto>

    @POST("favorite")
    suspend fun saveFavorite(
        @Body favorite: CreateFavoriteRequestDto
    )

    @DELETE("favorite/{mealId}/{userId}")
    suspend fun deleteFavorite(
        @Path("mealId") mealId: String,
        @Path("userId") userId: String
    )
}

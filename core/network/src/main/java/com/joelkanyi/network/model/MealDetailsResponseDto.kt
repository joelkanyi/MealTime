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
package com.joelkanyi.network.model

import com.google.gson.annotations.SerializedName

data class MealDetailsResponseDto(
    @SerializedName("calories")
    val calories: Double?,
    @SerializedName("category")
    val category: String,
    @SerializedName("cookingDifficulty")
    val cookingDifficulty: String?,
    @SerializedName("cookingInstructions")
    val cookingInstructions: List<CookingInstructionDto>,
    @SerializedName("cookingTime")
    val cookingTime: Int?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: String,
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("ingredients")
    val ingredients: List<IngredientDto>,
    @SerializedName("name")
    val name: String,
    @SerializedName("recipePrice")
    val recipePrice: Double?,
    @SerializedName("reviewDtos")
    val reviewDtos: List<ReviewDto>,
    @SerializedName("serving")
    val serving: Int?,
    @SerializedName("youtubeUrl")
    val youtubeUrl: String?
) {
    data class CookingInstructionDto(
        @SerializedName("id")
        val id: Int,
        @SerializedName("instruction")
        val instruction: String
    )

    data class IngredientDto(
        @SerializedName("id")
        val id: Int,
        @SerializedName("name")
        val name: String,
        @SerializedName("quantity")
        val quantity: String
    )

    data class ReviewDto(
        @SerializedName("comment")
        val comment: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("rating")
        val rating: Int,
        @SerializedName("user")
        val user: UserDto
    ) {
        data class UserDto(
            @SerializedName("email")
            val email: String,
            @SerializedName("firstName")
            val firstName: String,
            @SerializedName("id")
            val id: String,
            @SerializedName("lastName")
            val lastName: String
        )
    }
}

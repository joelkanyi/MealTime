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

data class CreateMealRequestDto(
    @SerializedName("calories")
    val calories: Int?,
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
    @SerializedName("imageUrl")
    val imageUrl: String,
    @SerializedName("ingredients")
    val ingredients: List<IngredientDto>,
    @SerializedName("name")
    val name: String,
    @SerializedName("recipePrice")
    val recipePrice: Double?,
    @SerializedName("serving")
    val serving: Int?,
    @SerializedName("userId")
    val userId: String,
    @SerializedName("youtubeUrl")
    val youtubeUrl: String?
) {
    data class CookingInstructionDto(
        @SerializedName("instruction")
        val instruction: String
    )

    data class IngredientDto(
        @SerializedName("name")
        val name: String,
        @SerializedName("quantity")
        val quantity: String?
    )
}

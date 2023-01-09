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
package com.kanyideveloper.core_network.model

import com.google.gson.annotations.SerializedName

data class MealDetailsResponse(
    @SerializedName("meals")
    val meals: List<Meal>
) {
    data class Meal(
        @SerializedName("dateModified")
        val dateModified: Any,
        @SerializedName("idMeal")
        val idMeal: String,
        @SerializedName("strArea")
        val strArea: String,
        @SerializedName("strCategory")
        val strCategory: String,
        @SerializedName("strCreativeCommonsConfirmed")
        val strCreativeCommonsConfirmed: Any,
        @SerializedName("strDrinkAlternate")
        val strDrinkAlternate: Any,
        @SerializedName("strImageSource")
        val strImageSource: Any,
        @SerializedName("strIngredient1")
        val strIngredient1: String,
        @SerializedName("strIngredient10")
        val strIngredient10: String,
        @SerializedName("strIngredient11")
        val strIngredient11: String,
        @SerializedName("strIngredient12")
        val strIngredient12: String,
        @SerializedName("strIngredient13")
        val strIngredient13: String,
        @SerializedName("strIngredient14")
        val strIngredient14: String,
        @SerializedName("strIngredient15")
        val strIngredient15: String,
        @SerializedName("strIngredient16")
        val strIngredient16: String,
        @SerializedName("strIngredient17")
        val strIngredient17: String,
        @SerializedName("strIngredient18")
        val strIngredient18: String,
        @SerializedName("strIngredient19")
        val strIngredient19: String,
        @SerializedName("strIngredient2")
        val strIngredient2: String,
        @SerializedName("strIngredient20")
        val strIngredient20: String,
        @SerializedName("strIngredient3")
        val strIngredient3: String,
        @SerializedName("strIngredient4")
        val strIngredient4: String,
        @SerializedName("strIngredient5")
        val strIngredient5: String,
        @SerializedName("strIngredient6")
        val strIngredient6: String,
        @SerializedName("strIngredient7")
        val strIngredient7: String,
        @SerializedName("strIngredient8")
        val strIngredient8: String,
        @SerializedName("strIngredient9")
        val strIngredient9: String,
        @SerializedName("strInstructions")
        val strInstructions: String,
        @SerializedName("strMeal")
        val strMeal: String,
        @SerializedName("strMealThumb")
        val strMealThumb: String,
        @SerializedName("strMeasure1")
        val strMeasure1: String,
        @SerializedName("strMeasure10")
        val strMeasure10: String,
        @SerializedName("strMeasure11")
        val strMeasure11: String,
        @SerializedName("strMeasure12")
        val strMeasure12: String,
        @SerializedName("strMeasure13")
        val strMeasure13: String,
        @SerializedName("strMeasure14")
        val strMeasure14: String,
        @SerializedName("strMeasure15")
        val strMeasure15: String,
        @SerializedName("strMeasure16")
        val strMeasure16: String,
        @SerializedName("strMeasure17")
        val strMeasure17: String,
        @SerializedName("strMeasure18")
        val strMeasure18: String,
        @SerializedName("strMeasure19")
        val strMeasure19: String,
        @SerializedName("strMeasure2")
        val strMeasure2: String,
        @SerializedName("strMeasure20")
        val strMeasure20: String,
        @SerializedName("strMeasure3")
        val strMeasure3: String,
        @SerializedName("strMeasure4")
        val strMeasure4: String,
        @SerializedName("strMeasure5")
        val strMeasure5: String,
        @SerializedName("strMeasure6")
        val strMeasure6: String,
        @SerializedName("strMeasure7")
        val strMeasure7: String,
        @SerializedName("strMeasure8")
        val strMeasure8: String,
        @SerializedName("strMeasure9")
        val strMeasure9: String,
        @SerializedName("strSource")
        val strSource: String,
        @SerializedName("strTags")
        val strTags: String,
        @SerializedName("strYoutube")
        val strYoutube: String
    )
}

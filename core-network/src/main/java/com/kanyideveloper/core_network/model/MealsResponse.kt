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

data class MealsResponse(
    @SerializedName("meals")
    val meals: List<Meal>
) {
    data class Meal(
        @SerializedName("idMeal")
        val idMeal: String,
        @SerializedName("strMeal")
        val strMeal: String,
        @SerializedName("strMealThumb")
        val strMealThumb: String
    )
}

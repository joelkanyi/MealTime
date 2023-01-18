package com.kanyideveloper.core_network.model

import com.google.gson.annotations.SerializedName

data class IngredientsResponse(
    @SerializedName("meals")
    val meals: List<Meal>
) {
    data class Meal(
        @SerializedName("idIngredient")
        val idIngredient: String,
        @SerializedName("strDescription")
        val strDescription: String,
        @SerializedName("strIngredient")
        val strIngredient: String,
        @SerializedName("strType")
        val strType: String
    )
}

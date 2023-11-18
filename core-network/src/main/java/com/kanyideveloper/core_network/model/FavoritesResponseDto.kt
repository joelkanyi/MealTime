package com.kanyideveloper.core_network.model


import com.google.gson.annotations.SerializedName

data class FavoritesResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("mealId")
    val mealId: String,
    @SerializedName("mealImageUrl")
    val mealImageUrl: String,
    @SerializedName("mealName")
    val mealName: String
)

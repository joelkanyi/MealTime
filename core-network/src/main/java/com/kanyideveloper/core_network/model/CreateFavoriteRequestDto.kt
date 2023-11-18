package com.kanyideveloper.core_network.model


import com.google.gson.annotations.SerializedName

data class CreateFavoriteRequestDto(
    @SerializedName("mealId")
    val mealId: String,
    @SerializedName("userId")
    val userId: String
)
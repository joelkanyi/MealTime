package com.kanyideveloper.core_network.model


import com.google.gson.annotations.SerializedName

data class MealsResponseDto(
    @SerializedName("category")
    val category: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("image")
    val image: String,
    @SerializedName("name")
    val name: String
)

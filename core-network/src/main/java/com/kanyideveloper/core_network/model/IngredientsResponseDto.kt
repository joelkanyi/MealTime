package com.kanyideveloper.core_network.model


import com.google.gson.annotations.SerializedName

data class IngredientsResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("quantity")
    val quantity: String
)

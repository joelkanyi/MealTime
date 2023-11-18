package com.kanyideveloper.core_network.model


import com.google.gson.annotations.SerializedName

data class CategoriesResponseDto(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String
)

package com.kanyideveloper.addmeal.domain.model

data class Meal(
    val name: String,
    val imageUrl: String,
    val cookingTime: Int,
    val category: String,
    val cookingDifficulty: String,
    val ingredients: List<String>,
    val cookingDirections: List<String>,
    val isFavorite: Boolean = false
)

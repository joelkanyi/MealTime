package com.kanyideveloper.mealtime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kanyideveloper.mealtime.util.Constants.MEAL_TABLE

@Entity(tableName = MEAL_TABLE)
data class Meal(
    val name: String,
    val imageUrl: String,
    val cookingTime: Int,
    val category: String,
    val cookingDifficulty: String,
    val ingredients: List<String>,
    val cookingDirections: List<String>,
    val isFavorite: Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int? = 0
)

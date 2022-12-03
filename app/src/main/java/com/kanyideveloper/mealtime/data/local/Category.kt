package com.kanyideveloper.mealtime.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kanyideveloper.mealtime.util.Constants.CATEGORIES_TABLE

@Entity(tableName = CATEGORIES_TABLE)
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String
)

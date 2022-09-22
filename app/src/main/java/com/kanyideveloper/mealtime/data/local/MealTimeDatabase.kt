package com.kanyideveloper.mealtime.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Meal::class], version = 1)
abstract class MealTimeDatabase: RoomDatabase() {
    abstract val mealDao: MealDao
}
package com.kanyideveloper.mealtime.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.kanyideveloper.mealtime.data.local.converters.Converters

@TypeConverters(Converters::class)
@Database(entities = [Meal::class], version = 1)
abstract class MealTimeDatabase: RoomDatabase() {
    abstract val mealDao: MealDao
}
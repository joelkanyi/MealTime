package com.kanyideveloper.mealtime.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface MealDao {

    @Insert(onConflict = REPLACE)
    suspend fun insertMeal(meal: Meal)

    @Query("SELECT * FROM meal_table ORDER BY id DESC")
    fun getAllMeals(): LiveData<List<Meal>>

    @Query("SELECT * FROM meal_table WHERE id = :id")
    fun getSingleMeals(id: Int): LiveData<List<Meal>>

    @Delete
    suspend fun deleteMeal(meal: Meal)
}
/*
 * Copyright 2023 Joel Kanyi.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kanyideveloper.mealplanner.data.repository

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.asLiveData
import com.kanyideveloper.core.data.MealTimePreferences
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.model.MealPlanPreference
import com.kanyideveloper.core.util.Resource
import com.kanyideveloper.core.util.safeApiCall
import com.kanyideveloper.core_database.dao.FavoritesDao
import com.kanyideveloper.core_database.dao.MealDao
import com.kanyideveloper.core_database.dao.MealPlanDao
import com.kanyideveloper.core_network.MealDbApi
import com.kanyideveloper.mealplanner.data.mapper.toEntity
import com.kanyideveloper.mealplanner.data.mapper.toGeneralMeal
import com.kanyideveloper.mealplanner.data.mapper.toMeal
import com.kanyideveloper.mealplanner.data.mapper.toMealPlan
import com.kanyideveloper.mealplanner.data.mapper.toOnlineMeal
import com.kanyideveloper.mealplanner.domain.repository.MealPlannerRepository
import com.kanyideveloper.mealplanner.model.MealPlan
import com.kanyideveloper.core.notifications.NotificationReceiver
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class MealPlannerRepositoryImpl(
    private val mealTimePreferences: MealTimePreferences,
    private val mealPlanDao: MealPlanDao,
    private val favoritesDao: FavoritesDao,
    private val mealDao: MealDao,
    private val mealDbApi: MealDbApi,
    private val context: Context,
) : MealPlannerRepository {

    override suspend fun saveMealToPlan(mealPlan: MealPlan) {
        mealPlanDao.insertMealPlan(mealPlanEntity = mealPlan.toEntity())
    }

    override suspend fun searchMeal(
        source: String,
        searchBy: String,
        searchString: String,
    ): Resource<LiveData<List<Meal>>> {
        return when (source) {
            "Online" -> {
                when (searchBy) {
                    "Name" -> {
                        safeApiCall(Dispatchers.IO) {
                            val response = mealDbApi.searchMealsByName(query = searchString)
                            val mealsList = response.meals.map { it.toOnlineMeal().toGeneralMeal() }

                            val liveData = MutableLiveData<List<Meal>>()
                            liveData.postValue(mealsList)
                            liveData
                        }
                    }
                    "Ingredient" -> {
                        safeApiCall(Dispatchers.IO) {
                            val response = mealDbApi.searchMealsByIngredient(query = searchString)
                            val mealsList = response.meals.map { it.toOnlineMeal().toGeneralMeal() }

                            val liveData = MutableLiveData<List<Meal>>()
                            liveData.postValue(mealsList)
                            liveData
                        }
                    }
                    "Category" -> {
                        safeApiCall(Dispatchers.IO) {
                            val response = mealDbApi.searchMealsByCategory(query = searchString)
                            val mealsList = response.meals.map { it.toOnlineMeal().toGeneralMeal() }

                            val liveData = MutableLiveData<List<Meal>>()
                            liveData.postValue(mealsList)
                            liveData
                        }
                    }
                    else -> {
                        Resource.Error("Unknown online search by")
                    }
                }
            }
            "My Meals" -> {
                val a = mealDao.getAllMeals().map { meals ->
                    meals.map { it.toMeal() }
                }.asLiveData()

                Resource.Success(a)
            }
            "My Favorites" -> {
                val meals = favoritesDao.getFavorites().map { favs ->
                    favs.map { it.toMeal() }
                }.asLiveData()

                Resource.Success(meals)
            }
            else -> {
                Resource.Error("Invalid source: $source", null)
            }
        }
    }

    override suspend fun removeMealFromPlan(id: Int) {
        mealPlanDao.deleteAMealFromPlan(id = id)
    }

    override suspend fun getAllIngredients(): Resource<List<String>> {
        return safeApiCall(Dispatchers.IO) {
            val response = mealDbApi.getAllIngredients()
            Timber.d("Ingredients response: $response")
            response.meals.map { it.strIngredient }
        }
    }

    override suspend fun saveMealPlannerPreferences(
        allergies: List<String>,
        numberOfPeople: String,
        dishTypes: List<String>,
    ) {
        // Set Alarms
        setAlarm()

        mealTimePreferences.saveMealPlanPreferences(
            allergies = allergies,
            numberOfPeople = numberOfPeople,
            dishTypes = dishTypes
        )
    }

    override val hasMealPlanPref: Flow<MealPlanPreference?>
        get() = mealTimePreferences.mealPlanPreferences

    override fun getMealsInMyPlan(filterDay: String): LiveData<List<MealPlan>> {
        return Transformations.map(mealPlanDao.getPlanMeals(filterDay = filterDay)) { meals ->
            meals.map { it.toMealPlan() }
        }
    }

    override fun getExistingMeals(mealType: String, date: String): List<Meal> {
        return emptyList()
    }

    override suspend fun deleteAMealFromPlan(id: Int) {
        mealPlanDao.deleteAMealFromPlan(id = id)
    }

    override fun setAlarm() {
        CoroutineScope(Dispatchers.Default).launch {
            hasMealPlanPref.collectLatest { mealPlanPreference ->
                mealPlanPreference?.dishTypes?.forEach {
                    if (it == "Breakfast") {
                        setBreakfastAlarm()
                    }
                    if (it == "Lunch") {
                        setLunchAlarm()
                    }

                    if (it == "Dinner") {
                        setDinnerAlarm()
                    }

                    if (it == "Dessert") {
                        setDessertAlarm()
                    }
                }
            }
        }
    }

    private fun setLunchAlarm() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 13)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val alarmManager = context.getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        // Add the desired parameters to the intent
        intent.putExtra("MESSAGE", "Lunch")
        intent.putExtra("DESCRIPTION", "Time to prepare and eat your lunch")

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1,
            intent,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun setBreakfastAlarm() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val alarmManager = context.getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)

        // Add the desired parameters to the intent
        intent.putExtra("MESSAGE", "Breakfast")
        intent.putExtra("DESCRIPTION", "Time to prepare and eat your breakfast")

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            2,
            intent,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun setDinnerAlarm() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 19)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val alarmManager = context.getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)

        // Add the desired parameters to the intent
        intent.putExtra("MESSAGE", "Dinner")
        intent.putExtra("DESCRIPTION", "Time to prepare and eat your dinner")

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            3,
            intent,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun setDessertAlarm() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 20)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val alarmManager = context.getSystemService(ComponentActivity.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java)

        // Add the desired parameters to the intent
        intent.putExtra("MESSAGE", "Dessert")
        intent.putExtra("DESCRIPTION", "Time to prepare and eat your dessert")

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            4,
            intent,
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                PendingIntent.FLAG_IMMUTABLE
            } else {
                PendingIntent.FLAG_UPDATE_CURRENT
            }
        )

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}

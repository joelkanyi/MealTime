package com.kanyideveloper.preferences.domain

import com.kanyideveloper.core.model.MealPlanPreference
import kotlinx.coroutines.flow.Flow

interface MealtimeSettings {
    fun mealPlanPreferences(): Flow<MealPlanPreference?>
    suspend fun saveAllergies(allergies: List<String>)
    suspend fun saveNumberOfPeople(numberOfPeople: String)
    suspend fun saveDishTypes(dishTypes: List<String>)
    suspend fun saveTheme(themeValue: Int)
    fun getTheme(): Flow<Int>
    fun getAccessToken(): Flow<String?>
    suspend fun saveAccessToken(accessToken: String)
    suspend fun deleteAccessToken()
    fun getUserId(): Flow<String?>
    suspend fun saveUserId(userId: String)
}
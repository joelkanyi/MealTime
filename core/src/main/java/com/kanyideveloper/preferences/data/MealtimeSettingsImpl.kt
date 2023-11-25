package com.kanyideveloper.preferences.data

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.kanyideveloper.core.model.MealPlanPreference
import com.kanyideveloper.core.util.Constants
import com.kanyideveloper.preferences.domain.MealtimeSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MealtimeSettingsImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : MealtimeSettings {
    override fun mealPlanPreferences(): Flow<MealPlanPreference?> {
        return dataStore.data.map { preferences ->
            MealPlanPreference(
                numberOfPeople = preferences[Constants.NUMBER_OF_PEOPLE] ?: "0",
                dishTypes = preferences[Constants.DISH_TYPES]?.toList() ?: listOf(""),
                allergies = preferences[Constants.ALLERGIES]?.toList() ?: listOf("")
            )
        }
    }

    override suspend fun saveAllergies(allergies: List<String>) {
        dataStore.edit { preferences ->
            preferences[Constants.ALLERGIES] = allergies.toSet()
        }
    }

    override suspend fun saveNumberOfPeople(numberOfPeople: String) {
        dataStore.edit { preferences ->
            preferences[Constants.NUMBER_OF_PEOPLE] = numberOfPeople
        }
    }

    override suspend fun saveDishTypes(dishTypes: List<String>) {
        dataStore.edit { preferences ->
            preferences[Constants.DISH_TYPES] = dishTypes.toSet()
        }
    }

    override suspend fun saveTheme(themeValue: Int) {
        dataStore.edit { preferences ->
            preferences[Constants.THEME_OPTIONS] = themeValue
        }
    }

    override fun getTheme(): Flow<Int> {
        return dataStore.data.map { preferences ->
            preferences[Constants.THEME_OPTIONS] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }
    }

    override fun getAccessToken(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[Constants.ACCESS_TOKEN]
        }
    }

    override suspend fun saveAccessToken(accessToken: String) {
        dataStore.edit { preferences ->
            preferences[Constants.ACCESS_TOKEN] = accessToken
        }
    }

    override suspend fun deleteAccessToken() {
        dataStore.edit { preferences ->
            preferences.remove(Constants.ACCESS_TOKEN)
        }
    }

    override fun getUserId(): Flow<String?> {
        return dataStore.data.map { preferences ->
            preferences[Constants.USER_ID]
        }
    }

    override suspend fun saveUserId(userId: String) {
        dataStore.edit { preferences ->
            preferences[Constants.USER_ID] = userId
        }
    }
}
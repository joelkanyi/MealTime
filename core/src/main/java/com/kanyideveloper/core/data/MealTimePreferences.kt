/*
 * Copyright 2022 Joel Kanyi.
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
package com.kanyideveloper.core.data

import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.kanyideveloper.core.model.MealPlanPreference
import com.kanyideveloper.core.util.Constants.ALLERGIES
import com.kanyideveloper.core.util.Constants.DISH_TYPES
import com.kanyideveloper.core.util.Constants.NUMBER_OF_PEOPLE
import com.kanyideveloper.core.util.Constants.THEME_OPTIONS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MealTimePreferences(
    private val dataStore: DataStore<Preferences>
) {
    suspend fun saveTheme(themeValue: Int) {
        dataStore.edit { preferences ->
            preferences[THEME_OPTIONS] = themeValue
        }
    }

    suspend fun saveMealPlanPreferences(
        allergies: List<String>,
        numberOfPeople: String,
        dishTypes: List<String>
    ) {
        dataStore.edit { preferences ->
            preferences[ALLERGIES] = allergies.toSet()
            preferences[NUMBER_OF_PEOPLE] = numberOfPeople
            preferences[DISH_TYPES] = dishTypes.toSet()
        }
    }

    val getTheme: Flow<Int> = dataStore.data.map { preferences ->
        preferences[THEME_OPTIONS] ?: AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
    }

    val mealPlanPreferences: Flow<MealPlanPreference?> = dataStore.data.map { preferences ->
        MealPlanPreference(
            numberOfPeople = preferences[NUMBER_OF_PEOPLE] ?: "0",
            dishTypes = preferences[DISH_TYPES]?.toList() ?: listOf(""),
            allergies = preferences[ALLERGIES]?.toList() ?: listOf("")
        )
    }
}

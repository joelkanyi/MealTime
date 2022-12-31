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
package com.kanyideveloper.core.util

import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object Constants {
    const val MEAL_TABLE = "meal_table"
    const val CATEGORIES_TABLE = "categories_table"
    const val MEALTIME_DATABASE = "meal_time_database"
    const val FAVORITES_TABLE = "favorites_table"

    val THEME_OPTIONS = intPreferencesKey(name = "theme_option")
    const val MEALTIME_PREFERENCES = "MEALTIME_PREFERENCES"
    val USER_DATA = stringPreferencesKey("user_data")
}

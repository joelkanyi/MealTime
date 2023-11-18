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

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

object Constants {
    val USER_ID = stringPreferencesKey("user_id")
    val ACCESS_TOKEN = stringPreferencesKey("access_token")
    const val MIXPANEL_TOKEN = "befbaa0a69fb8d4b9824e1a2391a8540"
    const val MEAL_TABLE = "meal_table"
    const val CATEGORIES_TABLE = "categories_table"
    const val MEALTIME_DATABASE = "meal_time_database"
    const val FAVORITES_TABLE = "favorites_table"
    const val MEAL_PLAN_TABLE = "meal_plan_table"

    val THEME_OPTIONS = intPreferencesKey(name = "theme_option")
    const val MEALTIME_PREFERENCES = "MEALTIME_PREFERENCES"
    val USER_DATA = stringPreferencesKey("user_data")

    val ALLERGIES = stringSetPreferencesKey("allergies")
    val NUMBER_OF_PEOPLE = stringPreferencesKey("number_of_people")
    val DISH_TYPES = stringSetPreferencesKey("dish_types")
    val SUBSCRIPTION = booleanPreferencesKey("subscription")
    val PURCHASE_ID = "product_premium_subscription"
    val QONVERSION_PROJECT_KEY = "fuin-2K_eXiH5zYAUIlXaIPEn0BDiWI9"
    val CLIENT_ID = "185726691445-ke95p4s6bk7315ni58ek2m718nkk6bal.apps.googleusercontent.com"
}

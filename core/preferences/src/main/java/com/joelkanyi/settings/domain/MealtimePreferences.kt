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
package com.joelkanyi.settings.domain

import com.joelkanyi.settings.domain.model.MealPlanPreference
import kotlinx.coroutines.flow.Flow

interface MealtimePreferences {
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
    suspend fun clear()
}

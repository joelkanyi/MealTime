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
package com.kanyideveloper.settings.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.kanyideveloper.core.data.MealTimePreferences
import com.kanyideveloper.core.domain.UserDataRepository
import com.kanyideveloper.settings.data.UserDataRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserDataModule {

    @Provides
    @Singleton
    fun provideMealTimePreferences(
        dataStore: DataStore<Preferences>,
        databaseReference: DatabaseReference,
        firebaseAuth: FirebaseAuth
    ) =
        MealTimePreferences(
            dataStore = dataStore,
            databaseReference = databaseReference,
            firebaseAuth = firebaseAuth
        )

    @Provides
    @Singleton
    fun provideUserDataRepository(mealTimePreferences: MealTimePreferences): UserDataRepository {
        return UserDataRepositoryImpl(mealTimePreferences = mealTimePreferences)
    }
}

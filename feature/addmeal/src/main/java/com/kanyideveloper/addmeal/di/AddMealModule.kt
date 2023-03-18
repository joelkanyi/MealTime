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
package com.kanyideveloper.addmeal.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.kanyideveloper.addmeal.data.repository.SaveMealRepositoryImpl
import com.kanyideveloper.addmeal.data.repository.UploadImageRepositoryImpl
import com.kanyideveloper.addmeal.domain.repository.SaveMealRepository
import com.kanyideveloper.addmeal.domain.repository.UploadImageRepository
import com.kanyideveloper.core_database.MealTimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AddMealModule {

    @Provides
    @Singleton
    fun provideUploadImageRepository(
        storageReference: StorageReference,
        @ApplicationContext context: Context
    ): UploadImageRepository {
        return UploadImageRepositoryImpl(
            storageReference = storageReference,
            context = context
        )
    }

    @Provides
    @Singleton
    fun provideSaveMealRepository(
        mealTimeDatabase: MealTimeDatabase,
        databaseReference: DatabaseReference,
        firebaseAuth: FirebaseAuth
    ): SaveMealRepository {
        return SaveMealRepositoryImpl(
            mealTimeDatabase = mealTimeDatabase,
            databaseReference = databaseReference,
            firebaseAuth = firebaseAuth
        )
    }

    @Provides
    @Singleton
    fun provideFirebaseStorageReference(): StorageReference {
        return FirebaseStorage.getInstance().getReference("meal_images")
    }
}

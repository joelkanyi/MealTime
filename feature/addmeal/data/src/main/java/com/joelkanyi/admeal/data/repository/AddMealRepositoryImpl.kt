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
package com.joelkanyi.admeal.data.repository

import android.graphics.Bitmap
import com.google.firebase.storage.StorageReference
import com.joelkanyi.admeal.domain.repository.AddMealRepository
import com.joelkanyi.common.model.Ingredient
import com.joelkanyi.common.util.Resource
import com.joelkanyi.common.util.safeApiCall
import com.joelkanyi.network.MealtimeApiService
import com.joelkanyi.network.model.CreateMealRequestDto
import com.joelkanyi.settings.domain.MealtimePreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.util.UUID
import javax.inject.Inject

class AddMealRepositoryImpl @Inject constructor(
    private val mealtimeApiService: MealtimeApiService,
    private val mealtimePreferences: MealtimePreferences,
    private val storageReference: StorageReference,
) : AddMealRepository {
    override suspend fun saveMeal(
        calories: Int,
        category: String,
        cookingDifficulty: String,
        cookingInstructions: List<String>,
        cookingTime: Int,
        description: String?,
        imageUrl: String,
        ingredients: List<Ingredient>,
        name: String,
        recipePrice: Double?,
        serving: Int?,
        youtubeUrl: String?
    ): Resource<Unit> {
        return safeApiCall(Dispatchers.IO) {
            val request = CreateMealRequestDto(
                calories = calories,
                category = category,
                cookingDifficulty = cookingDifficulty,
                cookingInstructions = cookingInstructions.map {
                    CreateMealRequestDto.CookingInstructionDto(
                        it
                    )
                },
                cookingTime = cookingTime,
                description = description,
                imageUrl = imageUrl,
                ingredients = ingredients.map {
                    CreateMealRequestDto.IngredientDto(
                        it.name,
                        it.quantity
                    )
                },
                name = name,
                recipePrice = recipePrice,
                serving = serving,
                youtubeUrl = youtubeUrl,
                userId = mealtimePreferences.getUserId().first() ?: ""
            )
            mealtimeApiService.saveMeal(request)
        }
    }

    override suspend fun uploadImage(imageBitmap: Bitmap): Resource<String> {
        return safeApiCall(Dispatchers.IO) {
            val fileStorageReference = storageReference.child("${UUID.randomUUID()}${imageBitmap.hashCode()}}")

            val byteArrayOutputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 25, byteArrayOutputStream)
            val fileInBytes: ByteArray = byteArrayOutputStream.toByteArray()

            val uploadTask = fileStorageReference.putBytes(fileInBytes)

            val result = uploadTask.continueWithTask {
                fileStorageReference.downloadUrl
            }.await().toString()

            result
        }
    }
}

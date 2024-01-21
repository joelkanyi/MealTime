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
package com.joelkanyi.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.joelkanyi.common.util.Resource
import com.joelkanyi.data.mapper.toFavorite
import com.joelkanyi.settings.domain.MealtimePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val favoritesDao: com.joelkanyi.database.dao.FavoritesDao,
    private val mealtimeApiService: com.joelkanyi.network.MealtimeApiService,
    private val mealtimePreferences: MealtimePreferences
) : com.joelkanyi.domain.repository.FavoritesRepository {

    override fun getFavorites(): Flow<List<com.joelkanyi.domain.entity.Favorite>> {
        return favoritesDao.getFavorites().map {
            it.map { favoriteEntity ->
                favoriteEntity.toFavorite()
            }
        }
    }

    override fun getASingleFavorite(id: Int): Flow<com.joelkanyi.domain.entity.Favorite?> {
        return favoritesDao.getAFavoriteById(id = id).map {
            it?.toFavorite()
        }
    }

    override fun isFavorite(id: String): LiveData<Boolean> {
        return favoritesDao.inFavorites(id = id).map {
            it != null
        }
    }

    override suspend fun deleteAllFavorites() {
        favoritesDao.deleteAllFavorites()
    }

    override suspend fun insertFavorite(
        name: String,
        imageUrl: String,
        mealId: String,
        category: String
    ): Resource<Boolean> {
        favoritesDao.insertAFavorite(
            com.joelkanyi.database.model.FavoriteEntity(
                mealId = mealId,
                mealName = name,
                mealImageUrl = imageUrl,
            )
        )
        return Resource.Success(true)
    }

    override suspend fun deleteAFavorite(
        mealId: String
    ) {
        favoritesDao.deleteAFavorite(mealId = mealId)
    }
}

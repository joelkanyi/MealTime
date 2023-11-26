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
import com.joelkanyi.common.util.safeApiCall
import com.joelkanyi.data.mapper.toFavorite
import com.joelkanyi.settings.domain.MealtimeSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val favoritesDao: com.joelkanyi.database.dao.FavoritesDao,
    private val mealDbApi: com.joelkanyi.network.MealDbApi,
    private val mealtimeSettings: MealtimeSettings
) : com.joelkanyi.domain.repository.FavoritesRepository {

    override fun getFavorites(): Flow<List<com.joelkanyi.domain.entity.Favorite>> {
        /*safeApiCall(Dispatchers.IO) {
            val response = mealDbApi.getFavorites(
                // userId = mealTimePreferences.getUserId().first()
                userId = "a2fb5562-871b-4346-9cab-2cd4f4922738"
            )

            favoritesDao.deleteAllFavorites()

            response.forEach { favorite ->
                favoritesDao.insertAFavorite(
                    FavoriteEntity(
                        id = favorite.id,
                        mealId = favorite.mealId,
                        mealName = favorite.mealName,
                        mealImageUrl = favorite.mealImageUrl,
                    )
                )
            }
        }*/

        return favoritesDao.getFavorites().map {
            it.map { favoriteEntity ->
                favoriteEntity.toFavorite()
            }
        }
    }

    /*    private suspend fun getFavoritesFromRemoteDataSource(): Resource<Flow<List<Favorite>>> {
            */
    /**
     * Do offline caching
     *//*
        // first read from the local database
        val favorites = favoritesDao.getFavorites()

        return try {
            val newFavorites = withTimeoutOrNull(10000L) {
                // fetch from the remote database
                val favoritesRemote = mealDbApi.getFavorites(
                    // userId = mealTimePreferences.getUserId().first()
                    userId = "a2fb5562-871b-4346-9cab-2cd4f4922738"
                )

                // clear the local database
                favoritesDao.deleteAllFavorites()

                // save the remote data to the local database
                favoritesRemote.forEach { onlineFavorite ->
                    favoritesDao.insertAFavorite(
                        FavoriteEntity(
                            id = onlineFavorite.id,
                            mealId = onlineFavorite.mealId,
                            mealName = onlineFavorite.mealName,
                            mealImageUrl = onlineFavorite.mealImageUrl,
                        )
                    )
                }

                // read from the local database
                favoritesDao.getFavorites().map { favoriteEntities ->
                    favoriteEntities.map { favoriteEntity ->
                        favoriteEntity.toFavorite()
                    }
                }
            }

            if (newFavorites == null) {
                Resource.Error(
                    "Viewing offline data",
                    data = favorites.map {
                        it.map { favoriteEntity ->
                            favoriteEntity.toFavorite()
                        }
                    }
                )
            } else {
                Resource.Success(data = newFavorites)
            }
        } catch (e: Exception) {
            Resource.Error(
                e.localizedMessage ?: "Unknown error occurred",
                data = favorites.map {
                    it.map { favoriteEntity ->
                        favoriteEntity.toFavorite()
                    }
                }
            )
        }
    }*/

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

    private suspend fun saveFavoriteToRemoteDatasource(
        mealId: String,
    ): Resource<Boolean> {
        /*return safeApiCall(Dispatchers.IO) {
            mealDbApi.saveFavorite(
                CreateFavoriteRequestDto(
                    mealId = mealId,
                    // userId = mealTimePreferences.getUserId().first(),
                    userId = "a2fb5562-871b-4346-9cab-2cd4f4922738",
                )
            )
            true
        }*/
        favoritesDao.insertAFavorite(
            com.joelkanyi.database.model.FavoriteEntity(
                mealId = mealId,
                mealName = "Meal Name",
                mealImageUrl = "https://www.themealdb.com/images/media/meals/llcbn01574260722.jpg",
            )
        )
        return Resource.Success(true)
    }

    private suspend fun deleteAFavoriteFromRemoteDatasource(
        mealId: String
    ): Resource<Boolean> {
        return safeApiCall(Dispatchers.IO) {
            mealDbApi.deleteFavorite(
                mealId = mealId,
                // userId = mealTimePreferences.getUserId().first(),
                userId = "a2fb5562-871b-4346-9cab-2cd4f4922738",
            )
            favoritesDao.deleteAFavorite(mealId = mealId)
            true
        }
    }
}

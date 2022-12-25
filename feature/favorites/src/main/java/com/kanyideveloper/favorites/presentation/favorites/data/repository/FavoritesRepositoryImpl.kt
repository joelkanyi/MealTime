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
package com.kanyideveloper.favorites.presentation.favorites.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.kanyideveloper.core.domain.FavoritesRepository
import com.kanyideveloper.core.model.Favorite
import com.kanyideveloper.core_database.dao.FavoritesDao
import com.kanyideveloper.favorites.presentation.favorites.data.mapper.toEntity
import com.kanyideveloper.favorites.presentation.favorites.data.mapper.toFavorite

class FavoritesRepositoryImpl(
    private val favoritesDao: FavoritesDao
) : FavoritesRepository {
    override suspend fun insertFavorite(favorite: Favorite) {
        favoritesDao.insertAFavorite(favorite.toEntity())
    }

    override fun getFavorites(): LiveData<List<Favorite>> {
        return Transformations.map(favoritesDao.getFavorites()) { favoritesEntity ->
            favoritesEntity.map { it.toFavorite() }
        }
    }

    override fun getASingleFavorite(id: Int): LiveData<Favorite?> {
        return Transformations.map(favoritesDao.getAFavoriteById(id = id)) {
            it?.toFavorite()
        }
    }

    override fun isFavorite(id: Int): LiveData<Boolean> {
        return favoritesDao.inFavorites(id = id)
    }

    override suspend fun deleteOneFavorite(favorite: Favorite) {
        favoritesDao.deleteAFavorite(favorite.toEntity())
    }

    override suspend fun deleteAllFavorites() {
        favoritesDao.deleteAllFavorites()
    }

    override fun inFavorites(id: Int): LiveData<Boolean> {
        return favoritesDao.inFavorites(id = id)
    }
}

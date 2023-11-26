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
package com.joelkanyi.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.joelkanyi.database.model.FavoriteEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Insert
    suspend fun insertAFavorite(favoriteEntity: FavoriteEntity)

    @Query("SELECT * FROM favorites_table ORDER BY id DESC")
    fun getFavorites(): Flow<List<FavoriteEntity>>

    @Query("SELECT * FROM favorites_table WHERE id  == :id LIMIT 1")
    fun getAFavoriteById(id: Int): Flow<FavoriteEntity?>

    @Query("SELECT mealId FROM favorites_table WHERE mealId == :id LIMIT 1")
    fun inFavorites(id: String): LiveData<String?>

    @Query("DELETE FROM favorites_table WHERE mealId = :mealId")
    suspend fun deleteAFavorite(mealId: String)

    @Query("DELETE FROM favorites_table")
    suspend fun deleteAllFavorites()
}

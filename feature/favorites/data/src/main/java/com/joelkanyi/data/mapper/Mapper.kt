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
package com.joelkanyi.data.mapper

import com.joelkanyi.database.model.FavoriteEntity
import com.joelkanyi.domain.entity.Favorite
import com.joelkanyi.network.model.FavoritesResponseDto

internal fun com.joelkanyi.network.model.FavoritesResponseDto.toFavorite(): com.joelkanyi.domain.entity.Favorite {
    return com.joelkanyi.domain.entity.Favorite(
        id = id,
        mealName = mealName,
        mealImageUrl = mealImageUrl,
        mealId = mealId,
    )
}

internal fun com.joelkanyi.database.model.FavoriteEntity.toFavorite(): com.joelkanyi.domain.entity.Favorite {
    return com.joelkanyi.domain.entity.Favorite(
        id = id,
        mealName = mealName,
        mealImageUrl = mealImageUrl,
        mealId = mealId,
    )
}

internal fun com.joelkanyi.domain.entity.Favorite.toEntity(): com.joelkanyi.database.model.FavoriteEntity {
    return com.joelkanyi.database.model.FavoriteEntity(
        id = id,
        mealName = mealName,
        mealImageUrl = mealImageUrl,
        mealId = mealId,
    )
}

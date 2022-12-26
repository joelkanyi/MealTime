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
package com.kanyideveloper.favorites.presentation.favorites.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.compose_ui.theme.Shapes
import com.kanyideveloper.core.components.EmptyStateComponent
import com.kanyideveloper.core.model.Favorite
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.mealtime.core.R
import com.ramcosta.composedestinations.annotation.Destination

interface FavoritesNavigator {
    fun openOnlineMealDetails(mealId: String)
    fun openMealDetails(meal: Meal)
}

@Destination
@Composable
fun FavoritesScreen(
    navigator: FavoritesNavigator,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val favorites by viewModel.favorites.observeAsState()
    val meal = viewModel.singleMeal.value

    FavoritesScreenContent(
        favorites = favorites,
        onClick = { _, onlineMealId, localMealId, isOnline ->
            if (isOnline) {
                onlineMealId?.let { navigator.openOnlineMealDetails(mealId = it) }
            } else {
                localMealId?.let { viewModel.getASingleMeal(id = it) }
                meal?.let { navigator.openMealDetails(meal = it) }
            }
        },
        onFavoriteClick = { favorite ->
            viewModel.deleteAFavorite(favorite = favorite)
        }
    )
}

@Composable
private fun FavoritesScreenContent(
    favorites: List<Favorite>?,
    onClick: (Int?, String?, Int?, Boolean) -> Unit,
    onFavoriteClick: (Favorite) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigate = {},
            title = {
                Text(text = "Favorite meals", fontSize = 18.sp)
            },
            showBackArrow = false,
            navActions = {
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn {
                items(favorites ?: emptyList()) { favorite ->
                    FoodItem(
                        favorite = favorite,
                        onClick = onClick,
                        onFavoriteClick = onFavoriteClick
                    )
                }
            }

            if (favorites.isNullOrEmpty()) {
                EmptyStateComponent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodItem(
    favorite: Favorite,
    modifier: Modifier = Modifier,
    onClick: (Int?, String?, Int?, Boolean) -> Unit,
    onFavoriteClick: (Favorite) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(130.dp)
            .padding(horizontal = 8.dp, vertical = 5.dp),
        shape = Shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        onClick = {
            onClick(favorite.id, favorite.onlineMealId, favorite.localMealId, favorite.isOnline)
        }
    ) {
        Row(Modifier.fillMaxWidth()) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .fillMaxHeight(),
                contentDescription = null,
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = favorite.mealImageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.food_loading)
                        }).build()
                ),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))

            Column(
                Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = favorite.mealName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        onFavoriteClick(favorite)
                    }) {
                        Icon(
                            modifier = Modifier
                                .size(24.dp),
                            painter = painterResource(
                                id = if (favorite.isFavorite) {
                                    R.drawable.filled_favorite
                                } else {
                                    R.drawable.unfilled_favorite
                                }
                            ),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

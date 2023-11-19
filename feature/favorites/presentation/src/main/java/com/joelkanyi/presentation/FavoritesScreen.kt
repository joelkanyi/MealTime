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
package com.joelkanyi.presentation

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.joelkanyi.domain.entity.Favorite
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.compose_ui.theme.MealTimeTheme
import com.kanyideveloper.compose_ui.theme.Shapes
import com.kanyideveloper.compose_ui.theme.Theme
import com.kanyideveloper.core.components.EmptyStateComponent
import com.kanyideveloper.core.util.UiEvents
import com.kanyideveloper.mealtime.core.R
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.collectLatest

@Destination
@Composable
fun FavoritesScreen(
    navigator: FavoritesNavigator,
    viewModel: FavoritesViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val favorites = viewModel.favorites.collectAsState().value
    var mDisplayMenu by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true, block = {
        viewModel.trackUserEvent("view_favorites_screen")
        viewModel.eventsFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                else -> {}
            }
        }
    })


    FavoritesScreenContent(
        snackbarHostState = snackbarHostState,
        favorites = favorites,
        displayMenu = mDisplayMenu,
        onToggleMenu = {
            mDisplayMenu = it
        },
        onClick = { mealId ->
            viewModel.trackUserEvent("Open favorite meal details clicked")
            mealId?.let { navigator.openMealDetails(mealId = it) }
        },
        onFavoriteClick = { favorite ->
            viewModel.trackUserEvent("favorite_screen_favorite_clicked")
            viewModel.deleteAFavorite(
                favorite = favorite,
            )
        },
        onDeleteAllFavs = {
            viewModel.trackUserEvent("favorite_screen_delete_all_favorites_clicked")
            viewModel.deleteAllFavorites()
            mDisplayMenu = false
        }
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun FavoritesScreenContent(
    modifier: Modifier = Modifier,
    favorites: List<Favorite>,
    snackbarHostState: SnackbarHostState,
    onClick: (String?) -> Unit,
    onFavoriteClick: (Favorite) -> Unit,
    onDeleteAllFavs: () -> Unit,
    displayMenu: Boolean,
    onToggleMenu: (Boolean) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(
                snackbarHostState
            )
        },
        topBar = {
            StandardToolbar(
                navigate = {},
                title = {
                    Text(text = "Favorite meals", fontSize = 18.sp)
                },
                showBackArrow = false,
                navActions = {
                    IconButton(onClick = { onToggleMenu(!displayMenu) }) {
                        Icon(Icons.Default.MoreVert, "")
                    }

                    DropdownMenu(
                        expanded = displayMenu,
                        onDismissRequest = { onToggleMenu(false) }
                    ) {
                        DropdownMenuItem(
                            onClick = onDeleteAllFavs,
                            text = {
                                Text(text = "Delete All Favorites")
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Outlined.Delete,
                                    contentDescription = null
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.chevron_right),
                                    contentDescription = null
                                )
                            }
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            if (favorites.isNotEmpty()) {
                LazyColumn(
                    modifier = modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(
                        favorites,
                        key = { favorite -> favorite.id }
                    ) { favorite ->
                        FoodItem(
                            modifier = Modifier.animateItemPlacement(),
                            favorite = favorite,
                            onClick = onClick,
                            onFavoriteClick = onFavoriteClick
                        )
                    }
                }
            } else {
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
    onClick: (String?) -> Unit,
    onFavoriteClick: (Favorite) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 8.dp, vertical = 5.dp),
        shape = Shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        onClick = {
            onClick(
                favorite.mealId,
            )
        }
    ) {
        Column {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentDescription = null,
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = favorite.mealImageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            placeholder(R.drawable.placeholder)
                        }).build()
                ),
                contentScale = ContentScale.Crop
            )

            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .wrapContentHeight(),
                    text = favorite.mealName,
                    style = MaterialTheme.typography.titleMedium
                )

                Icon(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {
                            onFavoriteClick(favorite)
                        },
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = null,
                    tint = Color(0xFFfa4a0c)
                )
            }
        }
    }
}


@Preview
@Composable
fun FavoritesScreenContentPreview() {
    MealTimeTheme(theme = Theme.LIGHT_THEME.themeValue) {
        FavoritesScreenContent(
            favorites = emptyList(),
            snackbarHostState = SnackbarHostState(),
            onClick = {},
            onFavoriteClick = {},
            onDeleteAllFavs = {},
            displayMenu = false,
            onToggleMenu = {}
        )
    }
}
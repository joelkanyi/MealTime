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
package com.joelkanyi.presentation.search

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.joelkanyi.common.model.Meal
import com.joelkanyi.common.util.UiEvents
import com.joelkanyi.designsystem.components.EmptyStateComponent
import com.joelkanyi.designsystem.components.ErrorStateComponent
import com.joelkanyi.designsystem.components.LoadingStateComponent
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.flow.collectLatest

@Destination
@Composable
fun SearchScreen(navigator: SearchNavigator, viewModel: SearchViewModel = hiltViewModel()) {
    val searchState = viewModel.searchState.value
    val favorites = viewModel.favorites.collectAsState().value
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.trackUserEvent("view_search_screen")
        viewModel.eventsFlow.collectLatest { event ->
            when (event) {
                is UiEvents.SnackbarEvent -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
    }

    SearchScreenContent(
        searchState = searchState,
        currentSearchString = viewModel.searchString.value,
        onSearchStringChange = { newValue ->
            viewModel.setSearchString(newValue)
        },
        onSearch = { searchParam ->
            viewModel.trackUserEvent("search online meals - $searchParam")
            viewModel.search(searchParam)
        },
        onSearchOptionClick = { option ->
            viewModel.trackUserEvent("search_screen_${option}_click ")
            viewModel.setSelectedSearchOption(option)
            if (viewModel.searchString.value.isNotEmpty()) {
                viewModel.search(viewModel.searchString.value)
            }
        },
        isSelected = { option ->
            viewModel.selectedSearchOption.value == option
        },
        addToFavorites = { meal ->
            viewModel.trackUserEvent("search_screen_add_to_favorites")
            viewModel.insertAFavorite(
                meal
            )
        },
        removeFromFavorites = { mealId ->
            viewModel.trackUserEvent("search_screen_remove_from_favorites")
            viewModel.deleteAnOnlineFavorite(
                mealId = mealId
            )
        },
        onMealClick = { mealId ->
            viewModel.trackUserEvent("search_screen_meal_click")
            navigator.openMealDetails(mealId = mealId)
        },
        onClickBack = {
            viewModel.trackUserEvent("search_screen_back_click")
            navigator.popBackStack()
        },
        isFavorite = { mealId ->
            favorites.any { it.mealId == mealId }
        }
    )
}

@Composable
private fun SearchScreenContent(
    searchState: SearchState,
    onMealClick: (String) -> Unit,
    addToFavorites: (meal: Meal) -> Unit,
    removeFromFavorites: (mealId: String) -> Unit,
    onSearchStringChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    currentSearchString: String,
    isSelected: (String) -> Boolean,
    onSearchOptionClick: (String) -> Unit,
    onClickBack: () -> Unit,
    isFavorite: (mealId: String) -> Boolean,
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {
        com.joelkanyi.designsystem.components.StandardToolbar(
            navigate = {
                onClickBack()
            },
            title = {
                Text(text = "Search", fontSize = 18.sp)
            },
            showBackArrow = true,
            navActions = {
            }
        )

        SearchOptionsComponent(
            options = searchOptions,
            onClick = onSearchOptionClick,
            isSelected = isSelected
        )

        SearchBar(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(8.dp),
            currentSearchString = currentSearchString,
            onSearchStringChange = onSearchStringChange,
            onSearch = onSearch
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(searchState.searchData) { searchData ->
                    OnlineMealItem(
                        meal = searchData,
                        onClick = onMealClick,
                        addToFavorites = addToFavorites,
                        removeFromFavorites = removeFromFavorites,
                        isFavorite = isFavorite
                    )
                }
            }

            // Loading data
            if (searchState.isLoading) {
                LoadingStateComponent()
            }

            // An Error has occurred
            if (!searchState.isLoading && searchState.error != null) {
                ErrorStateComponent(errorMessage = searchState.error)
            }

            // Loaded Data but the list is empty
            if (!searchState.isLoading && searchState.error == null && searchState.searchData.isEmpty()) {
                EmptyStateComponent()
            }
        }
    }
}

private val searchOptions = listOf("Meal Name", "Ingredient", "Meal Category")

@Composable
fun SearchOptionsComponent(
    options: List<String>,
    onClick: (String) -> Unit,
    isSelected: (String) -> Boolean
) {
    LazyRow {
        items(options) { option ->
            SearchOption(
                option = option,
                onClick = onClick,
                isSelected = isSelected
            )
        }
    }
}

@Composable
fun SearchOption(option: String, onClick: (String) -> Unit, isSelected: (String) -> Boolean) {
    Card(
        Modifier
            .wrapContentWidth()
            .wrapContentHeight()
            .padding(4.dp)
            .clickable {
                onClick(option)
            },
        shape = RoundedCornerShape(70.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected(option)) {
                androidx.compose.material3.MaterialTheme.colorScheme.primary
            } else {
                androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Box(
            modifier = Modifier
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = option,
                textAlign = TextAlign.Center,
                style = androidx.compose.material3.MaterialTheme.typography.labelLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (isSelected(option)) {
                    androidx.compose.material3.MaterialTheme.colorScheme.onPrimary
                } else {
                    androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    onSearch: (String) -> Unit = {},
    onSearchStringChange: (String) -> Unit,
    currentSearchString: String
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = currentSearchString,
        onValueChange = {
            onSearchStringChange(it)
        },
        placeholder = {
            Text(text = "Search...")
        },
        modifier = modifier
            .fillMaxWidth()
            .shadow(4.dp, CircleShape)
            .background(Color.Transparent, CircleShape),
        shape = MaterialTheme.shapes.medium,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Words,
            autoCorrect = true,
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions {
            keyboardController?.hide()
            onSearch(currentSearchString)
        },
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        maxLines = 1,
        singleLine = true,
        trailingIcon = {
            IconButton(
                onClick = {
                    keyboardController?.hide()
                    onSearch(currentSearchString)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
fun OnlineMealItem(
    meal: Meal,
    onClick: (String) -> Unit,
    addToFavorites: (meal: Meal) -> Unit,
    removeFromFavorites: (mealId: String) -> Unit,
    isFavorite: (mealId: String) -> Boolean,
) {
    val favorite = isFavorite(meal.mealId)
    Card(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentHeight()
            .padding(vertical = 5.dp)
            .clickable {
                onClick(meal.mealId)
            },
        shape = androidx.compose.material3.MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentDescription = meal.name,
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = meal.imageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            placeholder(com.joelkanyi.common.R.drawable.placeholder)
                        }).build()
                ),
                contentScale = ContentScale.Crop
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .padding(vertical = 3.dp),
                    text = meal.name,
                    style = androidx.compose.material3.MaterialTheme.typography.titleSmall,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                androidx.compose.material3.IconButton(
                    onClick = {
                        if (favorite) {
                            removeFromFavorites(
                                meal.mealId,
                            )
                        } else {
                            addToFavorites(meal)
                        }
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .size(30.dp),
                        painter = if (favorite) {
                            painterResource(id = com.joelkanyi.common.R.drawable.filled_favorite)
                        } else {
                            painterResource(id = com.joelkanyi.common.R.drawable.heart_plus)
                        },
                        contentDescription = null,
                        tint = if (favorite) {
                            Color(0xFFfa4a0c)
                        } else {
                            androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
        }
    }
}


@Preview
@Composable
fun SearchScreenContentPreview() {
    com.joelkanyi.designsystem.theme.MealTimeTheme(
        theme = com.joelkanyi.designsystem.theme.Theme.LIGHT_THEME.themeValue
    ) {
        SearchScreenContent(
            searchState = SearchState(
                isLoading = false,
                error = null,
                searchData = listOf(
                    Meal(
                        mealId = "1",
                        name = "Chicken",
                        imageUrl = "https://www.themealdb.com/images/media/meals/1529446352.jpg",
                        category = "Chicken",
                    ),
                )
            ),
            onMealClick = {},
            addToFavorites = {},
            removeFromFavorites = {},
            onSearchStringChange = {},
            onSearch = {},
            currentSearchString = "",
            isSelected = { false },
            onSearchOptionClick = {},
            onClickBack = {},
            isFavorite = { false }
        )
    }
}
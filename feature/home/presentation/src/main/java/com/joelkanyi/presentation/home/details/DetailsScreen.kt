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
package com.joelkanyi.presentation.home.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kanyideveloper.compose_ui.theme.MealTimeTheme
import com.kanyideveloper.compose_ui.theme.Theme
import com.kanyideveloper.core.components.EmptyStateComponent
import com.kanyideveloper.core.components.ErrorStateComponent
import com.kanyideveloper.core.components.LoadingStateComponent
import com.kanyideveloper.core.model.Ingredient
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.model.MealDetails
import com.kanyideveloper.mealtime.core.R
import com.joelkanyi.presentation.home.HomeNavigator
import com.ramcosta.composedestinations.annotation.Destination
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.CollapsingToolbarScaffoldState
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@Destination
@Composable
fun DetailsScreen(
    mealId: String?,
    navigator: HomeNavigator,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    val state = rememberCollapsingToolbarScaffoldState()
    val mealState = viewModel.details.value
    val favorites = viewModel.favorites.collectAsState().value

    LaunchedEffect(key1 = true, block = {
        viewModel.trackUserEvent("view_details_screen")
        if (mealId != null) {
            viewModel.getDetails(mealId = mealId)
        } else {
            viewModel.getRandomMeal()
        }
    })

    DetailsScreenContent(
        mealState = mealState,
        state = state,
        navigateBack = {
            navigator.popBackStack()
        },
        addToFavorites = { meal ->
            viewModel.trackUserEvent("details_screen_add_to_favorites")
            viewModel.insertAFavorite(meal)
        },
        removeFromFavorites = { id ->
            viewModel.trackUserEvent("details_screen_remove_from_favorites")
            viewModel.deleteAFavorite(mealId = id)
        },
        isFavorite = { _ ->
            favorites.any { it.mealId == mealId }
        },
    )
}

@Composable
private fun DetailsScreenContent(
    mealState: DetailsState,
    state: CollapsingToolbarScaffoldState,
    navigateBack: () -> Unit,
    addToFavorites: (meal: Meal) -> Unit,
    removeFromFavorites: (mealId: String) -> Unit,
    isFavorite: (mealId: String) -> Boolean,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Data has been loaded successfully
        if (!mealState.isLoading && mealState.mealDetails != null) {
            val meal = mealState.mealDetails
            val favorite = isFavorite(meal.mealId)
            val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp


            CollapsingToolbarScaffold(
                modifier = Modifier
                    .fillMaxSize(),
                state = state,
                scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                toolbarModifier = Modifier.background(MaterialTheme.colorScheme.background),
                enabled = true,
                toolbar = {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.background)
                            .fillMaxWidth()
                            .height(150.dp)
                            .pin()
                    )

                    Image(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .parallax(ratio = 0.5f)
                            .graphicsLayer {
                                alpha = if (textSize.value == 18f) 0f else 1f
                            },
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(data = meal.imageUrl)
                                .apply(block = fun ImageRequest.Builder.() {
                                    placeholder(R.drawable.placeholder)
                                }).build()
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = meal.name,
                        modifier = Modifier
                            .road(Alignment.CenterStart, Alignment.BottomEnd)
                            .padding(60.dp, 16.dp, 16.dp, 16.dp),
                        color = if (textSize.value >= 19) {
                            Color.Transparent
                        } else {
                            MaterialTheme.colorScheme.onBackground
                        },
                        fontSize = textSize
                    )

                    IconButton(onClick = {
                        navigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(
                        horizontal = 16.dp,
                        vertical = 12.dp
                    )
                ) {
                    item {
                        if (textSize.value >= 19) {
                            Row(
                                Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(0.85f),
                                    text = meal.name,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                                Box(
                                    modifier = Modifier
                                        .size(50.dp)
                                        .clip(CircleShape)
                                        .background(
                                            MaterialTheme.colorScheme.surfaceVariant
                                        )
                                        .clickable {
                                            if (favorite) {
                                                removeFromFavorites(
                                                    meal.mealId
                                                )
                                            } else {
                                                meal.let {
                                                    addToFavorites(
                                                        Meal(
                                                            mealId = it.mealId,
                                                            name = it.name,
                                                            imageUrl = it.imageUrl,
                                                            category = it.category,
                                                        )
                                                    )
                                                }
                                            }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        modifier = Modifier
                                            .size(30.dp),
                                        painter = if (favorite) {
                                            painterResource(id = R.drawable.filled_favorite)
                                        } else {
                                            painterResource(id = R.drawable.heart_plus)
                                        },
                                        contentDescription = null,
                                        tint = if (favorite) {
                                            Color(0xFFfa4a0c)
                                        } else {
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                        }
                                    )
                                }
                            }
                        }
                    }
                    item {
                        MealProperties(meal)
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ingredients",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    items(meal.ingredients) { ingredient ->
                        Row(
                            modifier = Modifier.padding(start = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.onBackground)
                            )
                            Text(
                                modifier = Modifier.padding(3.dp),
                                text = ingredient.name,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Cooking Instructions",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }

                    itemsIndexed(meal.cookingDirections) { index, instruction ->
                        Row(
                            modifier = Modifier.padding(start = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(6.dp),
                                text = "${index + 1}.",
                                style = MaterialTheme.typography.bodyMedium
                            )

                            Text(
                                modifier = Modifier.padding(6.dp),
                                text = instruction,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }


        // Loading data
        if (mealState.isLoading) {
            LoadingStateComponent()
        }

        // An Error has occurred
        if (!mealState.isLoading && mealState.error != null) {
            ErrorStateComponent(errorMessage = mealState.error)
        }

        // Loaded Data but the list is empty
        if (!mealState.isLoading && mealState.error == null && mealState.mealDetails == null) {
            EmptyStateComponent()
        }
    }
}


@Preview
@Composable
fun MealProperties(
    meal: MealDetails = sampleMealDetails
) {
    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (meal.cookingTime != null) {
            MealProperty(
                icon = R.drawable.ic_clock,
                value = "${meal.cookingTime} min"
            )
        }

        if (meal.servingPeople != null) {
            MealProperty(
                icon = R.drawable.users_three_light,
                value = "${meal.serving} serving"
            )
        }

        if (meal.cookingDifficulty != null) {
            MealProperty(
                icon = R.drawable.ic_easy,
                value = "${meal.cookingDifficulty}"
            )
        }

        // calories
        if (meal.calories != null) {
            MealProperty(
                icon = R.drawable.fire_simple_bold,
                value = "${meal.calories} Kcal"
            )
        }
        MealProperty(
            icon = R.drawable.ic_food,
            value = meal.category
        )
    }
}

@Composable
fun MealProperty(
    modifier: Modifier = Modifier,
    icon: Int,
    value: String
) {
    Box(
        modifier = modifier
            .height(80.dp)
            .width(70.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(
                MaterialTheme.colorScheme.surfaceVariant
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Icon(
                modifier = Modifier
                    .size(24.dp),
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailsScreenContentPreview() {
    MealTimeTheme(theme = Theme.LIGHT_THEME.themeValue) {
        DetailsScreenContent(
            mealState = DetailsState(
                mealDetails = sampleMealDetails,
            ),
            state = rememberCollapsingToolbarScaffoldState(),
            isFavorite = { true },
            navigateBack = {},
            addToFavorites = {},
            removeFromFavorites = {},
        )
    }
}

val sampleMealDetails = MealDetails(
    name = "Chicken Vesuvio",
    imageUrl = "https://www.themealdb.com/images/media/meals/wytyrl1511307101.jpg",
    cookingTime = 20,
    servingPeople = 4,
    category = "Chicken",
    cookingDifficulty = "Easy",
    ingredients = listOf(
        Ingredient(
            name = "Olive Oil",
            quantity = "1/2 cup",
            id = 1
        ),
        Ingredient(
            name = "Chicken",
            quantity = "1 whole",
            id = 2
        ),
        Ingredient(
            name = "Potatoes",
            quantity = "4 large",
            id = 3
        ),
        Ingredient(
            name = "Garlic",
            quantity = "3 cloves",
            id = 4
        ),
        Ingredient(
            name = "Rosemary",
            quantity = "1 tbsp",
            id = 5
        ),
    ),
    cookingDirections = listOf(
        "Heat oil in a large skillet over medium-high heat. Season chicken with salt and pepper. Add chicken to skillet and cook until golden brown, about 4 minutes per side. Transfer chicken to a plate.",
        "Add potatoes to skillet and cook until golden brown, about 4 minutes. Add garlic and rosemary and cook until fragrant, about 1 minute. Add wine and broth and bring to a boil. Reduce heat to medium-low, return chicken to skillet, cover, and simmer until potatoes are tender and chicken is cooked through, about 15 minutes.",
        "Transfer chicken to a serving platter. Increase heat to medium-high and simmer until liquid is reduced by half, about 5 minutes. Season sauce with salt and pepper. Pour sauce over chicken and serve."
    ),
    mealId = "52959",
    mealPlanId = null,
    calories = 602.0,
    description = "Chicken Vesuvio is an Italian-American dish made from chicken on the bone and wedges of potato, celery, and carrots, sauteed with garlic, oregano, white wine, and olive oil, then baked until the chicken's skin becomes crisp.",
    recipePrice = 0.0,
    reviews = emptyList(),
    serving = 4,
    youtubeUrl = "https://www.youtube.com/watch?v=1IszT_guI08"
)
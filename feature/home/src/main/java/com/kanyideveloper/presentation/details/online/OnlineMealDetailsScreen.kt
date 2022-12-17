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
package com.kanyideveloper.presentation.details.online

import android.annotation.SuppressLint
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kanyideveloper.compose_ui.theme.LightGrey
import com.kanyideveloper.compose_ui.theme.MainOrange
import com.kanyideveloper.core.components.EmptyStateComponent
import com.kanyideveloper.core.components.ErrorStateComponent
import com.kanyideveloper.core.components.LoadingStateComponent
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.mealtime.core.R
import com.kanyideveloper.presentation.details.DetailsState
import com.kanyideveloper.presentation.details.DetailsViewModel
import com.kanyideveloper.presentation.home.HomeNavigator
import com.ramcosta.composedestinations.annotation.Destination
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination
@Composable
fun OnlineMealDetailsScreen(
    mealId: String,
    navigator: HomeNavigator,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = true) {
        viewModel.getDetails(mealId = mealId)
    }

    val mealState = viewModel.details.value

    OnlineMealScreenContent(
        mealState = mealState,
        navigateBack = {
            navigator.popBackStack()
        }
    )
}

@Composable
private fun OnlineMealScreenContent(
    mealState: DetailsState,
    navigateBack: () -> Unit = {}
) {
    val state = rememberCollapsingToolbarScaffoldState()
    val textSize = (18 + (30 - 18) * state.toolbarState.progress).sp

    Box(modifier = Modifier.fillMaxSize()) {
        // Data has been loaded successfully
        if (!mealState.isLoading && mealState.mealDetails.isNotEmpty()) {
            val meal = mealState.mealDetails.first()
            CollapsingToolbarScaffold(
                modifier = Modifier.fillMaxSize(),
                state = state,
                scrollStrategy = ScrollStrategy.ExitUntilCollapsed,
                toolbarModifier = Modifier.background(MaterialTheme.colors.primary),
                enabled = true,
                toolbar = {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colors.primary)
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
                                    crossfade(true)
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
                            Color.White
                        },
                        fontSize = textSize
                    )

                    IconButton(onClick = {
                        navigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            ) {
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)) {
                    item {
                        Row(
                            Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                modifier = Modifier.fillMaxWidth(0.85f),
                                text = meal.name,
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            )
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(
                                        LightGrey.copy(alpha = .6f)
                                    )
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)
                                        .padding(0.dp)
                                        .clickable {
                                        },
                                    painter = painterResource(
                                        id = if (meal.isFavorite) {
                                            R.drawable.filled_favorite
                                        } else {
                                            R.drawable.unfilled_favorite
                                        }
                                    ),
                                    contentDescription = null
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    item {
                        MealProperties(meal)
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ingredients",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
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
                                    .background(Color.Black)
                            )
                            Text(
                                modifier = Modifier.padding(3.dp),
                                text = ingredient,
                                fontWeight = FontWeight.Light,
                                fontSize = 16.sp
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Cooking Instructions",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                    }

                    items(meal.cookingDirections) { instruction ->
                        Row(
                            modifier = Modifier.padding(start = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black)
                            )
                            Text(
                                modifier = Modifier.padding(3.dp),
                                text = instruction,
                                fontWeight = FontWeight.Light,
                                fontSize = 16.sp
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
        if (!mealState.isLoading && mealState.error == null && mealState.mealDetails.isEmpty()) {
            EmptyStateComponent()
        }
    }
}

@Composable
private fun MealProperties(meal: Meal) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(top = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(
            8.dp,
            Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    MainOrange
                )
        ) {
            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            Color.White
                        )
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                            .padding(0.dp),
                        painter = painterResource(id = R.drawable.ic_clock),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${meal.cookingTime} mins",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }

        // Cooking level
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(12.dp))
                .background(
                    MainOrange
                )
        ) {
            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            Color.White
                        )
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                            .padding(0.dp),
                        painter = painterResource(
                            id = R.drawable.users_three_light
                        ),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "${meal.servingPeople} Serving",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }

        // To be served to
        Box(
            modifier = Modifier
                .width(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    MainOrange
                )
        ) {
            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            Color.White
                        )
                ) {
                    Icon(
                        modifier = Modifier
                            .size(80.dp)
                            .align(Alignment.Center)
                            .padding(0.dp),
                        painter = painterResource(id = R.drawable.noun_easy),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = meal.cookingDifficulty,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }

        Box(
            modifier = Modifier
                .width(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    MainOrange
                )
        ) {
            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            Color.White
                        )
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                            .padding(0.dp),
                        painter = painterResource(
                            id = R.drawable.fire_simple_bold
                        ),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "30 kcal",
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }

        Box(
            modifier = Modifier
                .width(60.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(
                    MainOrange
                )
        ) {
            Column(
                modifier = Modifier.padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(
                            Color.White
                        )
                ) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                            .padding(0.dp),
                        painter = painterResource(id = R.drawable.ic_food),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = meal.category,
                    fontSize = 14.sp,
                    color = Color.White
                )
            }
        }
    }
}

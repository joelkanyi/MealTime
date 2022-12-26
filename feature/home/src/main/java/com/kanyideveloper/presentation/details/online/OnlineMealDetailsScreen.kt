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
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kanyideveloper.core.components.EmptyStateComponent
import com.kanyideveloper.core.components.ErrorStateComponent
import com.kanyideveloper.core.components.LoadingStateComponent
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.mealtime.core.R
import com.kanyideveloper.presentation.details.DetailsState
import com.kanyideveloper.presentation.details.DetailsViewModel
import com.kanyideveloper.presentation.details.common.DetailsCollapsingToolbar
import com.kanyideveloper.presentation.home.HomeNavigator
import com.ramcosta.composedestinations.annotation.Destination

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
    Box(modifier = Modifier.fillMaxSize()) {
        // Data has been loaded successfully
        if (!mealState.isLoading && mealState.mealDetails.isNotEmpty()) {
            val meal = mealState.mealDetails.first()
            DetailsCollapsingToolbar(
                meal = meal,
                navigateBack = {
                    navigateBack()
                },
                onRemoveFavorite = { _, _ ->
                }
            )
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
                    MaterialTheme.colorScheme.primaryContainer
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
                    MaterialTheme.colorScheme.primaryContainer
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
                    MaterialTheme.colorScheme.primaryContainer
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
                    MaterialTheme.colorScheme.primaryContainer
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
                    MaterialTheme.colorScheme.primaryContainer
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

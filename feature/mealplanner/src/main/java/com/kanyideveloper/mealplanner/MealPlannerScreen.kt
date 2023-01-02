/*
 * Copyright 2023 Joel Kanyi.
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
package com.kanyideveloper.mealplanner

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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.compose_ui.theme.PrimaryColor
import com.kanyideveloper.compose_ui.theme.Shapes
import com.kanyideveloper.core.components.EmptyStateComponent
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.mealtime.core.R
import com.ramcosta.composedestinations.annotation.Destination

interface MealPlannerNavigator {
    fun popBackStack()
    fun openAllergiesScreen()
    fun openNoOfPeopleScreen()
    fun openMealTypesScreen()
    fun openMealPlanner()
}

@Destination
@Composable
fun MealPlannerScreen(
    navigator: MealPlannerNavigator
) {
    val hasMealPlan = true
    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigate = {
                navigator.popBackStack()
            },
            title = {
                Text(text = "Meal Planner", fontSize = 18.sp)
            },
            showBackArrow = false,
            navActions = {
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (!hasMealPlan) {
                EmptyStateComponent(
                    anim = R.raw.women_thinking,
                    message = "Looks like you haven't created a meal plan yet",
                    content = {
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(onClick = {
                            navigator.openAllergiesScreen()
                        }) {
                            Text(text = "Get Started")
                        }
                    }
                )
            }

            if (hasMealPlan) {
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
                    item {
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            items(days) { day ->
                                DayItemCard(day = day.name, date = day.date)
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    items(mealTypes) { type ->
                        MealPlanItem(mealType = type)
                    }
                }
            }
        }
    }
}

data class Day(
    val name: String,
    val date: String
)

private val days = listOf(
    Day(name = "Mon", date = "02"),
    Day(name = "Tue", date = "03"),
    Day(name = "Wed", date = "04"),
    Day(name = "Thur", date = "05"),
    Day(name = "Fri", date = "06"),
    Day(name = "Sat", date = "07"),
    Day(name = "Sun", date = "08")
)

private val meal = Meal(
    name = "Test Meal that will fit here will fit well",
    imageUrl = "https://www.themealdb.com/images/media/meals/020z181619788503.jpg",
    cookingTime = 0,
    category = "Test",
    cookingDifficulty = "",
    ingredients = listOf(),
    cookingDirections = listOf(),
    isFavorite = false,
    servingPeople = 0
)

private val mealTypes = listOf(
    MealType(
        name = "Breakfast",
        meals = listOf(meal, meal, meal)
    ),
    MealType(
        name = "Lunch",
        meals = listOf(meal, meal)
    ),
    MealType(
        name = "Dinner",
        meals = listOf(meal)
    )
)

data class MealType(
    val name: String,
    val meals: List<Meal> = emptyList()
)

@Composable
fun MealPlanItem(
    mealType: MealType
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = mealType.name, style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = {
            }) {
                Icon(
                    modifier = Modifier.size(32.dp),
                    painter = painterResource(id = R.drawable.add_circle),
                    contentDescription = null
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(mealType.meals) { meal ->
                PlanMealItem(meal = meal)
            }
        }
    }
}

@Composable
fun DayItemCard(
    day: String,
    date: String,
    onClick: () -> Unit = {},
    selected: Boolean = false
) {
    Card(
        Modifier
            .width(70.dp)
            .height(70.dp)
            .padding(2.dp)
            .clickable {
                onClick()
            },
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = day,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (selected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                Text(
                    text = date,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (selected) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
            }
        }
    }
}

@Composable
fun PlanMealItem(
    meal: Meal,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Card(
            modifier = modifier
                .width(180.dp)
                .wrapContentHeight(),
            shape = Shapes.large,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    contentDescription = null,
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current)
                            .data(data = meal.imageUrl)
                            .apply(block = fun ImageRequest.Builder.() {
                                placeholder(R.drawable.food_loading)
                            }).build()
                    ),
                    contentScale = ContentScale.Crop
                )

                Text(
                    modifier = Modifier.padding(4.dp),
                    text = meal.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        Box(
            modifier = Modifier
                .size(34.dp)
                .align(Alignment.TopEnd)
                .background(
                    color = PrimaryColor,
                    shape = RoundedCornerShape(
                        topStart = 0.dp,
                        bottomStart = 12.dp,
                        topEnd = 12.dp,
                        bottomEnd = 0.dp
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            IconButton(onClick = {
            }) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Default.Close,
                    contentDescription = null,
                    tint = Color.White
                )
            }
        }
    }
}

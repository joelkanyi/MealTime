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
package com.kanyideveloper.presentation.home.onlinemeal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kanyideveloper.compose_ui.theme.LightGrey
import com.kanyideveloper.compose_ui.theme.MainOrange
import com.kanyideveloper.domain.model.Category
import com.kanyideveloper.domain.model.FeaturedMeal
import com.kanyideveloper.mealtime.core.R
import com.kanyideveloper.presentation.home.HomeNavigator
import com.kanyideveloper.presentation.home.onlinemeal.state.CategoriesState
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun OnlineMealScreen(
    navigator: HomeNavigator,
    viewModel: OnlineMealViewModel = hiltViewModel()
) {
    val meals = remember { featuredMeals }
    val categoriesState = viewModel.categories.value

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            SectionHeader("Feature Meals")
        }
        item(span = { GridItemSpan(2) }) {
            FeaturedMeals()
        }
        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(16.dp))
        }
        item(span = { GridItemSpan(2) }) {
            MealCategorySelection(state = categoriesState)
        }
        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(8.dp))
        }
        items(meals.size) {
            OnlineMealCard(meals[it])
        }
    }
}

@Composable
fun OnlineMealCard(meal: FeaturedMeal) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .height(220.dp)
            .padding(5.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 2.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f),
                contentDescription = null,
                painter = painterResource(id = meal.imageUrl),
                contentScale = ContentScale.Crop
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Card(
                    modifier = Modifier
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = 0.dp,
                    backgroundColor = LightGrey.copy(alpha = 0.8f)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 3.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = R.drawable.ic_clock),
                            tint = MainOrange,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            modifier = Modifier.padding(vertical = 3.dp),
                            text = "${meal.cookingTime} Mins",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Card(
                    modifier = Modifier
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp),
                    elevation = 0.dp,
                    backgroundColor = LightGrey.copy(alpha = 0.8f)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 3.dp, horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            painter = painterResource(id = R.drawable.ic_favorites),
                            tint = MainOrange,
                            contentDescription = null
                        )
                    }
                }
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .align(Alignment.BottomStart),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 3.dp),
                    text = meal.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun MealCategorySelection(
    state: CategoriesState
) {
    var selectedIndex by remember { mutableStateOf(0) }
    val onItemClick = { index: Int -> selectedIndex = index }
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        items(state.categories) { category ->
            CategoryItem(
                category = category,
                onClick = onItemClick
            )
        }
    }
}

@Composable
fun CategoryItem(category: Category, onClick: (Int) -> Unit = {}) {
    val selected = true
    Column(
        modifier = Modifier
            .wrapContentWidth()
            .padding(5.dp)
            .wrapContentHeight()
    ) {
        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    // onClick()
                }
        ) {
            Text(
                text = category.categoryName,
                Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
                    .padding(1.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (selected) Color(0xFFfa4a0c) else Color(0xfff0f5f4))
                    .padding(10.dp),
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (selected) Color(0xffe2f1f3) else Color(0xff121212)
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        modifier = Modifier.padding(vertical = 5.dp),
        text = title,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun FeaturedMeals() {
    val featuredMeals = remember { featuredMeals }
    LazyRow {
        items(items = featuredMeals, itemContent = { meal ->
            MealCard(meal)
        })
    }
}

@Composable
fun MealCard(featuredMeal: FeaturedMeal) {
    Column(
        modifier = Modifier
            .width(300.dp)
            .height(200.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter
        ) {
            val painter = painterResource(id = featuredMeal.imageUrl)
            Image(
                painter = painter,
                contentDescription = "",
                Modifier
                    .width(300.dp)
                    .height(250.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(5.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xff000000).copy(alpha = 0.70F))
                    .padding(horizontal = 15.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 10.dp),
                    text = featuredMeal.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 1.dp)
                                .size(40.dp)
                                .clip(CircleShape)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.meal_banner),
                                contentDescription = "",
                                Modifier
                                    .fillMaxSize()
                                    .size(90.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = featuredMeal.chef,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Light,
                            color = Color.White
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(5.dp)
                                .clip(CircleShape)
                                .padding(5.dp),
                            tint = Color(0xFFfa4a0c)
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 5.dp),
                            text = "${featuredMeal.cookingTime} Mins",
                            fontWeight = FontWeight.Light,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

private val featuredMeals = listOf(
    FeaturedMeal(
        "Beef Steak & Cheese",
        com.kanyideveloper.mealtime.core.R.drawable.meal_banner,
        50,
        "Favy Fay"
    ),
    FeaturedMeal("Chicken Asian Cuisine", R.drawable.meal_banner, 40, "Nyar Mkamba"),
    FeaturedMeal("Japanese Chicken", R.drawable.meal_banner, 45, "Jerim Kaura"),
    FeaturedMeal(
        "Rice Egg with Chicken Salad",
        R.drawable.meal_banner,
        35,
        "Royal Mum"
    )
)

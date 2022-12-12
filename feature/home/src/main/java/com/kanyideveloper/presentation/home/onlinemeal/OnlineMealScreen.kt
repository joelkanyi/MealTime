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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.kanyideveloper.compose_ui.theme.MainOrange
import com.kanyideveloper.compose_ui.theme.MyLightOrange
import com.kanyideveloper.domain.model.Category
import com.kanyideveloper.domain.model.OnlineMeal
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
    val meals = viewModel.meals.value
    val categoriesState = viewModel.categories.value

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            CategorySelection(
                state = categoriesState,
                viewModel = viewModel
            )
        }
        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(16.dp))
        }
        items(meals.meals) { meal ->
            OnlineMealItem(meal = meal)
        }
    }
}

@Composable
fun OnlineMealItem(
    meal: OnlineMeal,
    isFavorite: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxSize()
            .height(220.dp)
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = Color.White,
        elevation = 2.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f),
                contentDescription = null,
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = meal.imageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                        }).build()
                ),
                contentScale = ContentScale.Crop
            )
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 12.dp)
                    .align(Alignment.BottomStart),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .padding(vertical = 3.dp),
                    text = meal.name,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = {
                }) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp),
                        painter = painterResource(
                            id = if (isFavorite) {
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

@Composable
fun CategorySelection(
    state: CategoriesState,
    viewModel: OnlineMealViewModel
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        items(state.categories) { category ->
            CategoryItem(
                category = category,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun CategoryItem(
    category: Category,
    viewModel: OnlineMealViewModel
) {
    val selected = viewModel.selectedCategory.value == category.categoryName
    Card(
        Modifier
            .width(100.dp)
            .wrapContentHeight()
            .clickable {
                viewModel.setSelectedCategory(category.categoryName)
                viewModel.getMeals(viewModel.selectedCategory.value)
            },
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        backgroundColor = if (selected) {
            MainOrange.copy(alpha = .6f)
        } else {
            MyLightOrange
        }
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                modifier = Modifier
                    .width(100.dp)
                    .height(50.dp),
                contentDescription = null,
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(data = category.categoryImageUrl)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                        }).build()
                ),
                contentScale = ContentScale.Inside
            )
            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = category.categoryName,
                textAlign = TextAlign.Center,
                fontSize = 15.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = if (selected) {
                    Color.White
                } else {
                    Color.Black
                }
            )
        }
    }
}

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
package com.kanyideveloper.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanyideveloper.compose_ui.theme.LightGrey
import com.kanyideveloper.compose_ui.theme.MainOrange
import com.kanyideveloper.destinations.DetailsScreenDestination
import com.kanyideveloper.home.presentation.home.HomeNavigator
import com.kanyideveloper.mealtime.core.R
import com.ramcosta.composedestinations.annotation.Destination
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState


@Destination
@Composable
fun DetailsScreen(
    navigator: HomeNavigator,
) {
    val state = rememberCollapsingToolbarScaffoldState()
    val textSize = (18 + (4) * state.toolbarState.progress).sp


    DetailsScreenDestination

    Column(Modifier.fillMaxSize()) {


        if (textSize == 18.sp) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(color = Color.Transparent)
            )
        }
        CollapsingToolbarScaffold(
            modifier = Modifier,
            state = state,
            scrollStrategy = ScrollStrategy.EnterAlways,
            toolbar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp)
                        .pin()
                        .background(color = MaterialTheme.colors.primary)
                )

                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    painter = painterResource(id = R.drawable.meal_banner),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    alpha = if (textSize.value == 18f) 0f else 1f
                )

                Box(
                    Modifier
                        .fillMaxWidth()
                        .background(
                            if (textSize == 22.sp) {
                                Color.Black.copy(alpha = 0.3f)
                            } else {
                                Color.Transparent
                            }
                        )
                        .road(
                            whenCollapsed = Alignment.TopStart,
                            whenExpanded = Alignment.BottomStart
                        )
                ) {
                    Row(
                        Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (textSize.value == 18f) {
                            IconButton(onClick = {
                                //navigator.popBackStack()
                            }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = null,
                                    tint = Color.White
                                )
                            }
                        }
                        if (textSize == 18.sp) {
                            Text(
                                "Rice Chicken Rice Chicken Rice Chicken Rice Chicken",
                                style = TextStyle(color = Color.White, fontSize = textSize),
                                modifier = Modifier
                                    .padding(10.dp)
                            )
                        }
                    }
                }
            },
            body = {
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)) {
                    item {
                        Row(Modifier.fillMaxWidth()) {
                            Text(
                                modifier = Modifier.fillMaxWidth(0.85f),
                                text = "Rice Chicken Rice Chicken Rice Chicken Rice Chicken",
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            )
                            Box(
                                modifier = Modifier
                                    .size(50.dp)
                                    .clip(CircleShape)
                                    .background(
                                        LightGrey
                                    )
                            ) {
                                Icon(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .align(Alignment.Center)
                                        .padding(0.dp),
                                    imageVector = Icons.Default.Favorite,
                                    contentDescription = null,
                                    tint = MainOrange
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                    item {
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
                                        text = "30 mins",
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
                                        text = "3 Serving",
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
                                                .size(24.dp)
                                                .align(Alignment.Center)
                                                .padding(0.dp),
                                            painter = painterResource(id = R.drawable.noun_easy),
                                            contentDescription = null,
                                            tint = Color.Black
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "Easy",
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
                                        text = "Drink",
                                        fontSize = 14.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Ingredients",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )

                        for (i in 1..5) {
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
                                    text = "50g sugar",
                                    fontWeight = FontWeight.Light,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Cooking Directions",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )

                        for (i in 1..7) {
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
                                    text = "Put a glass full of water",
                                    fontWeight = FontWeight.Light,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}


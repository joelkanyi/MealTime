package com.kanyideveloper.mealtime.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.GridItemSpan
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
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
import com.kanyideveloper.mealtime.R
import com.kanyideveloper.mealtime.screens.components.StandardToolbar
import com.kanyideveloper.mealtime.ui.theme.*
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {
    Column(
        Modifier.fillMaxSize()
    ) {
        StandardToolbar(
            navigator = navigator,
            title = {
                Image(
                    painter = painterResource(id = R.drawable.ic_meal_time_banner),
                    contentDescription = null
                )
            },
            modifier = Modifier.fillMaxWidth(),
            showBackArrow = false,
            navActions = {

            }
        )

        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                CustomTabs()
            }
            item(span = { GridItemSpan(2) }) {
                Text(
                    modifier = Modifier.padding(vertical = 8.dp),
                    text = "Categories",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            item(span = { GridItemSpan(2) }) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(10) {
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MyLightOrange),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    modifier = Modifier.size(32.dp),
                                    tint = MainOrange,
                                    painter = painterResource(id = R.drawable.ic_pizza_thin),

                                    contentDescription = null
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Pizza",
                                    textAlign = TextAlign.Center,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(5.dp))
                                Text(
                                    text = "191 items",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraLight
                                )
                            }
                        }
                    }
                }
            }
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item(span = { GridItemSpan(2) }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .height(220.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = 0.dp
                ) {
                    Box(Modifier.fillMaxSize()) {
                        Image(
                            modifier = Modifier.fillMaxSize(),
                            painter = painterResource(id = R.drawable.meal_banner),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
            item(span = { GridItemSpan(2) }) {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item(span = { GridItemSpan(2) }) {
                Text(
                    modifier = Modifier.padding(vertical = 3.dp),
                    text = "Meals",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            items(10) {
                MealItem()
            }
        }
    }
}

@Composable
fun MealItem(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxSize()
            .height(220.dp)
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(12.dp),
        backgroundColor = MyLightBlue,
        elevation = 2.dp
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.75f),
                contentDescription = null,
                painter = painterResource(id = R.drawable.meal_banner),
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
                            text = "3 Mins",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(40.dp)
                        .background(LightGrey.copy(alpha = 0.8f))
                ) {

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
                    text = "Very Long Food Name To Be Here",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(

                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                        },
                    imageVector = Icons.Default.Favorite,
                    tint = MainOrange,
                    contentDescription = null
                )
            }

        }
    }
}

@Composable
fun CustomTabs() {
    var selectedIndex by remember { mutableStateOf(0) }

    val list = listOf("My Meals", "Online Recipes")

    TabRow(selectedTabIndex = selectedIndex,
        backgroundColor = MainOrange,
        modifier = Modifier
            .padding(vertical = 8.dp)
            .clip(RoundedCornerShape(50)),
        indicator = {
            Box {}
        }
    ) {
        list.forEachIndexed { index, text ->
            val selected = selectedIndex == index
            Tab(
                modifier = if (selected) Modifier
                    .padding(3.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        Color.White
                    )
                else Modifier
                    .padding(3.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        MainOrange
                    ),
                selected = selected,
                onClick = { selectedIndex = index },
                text = { Text(text = text, color = Color.Black) }
            )
        }
    }
}
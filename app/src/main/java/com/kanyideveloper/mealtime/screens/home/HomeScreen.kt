package com.kanyideveloper.mealtime.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.GridItemSpan
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
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
import com.kanyideveloper.mealtime.R
import com.kanyideveloper.mealtime.model.MealCategory
import com.kanyideveloper.mealtime.screens.components.StandardToolbar
import com.kanyideveloper.mealtime.screens.destinations.DetailsScreenDestination
import com.kanyideveloper.mealtime.ui.theme.LightGrey
import com.kanyideveloper.mealtime.ui.theme.MainOrange
import com.kanyideveloper.mealtime.ui.theme.MyLightBlue
import com.kanyideveloper.mealtime.ui.theme.MyLightOrange
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalFoundationApi::class)
@Destination(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator
) {

    var showRandomMeal by remember {
        mutableStateOf(false)
    }

    var isMyMeal by remember {
        mutableStateOf(true)
    }

    Scaffold(
        floatingActionButton = {
            if (isMyMeal) {
                FloatingActionButton(
                    modifier = Modifier
                        .height(50.dp),
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = MainOrange,
                    content = {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.fork_knife_thin),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "Add Meal")
                        }
                    },
                    onClick = {

                    },
                )
            }
        }
    ) { padding ->
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
                        modifier = Modifier.padding(vertical = 5.dp),
                        text = "Categories",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                item(span = { GridItemSpan(2) }) {

                    val mealCategories = listOf(
                        MealCategory(
                            "Food",
                            R.drawable.ic_food
                        ),
                        MealCategory(
                            "Breakfast",
                            R.drawable.ic_breakfast
                        ),
                        MealCategory(
                            "Drinks",
                            R.drawable.ic_drinks
                        ),
                        MealCategory(
                            "Fruits",
                            R.drawable.ic_fruit
                        ),
                        MealCategory(
                            "Fast Food",
                            R.drawable.ic_pizza_thin
                        )
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(mealCategories) { meal ->
                            Box(
                                modifier = Modifier
                                    .size(70.dp)
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
                                        painter = painterResource(id = meal.icon),

                                        contentDescription = null
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = meal.name,
                                        textAlign = TextAlign.Center,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
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
                            .height(200.dp),
                        shape = RoundedCornerShape(12.dp),
                        elevation = 0.dp
                    ) {
                        Box(Modifier.fillMaxSize()) {
                            Image(
                                modifier = Modifier.fillMaxSize(),
                                painter = painterResource(id = R.drawable.randomize_meals),
                                contentDescription = null,
                                contentScale = ContentScale.Crop
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(16.dp),
                            ) {
                                Text(
                                    text = "What to cook for lunch?",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                Button(onClick = {
                                    showRandomMeal = true
                                }) {
                                    Text(
                                        text = "Get a Random Meal",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }

                if (showRandomMeal) {
                    item(span = { GridItemSpan(2) }) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .height(180.dp),
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

                                Card(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .clickable {
                                            showRandomMeal = false
                                        },
                                    shape = RoundedCornerShape(8.dp),
                                    elevation = 0.dp,
                                    backgroundColor = Color.Red.copy(alpha = 0.8f)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(vertical = 3.dp, horizontal = 12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            modifier = Modifier.size(18.dp),
                                            imageVector = Icons.Default.Close,
                                            tint = Color.White,
                                            contentDescription = null
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            modifier = Modifier.padding(vertical = 3.dp),
                                            text = "Dismiss",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }

                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .align(Alignment.BottomEnd)
                                        .background(Color.Black.copy(alpha = 0.6f))
                                        .padding(5.dp),
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        text = "Rice Chicken with Chapati",
                                        textAlign = TextAlign.Center,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(30.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .padding(vertical = 3.dp, horizontal = 3.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                modifier = Modifier.size(24.dp),
                                                painter = painterResource(id = R.drawable.ic_clock),
                                                tint = MainOrange,
                                                contentDescription = null
                                            )
                                            Spacer(modifier = Modifier.width(5.dp))
                                            Text(
                                                modifier = Modifier.padding(vertical = 3.dp),
                                                text = "3 Mins",
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Light,
                                                color = Color.White
                                            )
                                        }
                                    }

                                }
                            }
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
                    MealItem(
                        modifier = Modifier.clickable {
                            navigator.navigate(DetailsScreenDestination)
                        }
                    )
                }
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
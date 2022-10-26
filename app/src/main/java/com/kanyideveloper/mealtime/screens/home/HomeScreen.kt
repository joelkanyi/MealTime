package com.kanyideveloper.mealtime.screens.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanyideveloper.mealtime.R
import com.kanyideveloper.mealtime.model.FeaturedMeal
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
                        // navigator.navigate(AddMealsScreenDestination)
                    },
                )
            }
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                color = MaterialTheme.colors.background
            ) {
                Column(Modifier.fillMaxSize()) {
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
                    CustomTabs(navigator)
                }
            }

        }
    )
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
fun CustomTabs(navigator: DestinationsNavigator) {
    var selectedIndex by remember { mutableStateOf(0) }

    val list = listOf("My Meals", "Online Recipes")

    TabRow(
        selectedTabIndex = selectedIndex,
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
    if (selectedIndex == 0) {
        MyMeals(navigator)
    } else {
        OnlineMeals()
    }
}

@Composable
fun OnlineMeals() {
    Column {
        SectionHeader("Feature Meals")
        FeaturedMeals()
        MealCategorySelection()
        PopularRecipes()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview(showBackground = true)
fun PopularRecipes() {
    val meals = remember { featuredMeals }

    LazyVerticalGrid(
        cells = GridCells.Fixed(2)
    ) {
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
fun MealCategorySelection() {
    val categories = listOf(
        "Breakfast Eatings",
        "Lunch Combo",
        "Late Evening Dinner ", "Snacks Time", "Dessert to Finish Up"
    )
    var selectedIndex by remember { mutableStateOf(0) }
    val onItemClick = { index: Int -> selectedIndex = index }
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        items(categories.size) { index ->
            CategoryItem(
                categories,
                index = index,
                selected = selectedIndex == index,
                onClick = onItemClick
            )
        }
    }
}

@Composable
fun CategoryItem(categories: List<String>, index: Int, selected: Boolean, onClick: (Int) -> Unit) {
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
                    onClick.invoke(index)
                }) {
            Text(
                text = categories[index],
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MyMeals(navigator: DestinationsNavigator) {
    var showRandomMeal by remember {
        mutableStateOf(false)
    }
    LazyVerticalGrid(
        cells = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
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
                painter = painter, contentDescription = "",
                Modifier
                    .width(300.dp)
                    .height(250.dp)
                    .padding(5.dp)
                    .clip(RoundedCornerShape(25.dp))
                    .fillMaxSize(), contentScale = ContentScale.Crop
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
    FeaturedMeal("Beef Steak & Cheese", R.drawable.beef_steak_with_cheese, 50, "Favy Fay"),
    FeaturedMeal("Chicken Asian Cuisine", R.drawable.chicken_asian_cusine, 40, "Nyar Mkamba"),
    FeaturedMeal("Japanese Chicken", R.drawable.japanese_chicken, 45, "Jerim Kaura"),
    FeaturedMeal(
        "Rice Egg with Chicken Salad",
        R.drawable.rice_egg_with_chicken_salad,
        35,
        "Royal Mum"
    ),
)
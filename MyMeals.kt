@Composable
fun MyMeals(navigator: DestinationsNavigator) {
    var showRandomMeal by remember {
        mutableStateOf(false)
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
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
                                        .padding(vertical = 3.dp,
                                            horizontal = 3.dp),
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
fun OnlineMeals(viewModel: HomeViewModel) {
    Column {
        SectionHeader("Feature Meals")
        FeaturedMeals(viewModel = viewModel)
        MealCategorySelection()
        PopularRecipes(viewModel = viewModel)
    }
}
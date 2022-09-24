package com.kanyideveloper.mealtime.screens.favorites

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanyideveloper.mealtime.R
import com.kanyideveloper.mealtime.screens.components.StandardToolbar
import com.kanyideveloper.mealtime.ui.theme.MainOrange
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun FavoritesScreen(
    navigator: DestinationsNavigator
) {
    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigator = navigator,
            title = {
                Text(text = "Favorite meals", fontSize = 18.sp)
            },
            showBackArrow = false,
            navActions = {

            }
        )

        LazyColumn {
            items(20) {
                FoodItem()
            }
        }
    }
}

@Composable
fun FoodItem(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(horizontal = 8.dp, vertical = 5.dp),
        elevation = 8.dp,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.fillMaxWidth()) {
            Image(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .fillMaxHeight(),
                contentDescription = null,
                painter = painterResource(id = R.drawable.meal_banner),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                Modifier.fillMaxSize()
            ) {
                Icon(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    imageVector = Icons.Default.Favorite,
                    tint = MainOrange,
                    contentDescription = null
                )
                Column(
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center), verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Rice Chicken Rice Chicken Rice Chicken Rice Chicken Rice Chicken",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_clock),
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "15 Minutes",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Light
                        )
                    }
                }
            }
        }
    }
}
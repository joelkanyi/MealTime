package com.kanyideveloper.mealtime.screens.add_meal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanyideveloper.mealtime.R
import com.kanyideveloper.mealtime.screens.components.StandardToolbar
import com.kanyideveloper.mealtime.screens.favorites.FoodItem
import com.kanyideveloper.mealtime.ui.theme.LightGrey
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun AddMealScreen(
    navigator: DestinationsNavigator
) {
    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigator = navigator,
            title = {
                Text(text = "Add meal", fontSize = 18.sp)
            },
            showBackArrow = false,
            navActions = {

            }
        )

        LazyColumn {
            item {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(LightGrey),
                    contentAlignment = Alignment.Center
                ){
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null)
                }
            }
        }
    }
}
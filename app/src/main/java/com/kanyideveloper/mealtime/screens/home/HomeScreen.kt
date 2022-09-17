package com.kanyideveloper.mealtime.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.kanyideveloper.mealtime.R
import com.kanyideveloper.mealtime.screens.components.StandardToolbar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

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
                Image(painter = painterResource(id = R.drawable.ic_meal_time_banner), contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            showBackArrow = false,
            navActions = {

            }
        )
    }
}
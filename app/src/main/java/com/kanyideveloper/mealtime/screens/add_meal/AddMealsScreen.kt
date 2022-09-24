package com.kanyideveloper.mealtime.screens.add_meal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.kanyideveloper.mealtime.ui.theme.MealTimeTheme
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun AddMealsScreen() {
    
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MealTimeTheme {
        Column(Modifier.fillMaxSize()) {
            
        }
    }
}
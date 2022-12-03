package com.kanyideveloper.mealtime.screens.home

import androidx.lifecycle.ViewModel
import com.kanyideveloper.mealtime.R
import com.kanyideveloper.mealtime.data.repository.HomeRepository
import com.kanyideveloper.mealtime.model.FeaturedMeal
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    val featuredMeals = listOf(
        FeaturedMeal("Beef Steak & Cheese",
            R.drawable.beef_steak_with_cheese,
            50,
            "Favy Fay"),
        FeaturedMeal("Chicken Asian Cuisine",
            R.drawable.chicken_asian_cusine,
            40,
            "Nyar Mkamba"),
        FeaturedMeal("Japanese Chicken",
            R.drawable.japanese_chicken,
            45,
            "Jerim Kaura"),
        FeaturedMeal(
            "Rice Egg with Chicken Salad",
            R.drawable.rice_egg_with_chicken_salad,
            35,
            "Royal Mum"
        ),
    )
}
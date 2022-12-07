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
package com.kanyideveloper.home.presentation.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    // private val homeRepository: HomeRepository
) : ViewModel() {

    private val _isMyMeal = mutableStateOf(true)
    val isMyMeal: State<Boolean> = _isMyMeal
    fun setIsMyMeal(value: Boolean) {
        _isMyMeal.value = value
    }

/*    val featuredMeals = listOf(
        FeaturedMeal(
            "Beef Steak & Cheese",
            R.drawable.beef_steak_with_cheese,
            50,
            "Favy Fay"
        ),
        FeaturedMeal(
            "Chicken Asian Cuisine",
            R.drawable.chicken_asian_cusine,
            40,
            "Nyar Mkamba"
        ),
        FeaturedMeal(
            "Japanese Chicken",
            R.drawable.japanese_chicken,
            45,
            "Jerim Kaura"
        ),
        FeaturedMeal(
            "Rice Egg with Chicken Salad",
            R.drawable.rice_egg_with_chicken_salad,
            35,
            "Royal Mum"
        )
    )*/
}

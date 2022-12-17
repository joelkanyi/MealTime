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
package com.kanyideveloper.presentation.home.onlinemeal

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.kanyideveloper.domain.model.OnlineMeal
import com.kanyideveloper.presentation.home.onlinemeal.state.CategoriesState
import com.kanyideveloper.presentation.home.onlinemeal.state.MealState
import org.junit.Rule
import org.junit.Test

class OnlineMealScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun testShowLoadingStateComponent_onLoading() {
        // Given
        val categoriesState = CategoriesState(
            isLoading = false,
            error = null,
            categories = emptyList()
        )

        val mealState = MealState(
            isLoading = true,
            error = null,
            meals = emptyList()
        )

        // When
        composeTestRule.setContent {
            OnlineMealScreenContent(
                categoriesState = categoriesState,
                selectedCategory = "Beef",
                mealsState = mealState,
                onSelectCategory = {},
                onMealClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithTag("Loading Component").assertExists()
        composeTestRule.onNodeWithText("Nothing found here!").assertDoesNotExist()
        composeTestRule.onNodeWithTag("Error State Component").assertDoesNotExist()
    }

    @Test
    fun testShowErrorStateComponent_onError() {
        // Given
        val categoriesState = CategoriesState(
            isLoading = false,
            error = null,
            categories = emptyList()
        )

        val mealState = MealState(
            isLoading = false,
            error = "A Network Error Occurred",
            meals = emptyList()
        )

        // When
        composeTestRule.setContent {
            OnlineMealScreenContent(
                categoriesState = categoriesState,
                selectedCategory = "Beef",
                mealsState = mealState,
                onSelectCategory = {},
                onMealClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithTag("Loading Component").assertDoesNotExist()
        composeTestRule.onNodeWithText("Nothing found here!").assertDoesNotExist()
        composeTestRule.onNodeWithTag("Error State Component").assertExists()
    }

    @Test
    fun testShowEmptyStateComponent_whenEmptyMeals() {
        // Given
        val categoriesState = CategoriesState(
            isLoading = false,
            error = null,
            categories = emptyList()
        )

        val mealState = MealState(
            isLoading = false,
            error = null,
            meals = emptyList()
        )

        // When
        composeTestRule.setContent {
            OnlineMealScreenContent(
                categoriesState = categoriesState,
                selectedCategory = "Beef",
                mealsState = mealState,
                onSelectCategory = {},
                onMealClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithTag("Loading Component").assertDoesNotExist()
        composeTestRule.onNodeWithText("Nothing found here!").assertExists()
        composeTestRule.onNodeWithTag("Error State Component").assertDoesNotExist()
    }

    @Test
    fun testShowMeals_onSuccess() {
        // Given
        val categoriesState = CategoriesState(
            isLoading = false,
            error = null,
            categories = emptyList()
        )

        val mealState = MealState(
            isLoading = false,
            error = null,
            meals = testMeals
        )

        // When
        composeTestRule.setContent {
            OnlineMealScreenContent(
                categoriesState = categoriesState,
                selectedCategory = "Beef",
                mealsState = mealState,
                onSelectCategory = {},
                onMealClick = {}
            )
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Beef and Mustard Pie").assertExists()
        composeTestRule.onNodeWithText("Beef and Mustard Pie").assertExists()
    }
}

private val testMeals = listOf(
    OnlineMeal(
        name = "Beef and Mustard Pie",
        imageUrl = "https://www.themealdb.com/images/category/breakfast.png",
        mealId = "53050"
    ),
    OnlineMeal(
        name = "Beef and Oyster Pie",
        imageUrl = "https://www.themealdb.com/images/category/breakfast.png",
        mealId = "53051"
    ),
    OnlineMeal(
        name = "Beef and Cauliflower",
        imageUrl = "https://www.themealdb.com/images/category/breakfast.png",
        mealId = "53052"
    )
)

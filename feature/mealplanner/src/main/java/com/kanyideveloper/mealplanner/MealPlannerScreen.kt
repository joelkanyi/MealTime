/*
 * Copyright 2023 Joel Kanyi.
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
package com.kanyideveloper.mealplanner

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.core.components.EmptyStateComponent
import com.kanyideveloper.mealtime.core.R
import com.ramcosta.composedestinations.annotation.Destination

interface MealPlannerNavigator {
    fun popBackStack()
    fun openAllergiesScreen()
    fun openNoOfPeopleScreen()
    fun openMealTypesScreen()
    fun openMealPlanner()
}

@Destination
@Composable
fun MealPlannerScreen(
    navigator: MealPlannerNavigator
) {
    val hasMealPlan = false
    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigate = {
                navigator.popBackStack()
            },
            title = {
                Text(text = "Meal Planner", fontSize = 18.sp)
            },
            showBackArrow = false,
            navActions = {
            }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            if (!hasMealPlan) {
                EmptyStateComponent(
                    anim = R.raw.women_thinking,
                    message = "Looks like you haven't created a meal plan yet",
                    content = {
                        Spacer(modifier = Modifier.height(32.dp))
                        Button(onClick = {
                            navigator.openAllergiesScreen()
                        }) {
                            Text(text = "Get Started")
                        }
                    }
                )
            }

            if (hasMealPlan) {
                LazyColumn {
                }
            }
        }
    }
}

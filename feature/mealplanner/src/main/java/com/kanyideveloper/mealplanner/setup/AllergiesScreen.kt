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
package com.kanyideveloper.mealplanner.setup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.compose_ui.theme.PrimaryColor
import com.kanyideveloper.mealplanner.MealPlannerNavigator
import com.ramcosta.composedestinations.annotation.Destination

@Destination
@Composable
fun AllergiesScreen(
    navigator: MealPlannerNavigator,
    viewModel: SetupViewModel = hiltViewModel()
) {
    AllergiesScreenContent(
        navigator = navigator,
        allergiesChoices = viewModel.allergies,
        allergies = viewModel.gson.toJson(viewModel.allergicTo),
        onCheck = { allergy ->
            viewModel.insertAllergicTo(allergy)
        },
        isChecked = { allergy ->
            viewModel.allergicTo.contains(allergy)
        }
    )
}

@Composable
private fun AllergiesScreenContent(
    navigator: MealPlannerNavigator,
    allergies: String,
    allergiesChoices: List<String>,
    isChecked: (String) -> Boolean,
    onCheck: (String) -> Unit
) {
    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigate = {
                navigator.popBackStack()
            },
            title = {},
            showBackArrow = true,
            navActions = {
                Text(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .clickable {
                            navigator.openNoOfPeopleScreen(
                                allergies = allergies
                            )
                        },
                    text = "Next",
                    style = MaterialTheme.typography.titleMedium,
                    color = PrimaryColor
                )
            }
        )

        LazyVerticalGrid(
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            columns = GridCells.Fixed(3)
        ) {
            item(span = { GridItemSpan(currentLineSpan = 3) }) {
                Text(
                    text = "Allergies/ dietary restrictions",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item(span = { GridItemSpan(currentLineSpan = 3) }) {
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(allergiesChoices) { allergy ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = isChecked(allergy),
                        onCheckedChange = {
                            onCheck(allergy)
                        }
                    )
                    Text(
                        text = allergy,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
        }
    }
}

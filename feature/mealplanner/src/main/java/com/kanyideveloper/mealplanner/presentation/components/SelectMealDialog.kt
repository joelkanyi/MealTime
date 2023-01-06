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
package com.kanyideveloper.mealplanner.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kanyidev.searchable_dropdown.SearchableExpandedDropDownMenu
import com.kanyideveloper.core.components.LoadingStateComponent
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.core.util.LottieAnim
import com.kanyideveloper.mealtime.core.R

@Composable
fun SelectMealDialog(
    onDismiss: () -> Unit,
    mealType: String,
    onClickAdd: (Meal, String) -> Unit,
    meals: List<Meal>,
    onSearchClicked: () -> Unit,
    onSearchValueChange: (String) -> Unit,
    currentSearchString: String,
    onSearchByChange: (String) -> Unit,
    onSourceChange: (String) -> Unit,
    sources: List<String>,
    searchOptions: List<String>,
    currentSource: String,
    isLoading: Boolean = false,
    error: String? = null
) {
    AlertDialog(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(.9f),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = mealType,
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Box(modifier = Modifier.fillMaxSize()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item(span = { GridItemSpan(2) }) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Source",
                                style = MaterialTheme.typography.labelMedium
                            )

                            SearchableExpandedDropDownMenu(
                                listOfItems = sources,
                                modifier = Modifier.fillMaxWidth(),
                                onDropDownItemSelected = { item ->
                                    onSourceChange(item)
                                },
                                dropdownItem = { category ->
                                    Text(text = category, color = Color.Black)
                                },
                                parentTextFieldCornerRadius = 4.dp
                            )
                        }
                    }

                    if (currentSource == "Online") {
                        item(span = { GridItemSpan(2) }) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "Search By",
                                    style = MaterialTheme.typography.labelMedium
                                )

                                SearchableExpandedDropDownMenu(
                                    listOfItems = searchOptions,
                                    modifier = Modifier.fillMaxWidth(),
                                    onDropDownItemSelected = { item ->
                                        onSearchByChange(item)
                                    },
                                    dropdownItem = { category ->
                                        Text(text = category, color = Color.Black)
                                    },
                                    parentTextFieldCornerRadius = 4.dp
                                )
                            }
                        }

                        item(span = { GridItemSpan(2) }) {
                            SearchBarComponent(
                                onSearchClicked = onSearchClicked,
                                onSearchValueChange = onSearchValueChange,
                                textFieldCurrentText = currentSearchString
                            )
                        }
                    }

                    if (meals.isEmpty() || error != null) {
                        item(span = { GridItemSpan(2) }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .align(
                                        Alignment.Center
                                    )
                                    .padding(16.dp)
                                    .testTag("Empty State Component"),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LottieAnim(
                                    resId = R.raw.search_empty,
                                    height = 150.dp
                                )
                                Text(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    text = error ?: "",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                    item(span = { GridItemSpan(2) }) {
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    items(meals) { searchMeal ->
                        PlanMealItem(
                            meal = searchMeal,
                            cardWidth = 160.dp,
                            imageHeight = 80.dp,
                            isAddingToPlan = true,
                            isAdded = false,
                            onClickAdd = onClickAdd,
                            type = mealType,
                            onRemoveClick = { mealId ->
                            }
                        )
                    }
                }

                if (isLoading) {
                    LoadingStateComponent()
                }
            }
        },
        confirmButton = {}
    )
}

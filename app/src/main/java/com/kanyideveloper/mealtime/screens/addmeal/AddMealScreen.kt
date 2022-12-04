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
package com.kanyideveloper.mealtime.screens.addmeal

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanyidev.searchable_dropdown.SearchableExpandedDropDownMenu
import com.kanyideveloper.mealtime.R
import com.kanyideveloper.mealtime.screens.components.StandardToolbar
import com.kanyideveloper.mealtime.screens.destinations.NextAddMealScreenDestination
import com.kanyideveloper.mealtime.ui.theme.LightGrey
import com.kanyideveloper.mealtime.ui.theme.MainOrange
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AddMealScreen(
    navigator: DestinationsNavigator
) {
    val context = LocalContext.current

    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigator = navigator,
            title = {
                Text(text = "Add meal", fontSize = 18.sp)
            },
            showBackArrow = true,
            navActions = {
            }
        )

        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(LightGrey),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = "Meal Name", fontSize = 12.sp)
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = "",
                        onValueChange = {
                        },
                        placeholder = {
                            Text(text = "Meal Name")
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words
                        )
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val sports = mutableListOf(
                        Sport("Basketball", "🏀"),
                        Sport("Rugby", "🏉"),
                        Sport("Football", "⚽️"),
                        Sport("MMA", "🤼‍♂️"),
                        Sport("Motorsport", "🏁"),
                        Sport("Snooker", "🎱"),
                        Sport("Tennis", "🎾")
                    )

                    Column(
                        modifier = Modifier.fillMaxWidth(.5f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "Category", fontSize = 12.sp)

                        SearchableExpandedDropDownMenu(
                            listOfItems = sports,
                            modifier = Modifier.fillMaxWidth(),
                            onDropDownItemSelected = { item ->
                                Toast.makeText(
                                    context,
                                    item.name,
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            dropdownItem = { test ->
                                DropDownItem(test = test)
                            },
                            parentTextFieldCornerRadius = 4.dp
                        )
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "Cooking Complexity", fontSize = 12.sp)

                        SearchableExpandedDropDownMenu(
                            listOfItems = sports,
                            modifier = Modifier.fillMaxWidth(),
                            onDropDownItemSelected = { item ->
                                Toast.makeText(
                                    context,
                                    item.name,
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            dropdownItem = { test ->
                                DropDownItem(test = test)
                            },
                            parentTextFieldCornerRadius = 4.dp
                        )
                    }
                }
            }

            item {
                var sliderPosition by remember { mutableStateOf(0f) }
                val interactionSource = MutableInteractionSource()
                val colors =
                    SliderDefaults.colors(thumbColor = MainOrange, activeTrackColor = MainOrange)
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Cooking Time - ${sliderPosition.toInt()} Minutes",
                            fontSize = 14.sp
                        )
                    }
                    Slider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics {
                                contentDescription = "Localized Description"
                            },
                        value = sliderPosition,
                        onValueChange = {
                            sliderPosition = it
                        },
                        valueRange = 0f..300f,
                        onValueChangeFinished = {
                            // launch some business logic update with the state you hold
                            // viewModel.updateSelectedSliderValue(sliderPosition)
                        },
                        interactionSource = interactionSource,
                        thumb = {
                            SliderDefaults.Thumb(
                                interactionSource = interactionSource,
                                colors = colors
                            )
                        },
                        track = { sliderPositions ->
                            SliderDefaults.Track(
                                colors = colors,
                                sliderPositions = sliderPositions
                            )
                        }
                    )
                }
            }

            item {
                var sliderPosition by remember { mutableStateOf(0f) }
                val interactionSource = MutableInteractionSource()
                val colors =
                    SliderDefaults.colors(thumbColor = MainOrange, activeTrackColor = MainOrange)
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Serving - ${sliderPosition.toInt()} People",
                            fontSize = 14.sp
                        )
                    }
                    Slider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics {
                                contentDescription = "Localized Description"
                            },
                        value = sliderPosition,
                        onValueChange = {
                            sliderPosition = it
                        },
                        valueRange = 0f..300f,
                        onValueChangeFinished = {
                            // launch some business logic update with the state you hold
                            // viewModel.updateSelectedSliderValue(sliderPosition)
                        },
                        interactionSource = interactionSource,
                        thumb = {
                            SliderDefaults.Thumb(
                                interactionSource = interactionSource,
                                colors = colors
                            )
                        },
                        track = { sliderPositions ->
                            SliderDefaults.Track(
                                colors = colors,
                                sliderPositions = sliderPositions
                            )
                        }
                    )
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        navigator.navigate(NextAddMealScreenDestination)
                    },
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        text = "Next",
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}

@Composable
fun DropDownItem(test: Sport) {
    Row(
        modifier = Modifier
            .padding(8.dp)
            .wrapContentSize()
    ) {
        Text(text = test.emoji)
        Spacer(modifier = Modifier.width(12.dp))
        Text(test.name)
    }
}

data class Sport(
    val name: String,
    val emoji: String
) {
    override fun toString(): String {
        return "$emoji $name"
    }
}
/**
 * Select Image -DONE
 * Enter Food Title - DONE
 * Cooking Time - DONE
 * Serving People
 * Cooking Complexity - DONE
 * Category - DONE
 * Ingredients
 * Cooking Directions
 */

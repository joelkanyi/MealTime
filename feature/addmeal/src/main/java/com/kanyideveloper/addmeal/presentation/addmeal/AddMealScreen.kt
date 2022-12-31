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
package com.kanyideveloper.addmeal.presentation.addmeal

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kanyidev.searchable_dropdown.SearchableExpandedDropDownMenu
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.core.util.imageUriToImageBitmap
import com.ramcosta.composedestinations.annotation.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun AddMealScreen(
    navigator: AddMealNavigator,
    viewModel: AddMealsViewModel = hiltViewModel()
) {
    val mealName = viewModel.mealName.value
    val category = viewModel.category.value
    val complexity = viewModel.cookingComplexity.value
    val peopleServing = viewModel.peopleServing.value
    val cookingTime = viewModel.cookingTime.value

    val context = LocalContext.current

    val sliderInteractionSource = MutableInteractionSource()
    val sliderColors =
        SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.primary,
            activeTrackColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = .5f)
        )

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            viewModel.setMealImageUri(uri)
        }

    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigate = {
                navigator.popBackStack()
            },
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
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .height(210.dp)
                        .clickable {
                            galleryLauncher.launch("image/*")
                        }
                ) {
                    if (viewModel.mealImageUri.value == null) {
                        IconButton(onClick = {
                            galleryLauncher.launch("image/*")
                        }) {
                            Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }

                    // Selected Image
                    viewModel.mealImageUri.value?.let { uri ->
                        Image(
                            modifier = Modifier
                                .fillMaxSize(),
                            bitmap = context.imageUriToImageBitmap(uri).asImageBitmap(),
                            contentDescription = null,
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Meal Name",
                        style = MaterialTheme.typography.labelMedium
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = mealName.text,
                        onValueChange = {
                            viewModel.setMealNameState(value = it)
                        },
                        placeholder = {
                            Text(
                                text = "Meal Name",
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Text,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        isError = mealName.error != null
                    )
                    if (mealName.error != null) {
                        Text(
                            text = mealName.error ?: "",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(.5f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Category",
                            style = MaterialTheme.typography.labelMedium
                        )

                        SearchableExpandedDropDownMenu(
                            listOfItems = viewModel.categories,
                            modifier = Modifier.fillMaxWidth(),
                            onDropDownItemSelected = { item ->
                                viewModel.setCategory(item)
                            },
                            dropdownItem = { category ->
                                Text(text = category, color = Color.Black)
                            },
                            parentTextFieldCornerRadius = 4.dp,
                            isError = category.error != null
                        )

                        if (category.error != null) {
                            Text(
                                text = category.error ?: "",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Cooking Complexity",
                            style = MaterialTheme.typography.labelMedium
                        )

                        SearchableExpandedDropDownMenu(
                            listOfItems = viewModel.cookingComplexities,
                            modifier = Modifier.fillMaxWidth(),
                            onDropDownItemSelected = { item ->
                                viewModel.setCookingComplexity(item)
                            },
                            dropdownItem = { complexity ->
                                Text(text = complexity, color = Color.Black)
                            },
                            parentTextFieldCornerRadius = 4.dp,
                            isError = complexity.error != null
                        )

                        if (complexity.error != null) {
                            Text(
                                text = complexity.error ?: "",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.End,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }

            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Cooking Time - ${cookingTime.toInt()} Minutes",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Slider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics {
                                contentDescription = "Cooking Time Slider"
                            },
                        value = cookingTime,
                        onValueChange = {
                            viewModel.setCookingTime(it)
                        },
                        valueRange = 0f..300f,
                        onValueChangeFinished = {
                            viewModel.setCookingTime(cookingTime)
                        },
                        interactionSource = sliderInteractionSource,
                        thumb = {
                            SliderDefaults.Thumb(
                                interactionSource = sliderInteractionSource,
                                colors = sliderColors
                            )
                        },
                        track = { sliderPositions ->
                            SliderDefaults.Track(
                                colors = sliderColors,
                                sliderPositions = sliderPositions
                            )
                        }
                    )
                }
            }

            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(32.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Serving - ${peopleServing.toInt()} People",
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Slider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .semantics {
                                contentDescription = "Localized Description"
                            },
                        value = peopleServing,
                        onValueChange = {
                            viewModel.setPeopleServing(it)
                        },
                        valueRange = 0f..300f,
                        onValueChangeFinished = {
                            viewModel.setPeopleServing(peopleServing)
                        },
                        interactionSource = sliderInteractionSource,
                        thumb = {
                            SliderDefaults.Thumb(
                                interactionSource = sliderInteractionSource,
                                colors = sliderColors
                            )
                        },
                        track = { sliderPositions ->
                            SliderDefaults.Track(
                                colors = sliderColors,
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
                        if (viewModel.mealImageUri.value == null) {
                            Toast.makeText(
                                context,
                                "Please select an image so as to proceed",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        } else if (mealName.text.isEmpty()) {
                            viewModel.setMealNameState(error = "Meal name cannot be empty")
                            return@Button
                        } else if (category.text.isEmpty()) {
                            viewModel.setCategory(error = "Meal category cannot be empty")
                            return@Button
                        } else if (complexity.text.isEmpty()) {
                            viewModel.setCookingComplexity(
                                error = "Cooking complexity cannot be empty"
                            )
                            return@Button
                        }

                        if (cookingTime.toInt() <= 0) {
                            Toast.makeText(
                                context,
                                "Please use the slider to select cooking time",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        if (peopleServing.toInt() <= 0) {
                            Toast.makeText(
                                context,
                                "Please use the slider to select people serving",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@Button
                        }

                        navigator.openNextAddMealScreen(
                            imageUri = viewModel.mealImageUri.value!!,
                            mealName = viewModel.mealName.value.text,
                            cookingTime = viewModel.cookingTime.value.toInt(),
                            servingPeople = viewModel.peopleServing.value.toInt(),
                            complexity = complexity.text,
                            category = category.text
                        )
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

/**
 * Select Image -DONE
 * Enter Food Title - DONE
 * Cooking Time - DONE
 * Serving People - DONE
 * Cooking Complexity - DONE
 * CategoryEntity - DONE
 * Ingredients - DONE
 * Cooking Directions - DONE
 */

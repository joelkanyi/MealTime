package com.joelkanyi.presentation.mealplanner.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.joelkanyi.common.model.Meal
import com.joelkanyi.designsystem.components.LoadingStateComponent
import com.joelkanyi.designsystem.components.LottieAnim

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMealBottomSheet(
    bottomSheetState: SheetState,
    onDismissRequest: () -> Unit,
    mealType: String,
    onClickAdd: (Meal, String) -> Unit,
    meals: List<Meal>,
    onSearchClicked: () -> Unit,
    onSearchValueChange: (String) -> Unit,
    currentSearchString: String,
    onSearchByChange: (String) -> Unit,
    onSourceChange: (String) -> Unit,
    onMealClick: (String?, String?, Boolean) -> Unit,
    sources: List<String>,
    searchOptions: List<String>,
    currentSource: String,
    isLoading: Boolean = false,
    error: String? = null
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = bottomSheetState,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = mealType,
                    style = MaterialTheme.typography.titleLarge
                )

                Icon(
                    modifier = Modifier
                        .padding(8.dp)
                        .testTag("close_dialog")
                        .clickable(MutableInteractionSource(), null) {
                            onDismissRequest()
                        },
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close dialog",
                    tint = Color.Red
                )
            }
            Box {
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

                            com.joelkanyi.designsystem.components.BloomDropDown(
                                modifier = Modifier.fillMaxWidth(),
                                options = sources,
                                onOptionSelected = { item ->
                                    onSourceChange(item)
                                },
                                selectedOption = currentSource,
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

                                com.joelkanyi.designsystem.components.BloomDropDown(
                                    options = searchOptions,
                                    modifier = Modifier.fillMaxWidth(),
                                    onOptionSelected = { item ->
                                        onSearchByChange(item)
                                    },
                                    selectedOption = "Name",
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
                                    .align(Alignment.Center)
                                    .padding(16.dp)
                                    .testTag("Empty State Component"),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LottieAnim(
                                    resId = com.joelkanyi.designsystem.R.raw.search_empty,
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
                            cardWidth = 150.dp,
                            imageHeight = 100.dp,
                            isAddingToPlan = true,
                            onClickAdd = onClickAdd,
                            type = mealType,
                            onRemoveClick = { _ ->
                            },
                            onMealClick = onMealClick
                        )
                    }
                }

                if (isLoading) {
                    LoadingStateComponent()
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}
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
package com.kanyideveloper.presentation.details

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.kanyideveloper.core.model.Meal
import com.kanyideveloper.presentation.details.common.DetailsCollapsingToolbar
import com.kanyideveloper.presentation.home.HomeNavigator
import com.ramcosta.composedestinations.annotation.Destination

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination
@Composable
fun DetailsScreen(
    meal: Meal,
    navigator: HomeNavigator,
    viewModel: DetailsViewModel = hiltViewModel()
) {
    DetailsScreenContent(
        meal = meal,
        navigateBack = {
            navigator.popBackStack()
        },
        onRemoveFavorite = { localMealId, _ ->
            viewModel.deleteALocalFavorite(localMealId = localMealId)
            navigator.popBackStack()
        }
    )
}

@Composable
private fun DetailsScreenContent(
    meal: Meal,
    navigateBack: () -> Unit,
    onRemoveFavorite: (Int, String) -> Unit
) {
    DetailsCollapsingToolbar(
        meal = meal,
        navigateBack = {
            navigateBack()
        },
        onRemoveFavorite = onRemoveFavorite
    )
}

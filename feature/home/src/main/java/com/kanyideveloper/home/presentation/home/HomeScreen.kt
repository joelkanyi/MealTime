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

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import com.kanyideveloper.mealtime.R
import com.kanyideveloper.core.presentation.components.StandardToolbar
import com.kanyideveloper.core.presentation.components.TabItem
import com.kanyideveloper.core.presentation.components.Tabs
import com.kanyideveloper.mealtime.screens.destinations.AddMealScreenDestination
import com.kanyideveloper.mealtime.screens.destinations.DetailsScreenDestination
import com.kanyideveloper.mealtime.ui.theme.MainOrange
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Destination(start = true)
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeViewModel = hiltViewModel()
) {
    Scaffold(
        topBar = {
            StandardToolbar(
                navigator = navigator,
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.ic_meal_time_banner),
                        contentDescription = null
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                showBackArrow = false,
                navActions = {}
            )
        },
        floatingActionButton = {
            if (viewModel.isMyMeal.value) {
                FloatingActionButton(
                    modifier = Modifier
                        .height(50.dp),
                    shape = RoundedCornerShape(24.dp),
                    backgroundColor = MainOrange,
                    content = {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.fork_knife_thin),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(text = "Add Meal")
                        }
                    },
                    onClick = {
                        navigator.navigate(AddMealScreenDestination)
                    }
                )
            }
        }
    ) { paddingValues ->
        MainContent(
            navigator = navigator,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues)
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun MainContent(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier
) {
    val listOfTabs = listOf(
        TabItem.Outgoing(navigator = navigator),
        TabItem.Incoming(navigator = navigator)
    )
    val pagerState = rememberPagerState()

    Column(modifier = modifier) {
        Tabs(
            tabs = listOfTabs,
            pagerState = pagerState
        )
        TabContent(
            tabs = listOfTabs,
            pagerState = pagerState,
            onClick = { navigator.navigate(DetailsScreenDestination) },
            modifier = modifier
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabContent(
    tabs: List<TabItem>,
    pagerState: PagerState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    HorizontalPager(
        count = tabs.size,
        state = pagerState,
        modifier = modifier
    ) { page ->
        tabs[page].screen(
            onClick = {
                onClick()
            }
        )
    }
}

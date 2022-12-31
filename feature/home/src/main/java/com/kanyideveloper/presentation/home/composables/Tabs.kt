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
package com.kanyideveloper.presentation.home.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Tab
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalPagerApi::class)
@Composable
fun Tabs(
    tabs: List<TabItem>,
    pagerState: PagerState,
    onClick: (Int) -> Unit
) {
    val scope = rememberCoroutineScope()

    TabRow(

        selectedTabIndex = pagerState.currentPage,
        backgroundColor = MaterialTheme.colorScheme.background,
        modifier = Modifier
            .width(300.dp)
            .padding(vertical = 4.dp, horizontal = 16.dp)
            .clip(RoundedCornerShape(50)),
        indicator = {
            Box {}
        },
        divider = {}
    ) {
        tabs.forEachIndexed { index, tabItem ->
            val selected = pagerState.currentPage == index
            Tab(
                modifier = if (selected) Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        MaterialTheme.colorScheme.primary
                    )
                else Modifier
                    .clip(RoundedCornerShape(50))
                    .background(
                        Color.Transparent
                    ),
                selected = pagerState.currentPage == index,
                onClick = {
                    scope.launch {
                        onClick(index)
                        pagerState.animateScrollToPage(index)
                    }
                },
                text = {
                    Text(
                        text = tabItem.title,
                        color = if (pagerState.currentPage == index) {
                            MaterialTheme.colorScheme.onPrimary
                        } else {
                            MaterialTheme.colorScheme.onBackground
                                .copy(alpha = .5f)
                        },
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            )
        }
    }
}

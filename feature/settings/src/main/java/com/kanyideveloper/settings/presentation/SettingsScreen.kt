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
package com.kanyideveloper.settings.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.ramcosta.composedestinations.annotation.Destination

interface SettingsNavigator {
    fun openSettings(showId: Long)
}

@Destination
@Composable
fun SettingsScreen(
    navigator: SettingsNavigator
) {
    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigate = {
            },
            title = {
                Text(text = "Settings", fontSize = 16.sp)
            },
            showBackArrow = false,
            navActions = {
            }
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = "Settings",
                fontSize = 16.sp
            )
        }
    }
}

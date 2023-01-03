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
package com.kanyideveloper.randommeal

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.kanyideveloper.compose_ui.components.StandardToolbar
import com.kanyideveloper.core.util.showDayCookMessage
import com.ramcosta.composedestinations.annotation.Destination

interface RandomMealNavigator {
    fun popBackStack()
}

@Destination
@Composable
fun RandomMealScreen(
    navigator: RandomMealNavigator
) {
    Column(Modifier.fillMaxSize()) {
        StandardToolbar(
            navigate = {
                navigator.popBackStack()
            },
            title = {
                Text(text = showDayCookMessage(), fontSize = 16.sp)
            },
            showBackArrow = true,
            navActions = {
            }
        )
    }
}

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

import androidx.compose.runtime.Composable
import com.kanyideveloper.presentation.home.HomeNavigator
import com.kanyideveloper.presentation.home.mymeal.MyMealScreen
import com.kanyideveloper.presentation.home.onlinemeal.OnlineMealScreen

typealias ComposableFun = @Composable (Boolean) -> Unit

sealed class TabItem(val title: String, val screen: ComposableFun) {
    data class Outgoing(val navigator: HomeNavigator) :
        TabItem("My Meals", screen = { isSubscribed ->
            MyMealScreen(
                isSubscribed = isSubscribed,
                navigator = navigator
            )
        })

    data class Incoming(val navigator: HomeNavigator) :
        TabItem("Online Meals", screen = { isSubscribed ->
            OnlineMealScreen(
                isSubscribed = isSubscribed,
                navigator = navigator
            )
        })
}

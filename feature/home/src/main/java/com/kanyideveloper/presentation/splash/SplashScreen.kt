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
package com.kanyideveloper.presentation.splash

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.kanyideveloper.presentation.home.HomeNavigator
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Destination
@Composable
fun SplashScreen(
    navigator: HomeNavigator
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val scale = remember {
            Animatable(0f)
        }

        val overshootInterpolator = remember {
            OvershootInterpolator(2.5f)
        }

        LaunchedEffect(key1 = true) {
            withContext(Dispatchers.Main) {
                scale.animateTo(
                    targetValue = 1.5f,
                    animationSpec = tween(
                        durationMillis = 700,
                        easing = {
                            overshootInterpolator.getInterpolation(it)
                        }
                    )
                )

                delay(2000L)
                navigator.popBackStack()
                navigator.openHome()
            }
        }

        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
        ) {
            Image(
                painterResource(
                    id = com.kanyideveloper.mealtime.core.R.drawable.app_logo
                ),
                contentDescription = "App-logo",
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

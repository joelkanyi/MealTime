package com.kanyideveloper.core.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.kanyideveloper.core.util.LottieAnim
import com.kanyideveloper.mealtime.core.R

@Composable
fun BoxScope.LoadingStateComponent() {
    LottieAnim(
        resId = R.raw.loading_anim,
        modifier = Modifier.align(
            Alignment.Center
        )
    )
}
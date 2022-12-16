package com.kanyideveloper.core.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kanyideveloper.core.util.LottieAnim
import com.kanyideveloper.mealtime.core.R

@Composable
fun BoxScope.EmptyStateComponent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .align(
                Alignment.Center
            )
            .padding(16.dp)
    ) {
        LottieAnim(resId = R.raw.empty_state)
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Nothing found here!",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}
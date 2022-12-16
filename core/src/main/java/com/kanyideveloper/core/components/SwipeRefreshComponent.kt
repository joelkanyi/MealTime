package com.kanyideveloper.core.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun SwipeRefreshComponent(
    isRefreshingState: Boolean,
    onRefreshData: () -> Unit,
    content: @Composable () -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing = isRefreshingState),
        onRefresh = {
            onRefreshData()
        },
        indicator = { state, refreshTrigger ->
            SwipeRefreshIndicator(
                state = state,
                refreshTriggerDistance = refreshTrigger,
                scale = true,
                arrowEnabled = true,
                refreshingOffset = 120.dp
            )
        }
    ) {
        content()
    }
}

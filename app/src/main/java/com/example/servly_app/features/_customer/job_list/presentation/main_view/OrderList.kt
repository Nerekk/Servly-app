package com.example.servly_app.features._customer.job_list.presentation.main_view

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.ui.theme.AppTheme
import kotlinx.coroutines.flow.distinctUntilChanged

@Preview(
    showBackground = true,
    locale = "en",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLightEN"
)
@Preview(
    showBackground = true,
    locale = "pl",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDarkPL"
)
@Composable
fun PreviewRequestList() {
    AppTheme {
        OrderList(getInProgressRequests(), false, {}, false)
    }
}

@Composable
fun OrderList(
    orders: List<Order>,
    isLoading: Boolean,
    loadOrders: () -> Unit,
    isActive: Boolean
) {
    val listState = rememberLazyListState()

    LaunchedEffect(listState, orders, isActive) {
        snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
            .collect { lastIndex ->
                if (isActive && lastIndex != null && lastIndex == orders.lastIndex) {
                    loadOrders()
                }
            }
    }


    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize()
    ) {
        if (orders.isEmpty() && !isLoading) {
            item {
                NoOrders()
            }
            return@LazyColumn
        }

        if (orders.isNotEmpty()) {
            items(orders) { request ->
                OrderCard(request)
            }

            if (isLoading) {
                item {
                    LoadingScreen()
                }
            }
        } else {
            item {
                LoadingScreen()
            }
        }
    }
}

@Composable
fun NoOrders() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No orders found",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}
package com.example.servly_app.features._customer.job_list.presentation.main_view.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.ui.theme.AppTheme

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
        OrderList(
            getInProgressRequests(), false,
            loadOrders = {},
            onClickCard = {},
            isActive = false
        )
    }
}

@Composable
fun OrderList(
    orders: List<Order>,
    isLoading: Boolean,
    loadOrders: () -> Unit,
    onClickCard: (Order) -> Unit,
    isActive: Boolean,
    header: @Composable (() -> Unit)? = null,
    myListState: LazyListState? = null,
    topbarPadding: PaddingValues? = null
) {
    val listState = myListState ?: rememberLazyListState()

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
        modifier = if (topbarPadding != null) Modifier.fillMaxSize().padding(topbarPadding) else Modifier.fillMaxSize()
    ) {
        item {
            if (header != null) {
                header()
            }
        }

        if (orders.isEmpty() && !isLoading) {
            item {
                NoOrders()
            }
            return@LazyColumn
        }

        items(orders) { order ->
            OrderCard(
                order,
                onClick = { onClickCard(order) },
            )
        }

        if (isLoading) {
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
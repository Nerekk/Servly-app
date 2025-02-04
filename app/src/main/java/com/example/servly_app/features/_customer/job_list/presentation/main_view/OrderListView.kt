package com.example.servly_app.features._customer.job_list.presentation.main_view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.servly_app.R
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features._customer.job_list.presentation.main_view.components.Order
import com.example.servly_app.features._customer.job_list.presentation.main_view.components.OrderList

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
fun PreviewRequestView() {
    AppTheme {
        OrderListView()
    }
}

@Composable
fun OrderListView() {
    val viewModel: OrderListViewModel = hiltViewModel()
    val state = viewModel.orderListState.collectAsState()

    OrderListContent(
        state,
        setTabIndex = viewModel::setTabIndex,
        loadOrders = viewModel::loadOrders
    )
}

@Composable
private fun OrderListContent(
    state: State<OrderListState>,
    setTabIndex: (Int) -> Unit,
    loadOrders: () -> Unit
) {
    val categories = listOf(
        stringResource(R.string.tab_active),
        stringResource(R.string.tab_ended)
    )

    val pagerState = rememberPagerState { categories.size }

    LaunchedEffect(state.value.selectedTabIndex) {
        pagerState.animateScrollToPage(state.value.selectedTabIndex)
    }
    LaunchedEffect(pagerState.currentPage, pagerState.isScrollInProgress) {
        if (!pagerState.isScrollInProgress) {
            setTabIndex(pagerState.currentPage)
        }
    }

    BasicScreenLayout {
        Column(modifier = Modifier.fillMaxSize()) {
            TabRow(
                selectedTabIndex = state.value.selectedTabIndex,
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        color = MaterialTheme.colorScheme.primary
                    )
                    .padding(4.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                indicator = {},
                divider = {}
            ) {
                categories.forEachIndexed { index, title ->
                    val selected = state.value.selectedTabIndex == index
                    Tab(
                        selected = selected,
                        onClick = { setTabIndex(index) },
                        modifier = if (selected) Modifier
                            .background(
                                color = MaterialTheme.colorScheme.onPrimary,
                                shape = RoundedCornerShape(50)
                            )
                        else Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = RoundedCornerShape(50)
                            ),

                        text = {
                            Text(
                                text = title,
                                color = if (selected) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    )
                }
            }

            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                when (index) {
                    0 -> OrderList(
                        orders = state.value.activeOrders.map {
                            Order(
                                title = it.title,
                                location = "${it.city}, ${it.street}",
                                category = state.value.getCategoryName(it.categoryId),
                                status = it.status
                            )
                        },
                        isLoading = state.value.isLoading,
                        loadOrders,
                        isActive = state.value.selectedTabIndex == index
                    )
                    1 -> OrderList(
                        orders = state.value.endedOrders.map {
                            Order(
                                title = it.title,
                                location = "${it.city}, ${it.street}",
                                category = state.value.getCategoryName(it.categoryId),
                                status = it.status
                            )
                        },
                        isLoading = state.value.isLoading,
                        loadOrders,
                        isActive = state.value.selectedTabIndex == index
                    )
                }
            }
        }
    }
}


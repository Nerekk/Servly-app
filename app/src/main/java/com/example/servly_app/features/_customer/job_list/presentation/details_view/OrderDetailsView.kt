package com.example.servly_app.features._customer.job_list.presentation.details_view

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo


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
fun PreviewRequestDetailsView() {
    AppTheme {
        OrderDetailsContent(null)
    }
}

@Composable
fun OrderDetailsView(
    order: JobPostingInfo
) {
    val viewModel: OrderDetailsViewModel = hiltViewModel<OrderDetailsViewModel, OrderDetailsViewModel.OrderDetailsViewModelFactory> { factory ->
        factory.create(order)
    }
    val state = viewModel.orderDetailsState.collectAsState()

    OrderDetailsContent(state)
}

@Composable
private fun OrderDetailsContent(state: State<OrderDetailsState>?) {
    val avatars = listOf(
        Pair(painterResource(R.drawable.test_square_image_large), "Jan"),
        Pair(painterResource(R.drawable.test_square_image_large), "Anna"),
        Pair(painterResource(R.drawable.test_square_image_large), "Piotr"),
        Pair(painterResource(R.drawable.test_square_image_large), "Jakub"),
        Pair(painterResource(R.drawable.test_square_image_large), "Jacek"),
        Pair(painterResource(R.drawable.test_square_image_large), "Zofia")
    )

    BasicScreenLayout {
        if (state!!.value.isLoading) {
            LoadingScreen()
            return@BasicScreenLayout
        }

        Column {
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    InterestedProviders(avatars)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
                    DetailsCard(state)
                }
            }
        }
    }
}
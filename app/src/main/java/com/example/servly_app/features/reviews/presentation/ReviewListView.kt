package com.example.servly_app.features.reviews.presentation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.servly_app.R
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.data.util.Role
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
fun PreviewReview() {
    AppTheme {
        val state = remember { mutableStateOf(ReviewListState()) }
        ReviewListContent(
            state, {}
        )
    }
}

@Composable
fun ReviewListView(userId: Long, role: Role) {
    val viewModel: ReviewListViewModel = hiltViewModel<ReviewListViewModel, ReviewListViewModel.ReviewListViewModelFactory> { factory ->
        factory.create(userId, role)
    }
    val state = viewModel.reviewListState.collectAsState()

    ReviewListContent(
        state,
        loadPage = viewModel::loadPage
    )
}

@Composable
private fun ReviewListContent(
    state: State<ReviewListState>,
    loadPage: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        PageButtonsBar(
            page = state.value.page,
            totalPages = state.value.totalPages,
            loadPage = loadPage
        )

        if (state.value.isLoading) {
            LoadingScreen()
            return@Column
        }

        if (state.value.reviews.isNotEmpty()) {
            LazyColumn {
                items(state.value.reviews) { review ->
                    ReviewCard(review)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        } else {
            PageNoData()
        }
    }
}

@Composable
private fun PageNoData() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.description_24px),
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(40.dp)
        )
        Text("No data", color = Color.Gray)
    }
}

@Composable
private fun PageButtonsBar(
    page: Int,
    totalPages: Int,
    loadPage: (Int) -> Unit,
) {
    val backgroundColor = MaterialTheme.colorScheme.surface
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
    ) {
        Row {
            IconButton(
                onClick = { loadPage(0) },
                enabled = page > 0
            ) {
                Icon(
                    painter = painterResource(R.drawable.first_page_24px),
                    contentDescription = null,
                    tint = contentColorFor(backgroundColor)
                )
            }

            IconButton(
                onClick = { loadPage(page-1) },
                enabled = page > 0
            ) {
                Icon(
                    painter = painterResource(R.drawable.chevron_left_24px),
                    contentDescription = null,
                    tint = contentColorFor(backgroundColor)
                )
            }
        }

        Text(
            text = "${page+1} of $totalPages",
            color = contentColorFor(backgroundColor)
        )

        Row {
            IconButton(
                onClick = { loadPage(page+1) },
                enabled = page < totalPages-1
            ) {
                Icon(
                    painter = painterResource(R.drawable.chevron_right_24px),
                    contentDescription = null,
                    tint = contentColorFor(backgroundColor)
                )
            }
            IconButton(
                onClick = { loadPage(totalPages-1) },
                enabled = page < totalPages-1
            ) {
                Icon(
                    painter = painterResource(R.drawable.last_page_24px),
                    contentDescription = null,
                    tint = contentColorFor(backgroundColor)
                )
            }
        }
    }
}
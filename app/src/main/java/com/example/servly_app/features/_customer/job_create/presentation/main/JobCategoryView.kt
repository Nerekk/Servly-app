package com.example.servly_app.features._customer.job_create.presentation.main

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.features._customer.job_create.presentation.main.components.JobCreateInfoCard
import com.example.servly_app.features._customer.job_create.presentation.main.components.JobCategoryList
import com.example.servly_app.features._customer.job_create.presentation.main.components.JobCategory

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
fun PreviewOffersView() {
    val state = remember { mutableStateOf(JobCategoryState()) }
    AppTheme {
        JobCategoryContent(state, onCategorySelect = {}, {})
    }
}

@Composable
fun JobCategoryView(
    onCategorySelect: (JobCategory) -> Unit,
    onc: () -> Unit
) {
    val viewModel: OffersViewModel = hiltViewModel()
    val state = viewModel.jobCategoryState.collectAsState()

    JobCategoryContent(
        state,
        onCategorySelect = { onCategorySelect(it) },
        onc = onc
    )
}

@Composable
private fun JobCategoryContent(
    state: State<JobCategoryState>,
    onCategorySelect: (JobCategory) -> Unit,
    onc: () -> Unit
) {
    BasicScreenLayout {
        Column {
//            Button(onClick = onc) {
//                Text("Stripe")
//            }
            JobCreateInfoCard()

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            if (state.value.isLoading) {
                LoadingScreen()
            } else {
                JobCategoryList(
                    state.value.categories.map { it.toJobCategory() },
                    onCategorySelect = { onCategorySelect(it) }
                )
            }
        }
    }
}
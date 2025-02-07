package com.example.servly_app.features._customer.job_list.presentation.details_view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features._customer.job_list.presentation.details_view.components.HeaderSection
import com.example.servly_app.features._customer.job_list.presentation.details_view.components.InfoSection


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
fun PreviewDetailsCard() {
    AppTheme {
        DetailsCard(null)
    }
}

@Composable
fun DetailsCard(state: State<OrderDetailsState>?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(24.dp)
            ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            HeaderSection(
                title = state!!.value.order.title,
                location = "${state.value.order.city}, ${state.value.order.street}",
                status = state.value.order.status
            )

            state.value.questions.forEachIndexed { index, question ->
                val matchingAnswer = state.value.order.answers.find { it.id == question.id }

                InfoSection(
                    title = "$index. ${question.text}",
                    body = matchingAnswer?.answer ?: "Answer unknown"
                )
            }
        }
    }
}

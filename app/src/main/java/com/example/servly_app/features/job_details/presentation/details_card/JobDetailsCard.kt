package com.example.servly_app.features.job_details.presentation.details_card

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features._customer.job_create.data.dtos.QuestionAnswer
import com.example.servly_app.features._customer.job_create.data.dtos.QuestionInfo
import com.example.servly_app.features.job_details.presentation.details_card.components.JobDetailsHeaderSection
import com.example.servly_app.features.job_details.presentation.details_card.components.JobDetailsInfoSection


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
        JobDetailsCard(JobDetails())
    }
}

data class JobDetails(
    val title: String = "",
    val address: String = "",
    val status: JobStatus = JobStatus.ACTIVE,
    val questions: List<QuestionInfo> = emptyList(),
    val answers: List<QuestionAnswer> = emptyList()
)

@Composable
fun JobDetailsCard(details: JobDetails) {
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
            JobDetailsHeaderSection(
                title = details.title,
                location = details.address
            )

            details.questions.forEachIndexed { index, question ->
                val matchingAnswer = details.answers.find { it.id == question.id }

                JobDetailsInfoSection(
                    title = "${index+1}. ${question.text}",
                    body = matchingAnswer?.answer ?: "Answer unknown"
                )
            }
        }
    }
}

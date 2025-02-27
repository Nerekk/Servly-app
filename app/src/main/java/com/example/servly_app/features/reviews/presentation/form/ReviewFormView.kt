package com.example.servly_app.features.reviews.presentation.form

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.servly_app.R
import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.reviews.data.ReviewFormData
import com.example.servly_app.features.reviews.data.ReviewInfo

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
fun Preview() {
    val state = remember { mutableStateOf(ReviewFormData()) }
    AppTheme {
        ReviewFormContent(JobRequestStatus.COMPLETED, state, {}, {}, {})
    }
}

@Composable
fun ReviewFormView(
    role: Role,
    jobRequestId: Long,
    jobRequestStatus: JobRequestStatus,
    onSuccess: (ReviewInfo) -> Unit
) {
    val viewModel: ReviewFormViewModel = hiltViewModel()
    val state = viewModel.formState.collectAsState()

    ReviewFormContent(
        jobRequestStatus,
        state,
        createReview = {
            if (role == Role.CUSTOMER) {
                viewModel.createReviewForCustomer(jobRequestId, onSuccess)
            } else {
                viewModel.createReviewForProvider(jobRequestId, onSuccess)
            }
        },
        updateRating = viewModel::updateRating,
        updateReview = viewModel::updateReview
    )

}

@Composable
private fun ReviewFormContent(
    jobRequestStatus: JobRequestStatus,
    state: State<ReviewFormData>,
    createReview: () -> Unit,
    updateRating: (Int) -> Unit,
    updateReview: (String) -> Unit,
    MAX_REVIEW_SIZE: Int = 200
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        if (jobRequestStatus == JobRequestStatus.COMPLETED) {
            Text(
                text = stringResource(R.string.review_rating),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 12.dp)
                    .align(Alignment.CenterHorizontally),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                for (i in 1..5) {
                    Icon(
                        painter = if (i <= state.value.rating) {
                            painterResource(R.drawable.star_filled_24px)
                        } else {
                            painterResource(R.drawable.star_24px)
                        },
                        contentDescription = "Star",
                        tint = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.size(40.dp).clickable { updateRating(i) }
                    )
                }
            }
        }

        Text(
            text = stringResource(R.string.review_opinion),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = state.value.review,
            onValueChange = {
                if (it.length <= MAX_REVIEW_SIZE) {
                    updateReview(it)
                }
            },
            placeholder = { Text(stringResource(R.string.review_opinion_field)) }
        )
        Text(
            text = "${state.value.review.count()}/$MAX_REVIEW_SIZE",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.End)
        )

        Button(
            modifier = Modifier
                .align(Alignment.End)
                .padding(top = 12.dp),
            enabled = state.value.isButtonEnabled && !state.value.isLoading,
            onClick = createReview
        ) {
            if (state.value.isLoading) {
                CircularProgressIndicator()
            } else {
                Text(stringResource(R.string.review_send))
            }
        }
    }
}
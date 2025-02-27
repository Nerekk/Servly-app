package com.example.servly_app.features.reviews.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.core.util.EnumUtils
import com.example.servly_app.core.util.LanguageUtils
import com.example.servly_app.core.util.formatRating
import com.example.servly_app.features.reviews.data.ReviewInfo
import java.time.LocalDateTime

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
fun PreviewReviewCard() {
    AppTheme {
        ReviewCard(ReviewInfo(
            jobRequestId = 0,
            rating = 3,
            review = "Test",
            jobRequestStatus = JobRequestStatus.COMPLETED,
            createdAt = LocalDateTime.now()))
    }
}

@Composable
fun ReviewCard(reviewInfo: ReviewInfo) {
    Card {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    Text(
                        reviewInfo.reviewerName ?: stringResource(R.string.unknown),
                        style = MaterialTheme.typography.titleMedium
                    )
                    reviewInfo.reviewerRating?.let {
                        Text(
                            text = formatRating(it),
                            modifier = Modifier.padding(start = 4.dp)
                        )
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Star"
                        )
                    }
                }

                Text(
                    text = LanguageUtils.formatReviewDateTime(reviewInfo.createdAt),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    stringResource(R.string.review_job),
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    stringResource(EnumUtils.getReviewStatusString(reviewInfo.jobRequestStatus)),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (reviewInfo.jobRequestStatus == JobRequestStatus.COMPLETED) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        stringResource(R.string.review_rating),
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Row {
                        for (i in 1..5) {
                            Icon(
                                painter = if (i <= reviewInfo.rating) {
                                    painterResource(R.drawable.star_filled_24px)
                                } else {
                                    painterResource(R.drawable.star_24px)
                                },
                                contentDescription = "Star",
                                tint = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            if (reviewInfo.review.isNotBlank()) {
                Text(
                    text = reviewInfo.review,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

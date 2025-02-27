package com.example.servly_app.features.job_details.presentation.header

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.job_details.data.JobRequestInfo
import com.example.servly_app.features.job_details.presentation.JobRequestDetailsState
import com.example.servly_app.features.role_selection.data.ProviderInfo

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
fun PreviewInterestedProviders() {
    AppTheme {
        val customerState = remember { mutableStateOf(JobRequestDetailsState()) }
        InterestedProviders(customerState, {})
    }
}

@Composable
fun InterestedProviders(
    headerState: State<JobRequestDetailsState>,
    selectProvider: (JobRequestInfo?) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(4.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.providers),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            if (headerState.value.jobRequests.isNotEmpty()) ProvidersList(headerState, selectProvider) else NoProviders()
        }
    }
}

@Composable
private fun ProvidersList(
    headerState: State<JobRequestDetailsState>,
    selectProvider: (JobRequestInfo?) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(headerState.value.jobRequests) { jobRequestInfo ->
            Column(
                modifier = Modifier
                    .clickable { selectProvider(jobRequestInfo) }
                    .width(80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = jobRequestInfo.jobRequestStatus.toString(),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.secondary
                )

                Image(
                    painter = painterResource(R.drawable.test_square_image_large),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = jobRequestInfo.provider!!.name,
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
fun NoProviders() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.no_providers),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
    }
}

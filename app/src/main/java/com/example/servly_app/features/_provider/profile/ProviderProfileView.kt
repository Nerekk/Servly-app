package com.example.servly_app.features._provider.profile

import android.content.res.Configuration
import android.telephony.PhoneNumberUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AssistChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features._provider.ProviderState
import com.example.servly_app.features._provider.profile.components.ProviderProfileCard

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
fun PreviewProviderProfileView() {
    val state = remember { mutableStateOf(ProviderState(name = "John Doe")) }
    AppTheme {
        ProviderProfileView(state, {})
    }
}


@Composable
fun ProviderProfileView(
    providerState: State<ProviderState>,
    onContactEdit: () -> Unit
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding()
            .padding(16.dp)
    ) {
        LazyColumn {
            item {
                ProviderProfileCard(
                    title = stringResource(R.string.profile_provider),
                    providerAvatar = painterResource(R.drawable.test_square_image_large),
                    providerName = providerState.value.name,
                    providerRating = 4.5
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                SectionContactDetails(
                    phoneNumber = providerState.value.phoneNumber,
                    city = providerState.value.city,
                    onContactEdit = onContactEdit
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                SectionAboutMe()

                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                SectionServicesPriceList()
            }
        }
    }
}

@Composable
private fun SectionContactDetails(
    phoneNumber: String,
    city: String,
    onContactEdit: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.profile_contact),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        AssistChip(
            onClick = { onContactEdit() },
            label = {
                Text(
                    text = "Edit",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Icon"
                )
            }
        )
    }

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = stringResource(R.string.profile_workingarea) + ": $city",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = stringResource(R.string.profile_phone) + ": ${PhoneNumberUtils.formatNumber(phoneNumber, "pl")}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
private fun SectionAboutMe() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.profile_aboutme),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        AssistChip(
            onClick = {},
            label = {
                Text(
                    text = "Edit",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Icon"
                )
            }
        )
    }
    Text(
        text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Proin felis libero, egestas id scelerisque ut, consequat ac turpis. Mauris eget mollis mi. In rhoncus nunc enim, a auctor arcu pellentesque sed. Proin vehicula diam leo, et suscipit justo lacinia eget.",
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = Modifier.padding(8.dp)
    )
}

@Composable
private fun SectionServicesPriceList() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.profile_services),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        AssistChip(
            onClick = {},
            label = {
                Text(
                    text = "Edit",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Icon"
                )
            }
        )
    }

    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        repeat(5) {
            Text(
                text = "Usługa - 1000 zł",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

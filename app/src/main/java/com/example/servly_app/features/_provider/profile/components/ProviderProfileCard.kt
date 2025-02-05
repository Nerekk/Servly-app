package com.example.servly_app.features._provider.profile.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
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
fun PreviewProviderProfileCard() {
    AppTheme {
        ProviderProfileCard(
            title = "Customer",
            providerAvatar = painterResource(R.drawable.test_square_image_large),
            providerName = "Jan Kowalski",
            providerRating = 4.9
        )
    }
}

@Composable
fun ProviderProfileCard(
    title: String,
    providerAvatar: Painter,
    providerName: String,
    providerRating: Double? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(16.dp, 16.dp, 16.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )


            Image(
                modifier = Modifier
                    .size(150.dp)
                    .padding(16.dp),
                painter = providerAvatar,
                contentDescription = "avatar"
            )

            Text(
                text = providerName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )



            Spacer(modifier = Modifier.height(4.dp))

            providerRating?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$providerRating",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "icon",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.padding(end = 8.dp).size(24.dp)
                    )

                    AssistChip(
                        onClick = {},
                        label = {
                            Text(
                                text = stringResource(R.string.profile_reviews),
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground,
                            )
                        }
                    )
                }
            } ?: run {
                Text(
                    text = "No reviews yet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}
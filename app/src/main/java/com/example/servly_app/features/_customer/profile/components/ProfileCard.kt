package com.example.servly_app.features._customer.profile.components

import android.content.res.Configuration
import android.telephony.PhoneNumberUtils
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
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.core.util.formatRating

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
fun PreviewProfileCard() {
    AppTheme {
        ProfileCard(
            title = "Customer",
            customerAvatar = painterResource(R.drawable.account_circle_24px),
            customerName = "Jan Kowalski",
            customerAddress = "Łódź, Górna",
            customerPhoneNumber = "325532643",
            null, false, {}
        )
    }
}

@Composable
fun ProfileCard(
    title: String,
    customerAvatar: Painter,
    customerName: String,
    customerAddress: String? = null,
    customerPhoneNumber: String? = null,
    rating: Double? = null,
    reviewsVisible: Boolean,
    updateVisibility: (Boolean) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (!reviewsVisible) {
                Image(
                    modifier = Modifier
                        .size(160.dp)
                        .padding(16.dp),
                    painter = customerAvatar,
                    contentDescription = "avatar",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
                )
            }

            Text(
                text = customerName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            if (!reviewsVisible) {
                Spacer(modifier = Modifier.height(8.dp))

                customerAddress?.let {
                    Row {
                        Icon(
                            imageVector = Icons.Filled.Place,
                            contentDescription = "icon"
                        )

                        Text(
                            text = customerAddress,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }

                customerPhoneNumber?.let {
                    Row {
                        Icon(
                            imageVector = Icons.Filled.Phone,
                            contentDescription = "icon"
                        )

                        Text(
                            text = PhoneNumberUtils.formatNumber(customerPhoneNumber, "pl") ?: customerPhoneNumber,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }

            rating?.let {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatRating(it),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "icon",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier.padding(end = 8.dp).size(24.dp)
                    )

                    InputChip(
                        selected = reviewsVisible,
                        onClick = {
                            if (reviewsVisible) {
                                updateVisibility(false)
                            } else {
                                updateVisibility(true)
                            }
                        },
                        label = {
                            Text(
                                text = stringResource(R.string.profile_reviews),
                                style = MaterialTheme.typography.bodyLarge,
                                color = if (!reviewsVisible) {
                                    MaterialTheme.colorScheme.onBackground
                                } else {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                },
                            )
                        }
                    )
                }
            } ?: run {
                Text(
                    text = stringResource(R.string.no_reviews),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }
    }
}
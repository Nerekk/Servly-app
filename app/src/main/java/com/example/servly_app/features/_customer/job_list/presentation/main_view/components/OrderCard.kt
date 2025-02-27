package com.example.servly_app.features._customer.job_list.presentation.main_view.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.job_details.data.JobRequestInfo

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
fun PreviewOfferCard() {
    AppTheme {
        OrderCard(
            Order(
                1, "Zapytanie 2", "Łódź, Polesie", "Elektryk", JobStatus.ACTIVE, "Tomasz"
            ),
            onClick = {},
        )
    }
}

@Composable
fun OrderCard(
    order: Order,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isVerticalLineEnabled: Boolean = true
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
        ) {
            Column(modifier = Modifier.padding(16.dp).weight(1f)) {
                order.jobRequestInfo?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(30))
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            text = order.category,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )

                        Text(
                            modifier = Modifier
                                .background(MaterialTheme.colorScheme.primaryContainer, shape = RoundedCornerShape(30))
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            text = order.jobRequestInfo.jobRequestStatus.toString(),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }

                } ?: run {
                    Text(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.tertiaryContainer, shape = RoundedCornerShape(30))
                            .padding(horizontal = 4.dp, vertical = 2.dp),
                        text = order.category,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }

                Text(
                    modifier = Modifier.padding(top = 4.dp),
                    text = order.title,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )



                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = "location")
                    Text(
                        text = order.location,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                order.person?.let {
                    Row {
                        Icon(imageVector = Icons.Outlined.Person, contentDescription = "person")
                        Text(
                            text = order.person,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            if (isVerticalLineEnabled) {
                Box(
                    modifier = Modifier
                        .width(8.dp)
                        .fillMaxHeight()
                        .background(
                            when (order.status) {
                                JobStatus.COMPLETED -> Color.Green
                                JobStatus.CANCELED -> MaterialTheme.colorScheme.errorContainer
                                JobStatus.ACTIVE -> MaterialTheme.colorScheme.tertiaryContainer
                                JobStatus.IN_PROGRESS -> MaterialTheme.colorScheme.tertiaryContainer
                            }
                        )
                )
            }
        }



    }
}
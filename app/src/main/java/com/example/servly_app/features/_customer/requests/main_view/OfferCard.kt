package com.example.servly_app.features._customer.requests.main_view

import android.content.res.Configuration
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
fun PreviewOfferCard() {
    AppTheme {
        OfferCard(Request("Zapytanie 2", "Łódź, Polesie", "Tomasz", OfferStatus.ACTIVE))
    }
}

@Composable
fun OfferCard(request: Request) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)
        ) {
            Column(modifier = Modifier.padding(16.dp).weight(1f)) {
                Text(text = request.title, style = MaterialTheme.typography.titleMedium)

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Icon(imageVector = Icons.Outlined.LocationOn, contentDescription = "location")
                    Text(text = request.location, style = MaterialTheme.typography.bodyMedium)
                }

                Row {
                    Icon(imageVector = Icons.Outlined.Person, contentDescription = "person")
                    Text(
                        text = request.person,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

            }

            Box(
                modifier = Modifier
                    .width(8.dp)
                    .fillMaxHeight()
                    .background(
                        when (request.status) {
                            OfferStatus.DONE -> Color.Green
                            OfferStatus.CANCELED -> MaterialTheme.colorScheme.errorContainer
                            OfferStatus.ACTIVE -> MaterialTheme.colorScheme.tertiaryContainer
                        }
                    )
            )
        }



    }
}
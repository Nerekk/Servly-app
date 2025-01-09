package com.example.servly_app.features.requests.details_view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
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
fun PreviewDetailsCard() {
    AppTheme {
        DetailsCard()
    }
}

@Composable
fun DetailsCard() {
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
                title = "Budowa domu",
                location = "Łódź, Polesie",
                person = "Jan"
            )

            InfoSection(
                title = "Opis",
                body = "Tutaj będzie opis budowy"
            )

            InfoSection(
                title = "Dodatkowe informacje",
                body = "Tutaj beda dodatkowe informacje o budowie"
            )

            InfoSection(
                title = "Oczekiwany termin wykonania",
                body = "Tutaj beda dodatkowe informacje o oczekiwanym teminie wykonania"
            )
        }
    }
}

@Composable
fun HeaderSection(
    title: String,
    location: String,
    person: String
) {
    Text(
        text = title,
        style = MaterialTheme.typography.headlineMedium,
        color = MaterialTheme.colorScheme.onSurface
    )

    Spacer(modifier = Modifier.height(8.dp))

    Row {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = "location",
            tint = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = location,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }

    Row {
        Icon(
            imageVector = Icons.Outlined.Person,
            contentDescription = "person",
            tint = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = person,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun InfoSection(
    title: String,
    body: String
) {
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Text(
        text = body,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
}
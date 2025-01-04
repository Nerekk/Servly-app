package com.example.servly_app.features.requests.details_view

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.theme.AppTheme
import com.example.servly_app.utils.ScreenContainer


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
fun PreviewRequestDetailsView() {
    AppTheme {
        RequestDetailsView()
    }
}

@Composable
fun RequestDetailsView() {
    val avatars = listOf(
        Pair(painterResource(R.drawable.test_square_image_large), "Jan"),
        Pair(painterResource(R.drawable.test_square_image_large), "Anna"),
        Pair(painterResource(R.drawable.test_square_image_large), "Piotr"),
        Pair(painterResource(R.drawable.test_square_image_large), "Jakub"),
        Pair(painterResource(R.drawable.test_square_image_large), "Jacek"),
        Pair(painterResource(R.drawable.test_square_image_large), "Zofia")
    )

    ScreenContainer {
        InterestedProviders(avatars)

        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

        DetailsCard()
    }
}
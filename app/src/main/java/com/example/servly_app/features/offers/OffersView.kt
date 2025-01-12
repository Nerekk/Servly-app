package com.example.servly_app.features.offers

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.core.components.BasicScreenLayout

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
fun PreviewOffersView() {
    AppTheme {
        OffersView()
    }
}

@Composable
fun OffersView() {
    BasicScreenLayout {
        Column {
            OffersInfoCard()

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

            OffersServiceList()
        }
    }
}
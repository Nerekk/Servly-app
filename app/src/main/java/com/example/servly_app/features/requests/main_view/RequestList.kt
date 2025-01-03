package com.example.servly_app.features.requests.main_view

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.servly_app.core.theme.AppTheme

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
fun PreviewRequestList() {
    AppTheme {
        RequestList(getInProgressRequests())
    }
}

@Composable
fun RequestList(requests: List<Request>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(requests) { request ->
            OfferCard(request)
        }
    }
}
package com.example.servly_app.features.profile

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
fun PreviewProfileView() {
    AppTheme {
        ProfileView()
    }
}

@Composable
fun ProfileView() {
    Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.fillMaxSize()) {
        ProfileCard(
            customerAvatar = painterResource(R.drawable.test_square_image_large),
            customerName = "Jan Kowalski",
            customerAddress = "Łódź, Górna",
            customerPhoneNumber = "425265424"
        )

        ExtendedFloatingActionButton(
            onClick = {},
            icon = {
                Icon(
                    imageVector = Icons.Outlined.Edit,
                    contentDescription = "edit"
                )
                   },
            text = {
                Text("Edytuj profil")
            },
            modifier = Modifier
                .align(Alignment.BottomEnd) // Wyrównanie w prawym dolnym rogu
                .padding(16.dp)
        )
    }
}
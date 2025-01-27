package com.example.servly_app.features._provider.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProviderSettingsView(onSwitchRole: () -> Unit, onLogout: () -> Unit) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Column {
            Button(
                onClick = { onSwitchRole() }
            ) {
                Text(text = "Change role")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { onLogout() }
            ) {
                Text(text = "Logout")
            }
        }
    }
}

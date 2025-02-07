package com.example.servly_app.core.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BasicScreenLayout(
    padding: PaddingValues = PaddingValues(0.dp),
    isListInContent: Boolean = false,
    content: @Composable () -> Unit
) {
    Box(
        modifier = if (!isListInContent) {
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(16.dp)
        } else {
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(horizontal = 16.dp)
        }
    ) {
        content()
    }
}
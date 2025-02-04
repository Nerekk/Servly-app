package com.example.servly_app.features.role_selection.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun FormHeader(
    imageVector: ImageVector? = null,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        imageVector?.let { iv ->
            Icon(
                imageVector = iv,
                contentDescription = "icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}
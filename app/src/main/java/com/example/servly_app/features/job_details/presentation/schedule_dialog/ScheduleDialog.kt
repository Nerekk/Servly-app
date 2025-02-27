package com.example.servly_app.features.job_details.presentation.schedule_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun ScheduleDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit) {
    if (showDialog) {
        Dialog(
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            onDismissRequest = { onDismiss() }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(1.dp)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        IconButton(onClick = { onDismiss() }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "close",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }

                    content()
                }
            }
        }
    }
}
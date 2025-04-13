package com.example.servly_app.core.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.servly_app.R

@Composable
fun ConfirmableOutlinedButton(
    onConfirmed: () -> Unit,
    modifier: Modifier = Modifier,
    @StringRes dialogTitleRes: Int = R.string.confirm_title,
    @StringRes dialogTextRes: Int = R.string.dialog_text,
    @StringRes confirmTextRes: Int = R.string.confirm_text,
    @StringRes dismissTextRes: Int = R.string.dismiss_text,
    content: @Composable RowScope.() -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(stringResource(dialogTitleRes)) },
            text = { Text(stringResource(dialogTextRes)) },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onConfirmed()
                }) {
                    Text(stringResource(confirmTextRes))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text(stringResource(dismissTextRes))
                }
            }
        )
    }

    OutlinedButton(
        onClick = { showDialog = true },
        modifier = modifier,
        content = content
    )
}
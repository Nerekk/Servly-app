package com.example.servly_app.features._customer.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.components.ArrangedColumn
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.util.ErrorStore
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun SettingsView(onSwitchRole: () -> Unit, onLogout: () -> Unit) {
    val context = LocalContext.current

    val user = FirebaseAuth.getInstance().currentUser
    val isPasswordLogin = user?.providerData?.any { it.providerId == "password" } == true

    BasicScreenLayout {
        ArrangedColumn {
            Column {
                val iconSize = 36.dp
                val iconTint = MaterialTheme.colorScheme.secondary

                val textStyle = MaterialTheme.typography.bodyLarge
                val textColor = MaterialTheme.colorScheme.onBackground

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openDarkModeSettings(context) }
                        .padding(8.dp)
                        .padding(),

                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.dark_mode_24px),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(iconSize)
                    )
                    Text(
                        stringResource(R.string.settings_nightmode),
                        style = textStyle,
                        color = textColor,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 1.dp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openAppLanguageSettings(context) }
                        .padding(8.dp)
                        .padding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.language_24px),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(iconSize)
                    )
                    Text(
                        stringResource(R.string.settings_language),
                        style = textStyle,
                        color = textColor,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 1.dp
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { openNotificationSettings(context) }
                        .padding(8.dp)
                        .padding(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.notifications_24px),
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(iconSize)
                    )
                    Text(
                        stringResource(R.string.settings_notifications),
                        style = textStyle,
                        color = textColor,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onBackground,
                    thickness = 1.dp
                )

            }

            Column {
                Button(
                    onClick = { onSwitchRole() },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Text(text = stringResource(R.string.settings_changerole))
                }

                if (isPasswordLogin) {
                    val email = user?.email

                    val toastMessageSuccess = stringResource(R.string.toast_reset_link_success)
                    val toastMessageFailed = stringResource(R.string.toast_reset_link_failed)
                    val toastMessageNotFound = stringResource(R.string.toast_email_not_found)

                    OutlinedButton(
                        onClick = {
                            if (email != null) {
                                FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            Toast.makeText(context, "$toastMessageSuccess $email", Toast.LENGTH_SHORT).show()
                                        } else {
                                            Toast.makeText(context, toastMessageFailed, Toast.LENGTH_SHORT).show()
                                            ErrorStore.addError("${task.exception?.localizedMessage}")
                                        }
                                    }
                            } else {
                                Toast.makeText(context, toastMessageNotFound, Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(R.string.settings_changepassword))
                    }
                }

                OutlinedButton(
                    onClick = { onLogout() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(R.string.settings_logout))
                }
            }
        }
    }
}

fun openNotificationSettings(context: Context) {
    val intent = Intent().apply {
        action = Settings.ACTION_APP_NOTIFICATION_SETTINGS
        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
    }
    context.startActivity(intent)
}

fun openAppLanguageSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APP_LOCALE_SETTINGS).apply {
        data = Uri.parse("package:${context.packageName}")
    }
    context.startActivity(intent)
}

fun openDarkModeSettings(context: Context) {
    val intent = Intent(Settings.ACTION_DISPLAY_SETTINGS)
    context.startActivity(intent)
}
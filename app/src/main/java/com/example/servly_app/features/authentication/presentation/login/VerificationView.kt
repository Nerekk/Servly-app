package com.example.servly_app.features.authentication.presentation.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.features.util.ArrangedColumn
import com.example.servly_app.features.util.ScreenContainer

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
fun PreviewVerificationView() {
    val navController = rememberNavController()
    AppTheme {
        VerificationView(navController)
    }
}

@Composable
fun VerificationView(navController: NavHostController) {
    var code by remember { mutableStateOf("") }

    ScreenContainer {
        ArrangedColumn {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.login_code),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                )
                VerificationCodeInput(
                    code = code,
                    onCodeChange = { newCode ->
                        code = newCode
                    },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }

            Column(
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate(AuthNavItem.RoleSelection.route)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.login_code_verify),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun SingleDigitInput(
    digit: String,
    onDigitChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    onBackspace: () -> Unit
) {
    TextField(
        value = digit,
        onValueChange = { newValue ->
            if (newValue.length <= 1 && newValue.all { it.isDigit() }) {
                onDigitChange(newValue)
            }
        },
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onAny = {
                if (digit.isEmpty()) {
                    onBackspace()
                }
            }
        )
    )
}

@Composable
fun VerificationCodeInput(
    code: String,
    onCodeChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequesters = List(6) { FocusRequester() }

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        for (i in 0 until 6) {
            SingleDigitInput(
                digit = code.getOrNull(i)?.toString() ?: "",
                onDigitChange = { newDigit ->
                    val updatedCode = buildString {
                        append(code.substring(0, i))
                        append(newDigit.take(1))
                        append(code.substring(i + 1).takeIf { it.length > 0 } ?: "")
                    }
                    onCodeChange(updatedCode.take(6))

                    if (newDigit.isNotEmpty() && i < 5) {
                        focusRequesters[i + 1].requestFocus()
                    }
                },
                modifier = Modifier
                    .width(48.dp)
                    .height(56.dp)
                    .focusRequester(focusRequesters[i]),
                onBackspace = {
                    if (i > 0 && code.getOrNull(i) == null) {
                        focusRequesters[i - 1].requestFocus()
                    }
                }
            )
        }
    }
}

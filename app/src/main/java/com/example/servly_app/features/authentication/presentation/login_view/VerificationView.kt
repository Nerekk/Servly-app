package com.example.servly_app.features.authentication.presentation.login_view

import android.content.res.Configuration
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.authentication.presentation.AuthViewModel
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.core.components.ScaffoldAuthNavBar
import com.example.servly_app.core.components.ArrangedColumn
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
fun PreviewVerificationView() {
    val navController = rememberNavController()
    AppTheme {
        VerificationView(navController)
    }
}

@Composable
fun VerificationView(navController: NavHostController, authViewModel: AuthViewModel = hiltViewModel()) {
    val keyboardController = LocalSoftwareKeyboardController.current

    Log.i("VIEWMODEL", "NR TEL ${authViewModel.phoneNumber}")
    ScaffoldAuthNavBar(navController) { initialPadding ->
        BasicScreenLayout(initialPadding) {
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
                    OtpTextField(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        authViewModel = authViewModel,
                        onCodeChange = {
                            authViewModel.updateVerificationCode(it)

                            if (authViewModel.verificationCode.length == 6) {
                                keyboardController?.hide()
                                authViewModel.verifyCode()
                            }
                        }
                    )
                }

                Column(
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Button(
                        onClick = {
//                            navController.navigate(AuthNavItem.RoleSelection.route)
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
}

@Composable
fun OtpTextField(
    modifier: Modifier,
    authViewModel: AuthViewModel,
    onCodeChange: (String) -> Unit
) {

    BasicTextField(
        value = authViewModel.verificationCode,
        onValueChange = {
            onCodeChange(it)
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(6) { index ->
                val number = when {
                    index >= authViewModel.verificationCode.length -> ""
                    else -> authViewModel.verificationCode[index]
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = number.toString(),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(2.dp)
                            .background(color = MaterialTheme.colorScheme.onBackground)
                    )
                }
            }
        }
    }
}


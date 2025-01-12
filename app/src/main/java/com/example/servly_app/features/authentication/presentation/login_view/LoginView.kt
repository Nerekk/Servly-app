package com.example.servly_app.features.authentication.presentation.login_view

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.authentication.presentation.AuthViewModel
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.core.components.HeaderTitle
import com.example.servly_app.core.components.ScaffoldAuthNavBar
import com.example.servly_app.core.components.ArrangedColumn
import com.example.servly_app.core.components.PhoneNumberInput
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
fun PreviewLoginView() {
    val navController = rememberNavController()
    AppTheme {
        LoginView(navController)
    }
}

@Composable
fun LoginView(navController: NavHostController, authViewModel: AuthViewModel = hiltViewModel()) {
    val activity = LocalContext.current as Activity

    ScaffoldAuthNavBar(navController) { initialPadding ->
        BasicScreenLayout(initialPadding) {
            ArrangedColumn {
                Column {
                    HeaderTitle(stringResource(R.string.login_number))

                    PhoneNumberInput(
                        phoneNumber = authViewModel.phoneNumber,
                        onPhoneNumberChange = { authViewModel.updatePhoneNumber(it) },
                        isValid = authViewModel.isValid
                    )
                }

                Column(
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    Button(
                        onClick = {
                            if (authViewModel.isPhoneNumberValid()) {
                                authViewModel.sendVerificationCode(activity)
                                navController.navigate(AuthNavItem.Verification.route)
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.login_number_send),
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}


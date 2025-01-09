package com.example.servly_app.features.authentication.presentation.login

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.features.authentication.presentation.util.HeaderTitle
import com.example.servly_app.features.util.ArrangedColumn
import com.example.servly_app.features.util.PhoneNumberInput
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
fun PreviewLoginView() {
    val navController = rememberNavController()
    AppTheme {
        LoginView(navController)
    }
}

@Composable
fun LoginView(navController: NavHostController) {
    var phoneNumber by remember { mutableStateOf("") }
    var isValid by remember { mutableStateOf(true) }

    ScreenContainer {
        ArrangedColumn {
            Column {
                HeaderTitle(stringResource(R.string.login_number))

                PhoneNumberInput(
                    phoneNumber = phoneNumber,
                    onPhoneNumberChange = { phoneNumber = it },
                    isValid = isValid
                )
            }

            Column(
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Button(
                    onClick = {
                        navController.navigate(AuthNavItem.Verification.route)
//                        isValid = verifyPhoneNumber(phoneNumber)
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

private fun verifyPhoneNumber(phoneNumber: String): Boolean {
    return phoneNumber.startsWith("+") && phoneNumber.length >= 9
}

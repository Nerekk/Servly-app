package com.example.servly_app.features.authentication.presentation.login_view

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.R
import com.example.servly_app.core.components.ArrangedColumn
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.EmailTextField
import com.example.servly_app.features.role_selection.presentation.components.HeaderTitle
import com.example.servly_app.core.components.PasswordTextField
import com.example.servly_app.core.components.ScaffoldAuthNavBar
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.authentication.presentation.AuthState
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem

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
    val authState = remember { mutableStateOf(AuthState()) }
    AppTheme {
        LoginContent(
            navController = navController,
            authState = authState,
            updateEmail = {},
            updatePassword = {},
            onLoginClick = {}
        )
    }
}

@Composable
fun LoginContent(
    navController: NavHostController,
    authState: State<AuthState>,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    ScaffoldAuthNavBar(navController) { initialPadding ->
        BasicScreenLayout(initialPadding) {
            ArrangedColumn {
                Column {
                    HeaderTitle(stringResource(R.string.login_title))

                    EmailTextField(
                        email = authState.value.email,
                        updateEmail = updateEmail,
                        errorMessage = authState.value.emailError
                    )

                    PasswordTextField(
                        password = authState.value.password,
                        updatePassword = updatePassword,
                        label = stringResource(R.string.auth_password),
                        errorMessage = authState.value.passwordError
                    )

                    Text(
                        text = AnnotatedString(stringResource(R.string.forgot_password)),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .align(Alignment.End)
                            .clickable { navController.navigate(AuthNavItem.ForgotPassword.route) }
                    )
                }

                Column(
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    if (authState.value.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Button(
                            onClick = {
                                onLoginClick()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.login_button),
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
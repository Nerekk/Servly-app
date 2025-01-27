package com.example.servly_app.features.authentication.presentation.login_view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.servly_app.R
import com.example.servly_app.core.components.ArrangedColumn
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.EmailTextField
import com.example.servly_app.features.role_selection.presentation.components.HeaderTitle
import com.example.servly_app.core.components.PasswordTextField
import com.example.servly_app.core.components.ScaffoldAuthNavBar
import com.example.servly_app.features.authentication.presentation.AuthState

@Composable
fun RegisterContent(
    navController: NavHostController,
    authState: State<AuthState>,
    updateEmail: (String) -> Unit,
    updatePassword: (String) -> Unit,
    updateConfirmPassword: (String) -> Unit,
    onLoginClick: () -> Unit
) {
    ScaffoldAuthNavBar(navController) { initialPadding ->
        BasicScreenLayout(initialPadding) {
            ArrangedColumn {
                Column {
                    HeaderTitle(stringResource(R.string.register_title))

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

                    PasswordTextField(
                        password = authState.value.confirmPassword,
                        updatePassword = updateConfirmPassword,
                        label = stringResource(R.string.auth_password_repeat),
                        errorMessage = authState.value.confirmPasswordError
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
                                text = stringResource(R.string.register_button),
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
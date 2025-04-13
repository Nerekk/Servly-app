package com.example.servly_app.features.authentication.presentation.forgot_password

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.servly_app.R
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.ScaffoldAuthNavBar
import com.example.servly_app.features.role_selection.presentation.components.HeaderTitle


@Composable
fun ForgotPasswordView(navController: NavController) {
    val context = LocalContext.current

    val viewModel: ForgotPasswordViewModel = hiltViewModel()
    val emailState = viewModel.emailState.collectAsState()

    val toastMessageSuccess = stringResource(R.string.toast_reset_link_success)
    val toastMessageFailed = stringResource(R.string.toast_reset_link_failed)

    ScaffoldAuthNavBar(navController) { initialPadding ->
        BasicScreenLayout(initialPadding) {
            Column {
                HeaderTitle(stringResource(R.string.forgot_password))
                OutlinedTextField(
                    value = emailState.value,
                    onValueChange = viewModel::updateEmail,
                    placeholder = { Text("Email") },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = {
                        viewModel.sendPasswordResetLink(
                            context, onSuccess = { navController.popBackStack() }, toastMessageSuccess, toastMessageFailed
                        )
                    },
                    enabled = emailState.value.isNotBlank(),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) { Text(stringResource(R.string.send_link)) }
            }
        }
    }
}

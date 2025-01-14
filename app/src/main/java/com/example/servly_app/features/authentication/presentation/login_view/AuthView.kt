package com.example.servly_app.features.authentication.presentation.login_view

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.servly_app.features.authentication.presentation.AuthViewModel


sealed class AuthType {
    object Login: AuthType()
    object Register: AuthType()
}

@Composable
fun AuthView(navController: NavHostController, authType: AuthType, onSuccess: () -> Unit) {
    val authViewModel: AuthViewModel = hiltViewModel()
    val authState = authViewModel.authState.collectAsState()

    LaunchedEffect(authState.value.isUserLoggedIn) {
        if (authState.value.isUserLoggedIn) {
            onSuccess()
        }
    }

    when (authType) {
        AuthType.Login -> {
            LoginContent(
                navController = navController,
                authState = authState,
                updateEmail = authViewModel::updateEmail,
                updatePassword = authViewModel::updatePassword,
                onLoginClick = {
                    authViewModel.signInWithEmail(authState.value.email, authState.value.password) // PO SUKCESIE USTAWIA isUserLoggedIn na true
                }
            )
        }
        AuthType.Register -> {
            RegisterContent(
                navController = navController,
                authState = authState,
                updateEmail = authViewModel::updateEmail,
                updatePassword = authViewModel::updatePassword,
                updateConfirmPassword = authViewModel::updateConfirmPassword,
                onLoginClick = {
                    authViewModel.signUpWithEmail(authState.value.email, authState.value.password) // PO SUKCESIE USTAWIA isUserLoggedIn na true
                }
            )
        }
    }

}




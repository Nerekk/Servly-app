package com.example.servly_app.features.authentication.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.servly_app.features.authentication.presentation.forgot_password.ForgotPasswordView
import com.example.servly_app.features.authentication.presentation.login_view.AuthType
import com.example.servly_app.features.authentication.presentation.login_view.AuthView
import com.example.servly_app.features.authentication.presentation.login_view.WelcomeScreen
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem

@Composable
fun AuthNavGraph(
    navController: NavHostController,
    checkUserStatus: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = AuthNavItem.Welcome.route
    ) {
        composable(AuthNavItem.Welcome.route) {
            WelcomeScreen(
                navController,
                onSuccess = {
                    checkUserStatus()
                }
            )
        }
        composable(AuthNavItem.Login.route) {
            AuthView(
                navController,
                AuthType.Login,
                onSuccess = {
                    checkUserStatus()
                }
            )
        }
        composable(AuthNavItem.Register.route) {
            AuthView(
                navController,
                AuthType.Register,
                onSuccess = {
                    checkUserStatus()
                }
            )
        }
        composable(AuthNavItem.ForgotPassword.route) {
            ForgotPasswordView(navController)
        }
    }
}
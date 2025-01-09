package com.example.servly_app.features.authentication.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.features.MainView
import com.example.servly_app.features.authentication.presentation.login.LoginView
import com.example.servly_app.features.authentication.presentation.login.RoleSelectionView
import com.example.servly_app.features.authentication.presentation.login.VerificationView
import com.example.servly_app.features.authentication.presentation.login.WelcomeScreen
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.features.authentication.presentation.user_data.CustomerFormView
import com.example.servly_app.features.authentication.presentation.user_data.ProviderFormView

@Composable
fun MainAuthView() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AuthNavItem.Welcome.route
    ) {
        composable(AuthNavItem.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(AuthNavItem.Login.route) {
            LoginView(navController)
        }
        composable(AuthNavItem.Verification.route) {
            VerificationView(navController)
        }
        composable(AuthNavItem.RoleSelection.route) {
            RoleSelectionView(navController)
        }
        composable(AuthNavItem.CustomerData.route) {
            CustomerFormView(navController)
        }
        composable(AuthNavItem.ProviderData.route) {
            ProviderFormView(navController)
        }


        composable(AuthNavItem.CustomerMain.route) {
            MainView()
        }
        composable(AuthNavItem.ProviderMain.route) {
            MainView()
        }
    }
}

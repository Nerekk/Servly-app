package com.example.servly_app.features.authentication.presentation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.features.MainAppView
import com.example.servly_app.features.authentication.presentation.login_view.LoginView
import com.example.servly_app.features.authentication.presentation.login_view.RoleSelectionView
import com.example.servly_app.features.authentication.presentation.login_view.VerificationView
import com.example.servly_app.features.authentication.presentation.login_view.WelcomeScreen
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.features.authentication.presentation.user_data_view.CustomerFormView
import com.example.servly_app.features.authentication.presentation.user_data_view.ProviderFormView

@Composable
fun MainAuthView() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = AuthNavItem.Welcome.route
    ) {
        composable(AuthNavItem.Welcome.route) {
            WelcomeScreen(navController)
        }
        composable(AuthNavItem.Login.route) {
            LoginView(navController, authViewModel)
        }
        composable(AuthNavItem.Verification.route) {
            VerificationView(navController, authViewModel)
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
            MainAppView()
        }
        composable(AuthNavItem.ProviderMain.route) {
            MainAppView()
        }
    }
}

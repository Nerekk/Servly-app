package com.example.servly_app.features.authentication.presentation.navigation

sealed class AuthNavItem(
    val route: String
) {
    object Welcome: AuthNavItem("auth_welcome")
    object Login: AuthNavItem("auth_login")
    object Register: AuthNavItem("auth_register")
    object RoleSelection: AuthNavItem("auth_role")
    object CustomerData: AuthNavItem("auth_customer")
    object ProviderData: AuthNavItem("auth_provider")
    object CustomerMain: AuthNavItem("auth_customer_main")
    object ProviderMain: AuthNavItem("auth_provider_main")
}
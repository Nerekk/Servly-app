package com.example.servly_app.features.role_selection.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.features.role_selection.presentation.user_data.CustomerFormView
import com.example.servly_app.features.role_selection.presentation.user_data.ProviderFormView

@Composable
fun RoleNavGraph(
    navController: NavHostController,
    checkUserStatus: () -> Unit,
    setDestination: (Role) -> Unit
) {

    NavHost(
        navController = navController,
        startDestination = AuthNavItem.RoleSelection.route,
    ) {
        composable(AuthNavItem.RoleSelection.route) {
            RoleSelectionView(
                navController = navController,
                onCustomerSelect = { role ->
                    when (role) {
                        Role.BOTH, Role.CUSTOMER -> {
                            setDestination(Role.CUSTOMER)
                        }

                        Role.NONE -> {
                            navController.navigate(AuthNavItem.CustomerData.route)
                        }

                        else -> {}
                    }
                },
                onProviderSelect = { role ->
                    when (role) {
                        Role.BOTH, Role.PROVIDER -> {
                            setDestination(Role.PROVIDER)
                        }

                        Role.NONE -> {
                            navController.navigate(AuthNavItem.CustomerData.route)
                        }

                        else -> {}
                    }
                }
            )
        }

        composable(AuthNavItem.CustomerData.route) {
            CustomerFormView(
                navController,
                onSuccess = { checkUserStatus() }
            )
        }

        composable(AuthNavItem.ProviderData.route) {
            ProviderFormView(
                navController,
                onSuccess = { checkUserStatus() }
            )
        }
    }
}
package com.example.servly_app.features._customer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.servly_app.R
import com.example.servly_app.core.MainState
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.ui.navigation.BottomNavigationBar
import com.example.servly_app.core.ui.navigation.CUSTOMER_ITEMS
import com.example.servly_app.core.ui.navigation.NavItem
import com.example.servly_app.features._customer.offers.OffersView
import com.example.servly_app.features._customer.profile.ProfileView
import com.example.servly_app.features._customer.requests.main_view.RequestsView
import com.example.servly_app.features._customer.schedule.ScheduleView
import com.example.servly_app.features._customer.settings.SettingsView
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.features.role_selection.presentation.user_data.ProviderFormView

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomerNavGraph(
    state: State<MainState>,
    navController: NavHostController,
    setAppBarTitle: (String) -> Unit,
    checkUserStatus: () -> Unit,
    logout: () -> Unit
) {
    val isVisible = remember { mutableStateOf(true) }

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            isVisible.value = destination.route != AuthNavItem.ProviderData.route
        }
    }

    Scaffold(
        topBar = {
            if (isVisible.value) {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = state.value.appBarTitle,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (isVisible.value) {
                BottomNavigationBar(navController, CUSTOMER_ITEMS)
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = NavItem.Customer.Offers.route,
            modifier = if (isVisible.value) Modifier.padding(innerPadding) else Modifier
        ) {
            composable(AuthNavItem.ProviderData.route) {
                ProviderFormView(
                    navController,
                    onSuccess = { checkUserStatus() }
                )
            }


            composable(NavItem.Customer.Offers.route) {
                setAppBarTitle(stringResource(R.string.offers))
                OffersView()
            }
            composable(NavItem.Customer.Requests.route) {
                setAppBarTitle(stringResource(R.string.requests))
                RequestsView()
            }
            composable(NavItem.Customer.Profile.route) {
                setAppBarTitle(stringResource(R.string.profile))
                ProfileView()
            }
            composable(NavItem.Customer.Schedule.route) {
                setAppBarTitle(stringResource(R.string.schedule))
                ScheduleView()
            }
            composable(NavItem.Customer.Settings.route) {
                setAppBarTitle(stringResource(R.string.settings))
                SettingsView(
                    onSwitchRole = {
                        if (state.value.userRole == Role.BOTH) {
                            checkUserStatus()
                        } else {
                            // TUTAJ USTAWIAM isVisible na false
                            navController.navigate(AuthNavItem.ProviderData.route)
                        }
                    },
                    onLogout = { logout() }
                )
            }
        }
    }
}
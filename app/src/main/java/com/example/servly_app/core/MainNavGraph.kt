package com.example.servly_app.core

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.ui.navigation.NavItem
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.core.util.ErrorStore
import com.example.servly_app.features._customer.CustomerNavGraph
import com.example.servly_app.features._provider.ProviderNavGraph
import com.example.servly_app.features.authentication.presentation.AuthNavGraph
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.features.role_selection.presentation.RoleNavGraph

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
fun PreviewMainView() {
    AppTheme {
        MainNavGraph()
    }
}

@Composable
fun MainNavGraph(mainViewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val state = mainViewModel.mainState.collectAsState()

    val errors by ErrorStore.errors.collectAsState()

    if (errors.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { ErrorStore.clearErrors() },
            title = { Text("Dev API Error") },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    errors.forEach { error ->
                        Text("• $error")
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { ErrorStore.clearErrors() }) {
                    Text("OK")
                }
            }
        )
    }

    if (state.value.isLoading) {
        LoadingScreen()
    } else {
        when (state.value.startDestination) {
            AuthNavItem.Welcome.route -> AuthNavGraph(
                navController,
                checkUserStatus = { mainViewModel.checkUserStatus() }
            )

            AuthNavItem.RoleSelection.route -> RoleNavGraph(
                navController,
                checkUserStatus = { mainViewModel.checkUserStatus() },
                setDestination = { role ->
                    if (role == Role.CUSTOMER) {
                        mainViewModel.setDestinationCustomer()
                    } else {
                        mainViewModel.setDestinationProvider()
                    }
                }
            )

            NavItem.Customer.Services.route -> CustomerNavGraph(
                state,
                navController,
                setAppBarTitle = { title -> mainViewModel.setAppBarTitle(title) },
                checkUserStatus = { mainViewModel.checkUserStatus() },
                logout = { mainViewModel.logout() }
            )

            NavItem.Provider.Jobs.route -> ProviderNavGraph(
                state,
                navController,
                setAppBarTitle = { title -> mainViewModel.setAppBarTitle(title) },
                checkUserStatus = { mainViewModel.checkUserStatus() },
                logout = { mainViewModel.logout() }
            )
        }
    }

//    MainNavHost(state, navController, mainViewModel)
}

//@Composable
//@OptIn(ExperimentalMaterial3Api::class)
//private fun MainNavHost(
//    state: State<MainState>,
//    navController: NavHostController,
//    mainViewModel: MainViewModel
//) {
//    Scaffold(
//        topBar = {
//            if (state.value.isUserLoggedIn && state.value.userRole == Role.CUSTOMER) {
//                TopAppBar(
//                    colors = TopAppBarDefaults.topAppBarColors(
//                        containerColor = MaterialTheme.colorScheme.primary,
//                        titleContentColor = MaterialTheme.colorScheme.onPrimary
//                    ),
//                    title = {
//                        Box(
//                            modifier = Modifier.fillMaxWidth(),
//                            contentAlignment = Alignment.Center
//                        ) {
//                            Text(
//                                text = state.value.appBarTitle,
//                                style = MaterialTheme.typography.titleLarge
//                            )
//                        }
//                    }
//                )
//            }
//        },
//        bottomBar = {
//            if (state.value.isUserLoggedIn && state.value.userRole == Role.CUSTOMER) {
//                BottomNavigationBar(navController)
//            }
//        }
//    ) { innerPadding ->
//
//        if (state.value.isLoading) return@Scaffold
//
//        NavHost(
//            navController = navController,
//            startDestination = state.value.startDestination,
//            Modifier.padding(innerPadding)
//        ) {
//            composable(AuthNavItem.Welcome.route) {
//                WelcomeScreen(
//                    navController,
//                    onSuccess = {
//                        mainViewModel.checkUserStatus()
//                    }
//                )
//            }
//            composable(AuthNavItem.Login.route) {
//                AuthView(
//                    navController,
//                    AuthType.Login,
//                    onSuccess = {
//                        mainViewModel.checkUserStatus()
//                    }
//                )
//            }
//            composable(AuthNavItem.Register.route) {
//                AuthView(
//                    navController,
//                    AuthType.Register,
//                    onSuccess = {
//                        mainViewModel.checkUserStatus()
//                    }
//                )
//            }
//
//            composable(AuthNavItem.RoleSelection.route) {
//                // poprawic te warunki, mozliwe ze te metody viewmodelu moge zastapic jedna - checkuserstatus
//                RoleSelectionView(
//                    navController = navController,
//                    onCustomerSelect = { role ->
//                        when (role) {
//                            Role.BOTH, Role.CUSTOMER -> {
//                                mainViewModel.setUserRole(role)
//                                mainViewModel.navigateToMain(navController)
//                            }
//
//                            Role.NONE -> {
//                                mainViewModel.setUserRole(role)
//                                navController.navigate(AuthNavItem.CustomerData.route)
//                            }
//
//                            else -> {}
//                        }
//                    },
//                    onProviderSelect = { role ->
//                        when (role) {
//                            Role.BOTH, Role.PROVIDER -> {
//                                mainViewModel.setUserRole(role)
//                                mainViewModel.navigateToMain(navController)
//                            }
//
//                            Role.NONE -> {
//                                mainViewModel.setUserRole(role)
//                                navController.navigate(AuthNavItem.CustomerData.route)
//                            }
//
//                            else -> {}
//                        }
//                    }
//                )
//            }
//
//            composable(AuthNavItem.CustomerData.route) {
//                CustomerFormView(
//                    navController,
//                    onSuccess = {
////                        mainViewModel.checkUserRole()
//                        mainViewModel.checkUserStatus()
////                        mainViewModel.navigateToMain(navController)
//                    }
//                )
//            }
//
//            composable(AuthNavItem.ProviderData.route) {
//
//            }
//
//
//            composable(BottomNavItem.Offers.route) {
//                mainViewModel.setAppBarTitle(stringResource(R.string.offers))
//                OffersView()
//            }
//            composable(BottomNavItem.Requests.route) {
//                mainViewModel.setAppBarTitle(stringResource(R.string.requests))
//                RequestsView()
//            }
//            composable(BottomNavItem.Profile.route) {
//                mainViewModel.setAppBarTitle(stringResource(R.string.profile))
//                ProfileView()
//            }
//            composable(BottomNavItem.Schedule.route) {
//                mainViewModel.setAppBarTitle(stringResource(R.string.schedule))
//                ScheduleView()
//            }
//            composable(BottomNavItem.Settings.route) {
//                mainViewModel.setAppBarTitle(stringResource(R.string.settings))
//                SettingsView(onLogout = {
//                    mainViewModel.logout(navController)
//                })
//            }
//        }
//    }
//}
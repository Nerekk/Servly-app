package com.example.servly_app.core

import android.content.res.Configuration
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.R
import com.example.servly_app.core.ui.navigation.BottomNavItem
import com.example.servly_app.core.ui.navigation.BottomNavigationBar
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.authentication.presentation.login_view.AuthType
import com.example.servly_app.features.authentication.presentation.login_view.AuthView
import com.example.servly_app.features.authentication.presentation.login_view.WelcomeScreen
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.features.offers.OffersView
import com.example.servly_app.features.profile.ProfileView
import com.example.servly_app.features.requests.main_view.RequestsView
import com.example.servly_app.features.schedule.ScheduleView
import com.example.servly_app.features.settings.SettingsView

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavGraph(mainViewModel: MainViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val appBarTitle by mainViewModel.appBarTitle.collectAsState()
    val isUserLoggedIn by mainViewModel.isUserLoggedIn.collectAsState()
    val startDestination by mainViewModel.startDestination.collectAsState()

    Scaffold(
        topBar = {
            if (isUserLoggedIn) {
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
                                text = appBarTitle,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (isUserLoggedIn) {
                BottomNavigationBar(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            Modifier.padding(innerPadding)
        ) {
            composable(AuthNavItem.Welcome.route) {
                WelcomeScreen(navController, onSuccess = { mainViewModel.navigateToMain(navController) })
            }
            composable(AuthNavItem.Login.route) {
                AuthView(navController, AuthType.Login, onSuccess = { mainViewModel.navigateToMain(navController) })
            }
            composable(AuthNavItem.Register.route) {
                AuthView(navController, AuthType.Register, onSuccess = { mainViewModel.navigateToMain(navController) })
            }


            composable(BottomNavItem.Offers.route) {
                mainViewModel.setAppBarTitle(stringResource(R.string.offers))
                OffersView()
            }
            composable(BottomNavItem.Requests.route) {
                mainViewModel.setAppBarTitle(stringResource(R.string.requests))
                RequestsView()
            }
            composable(BottomNavItem.Profile.route) {
                mainViewModel.setAppBarTitle(stringResource(R.string.profile))
                ProfileView()
            }
            composable(BottomNavItem.Schedule.route) {
                mainViewModel.setAppBarTitle(stringResource(R.string.schedule))
                ScheduleView()
            }
            composable(BottomNavItem.Settings.route) {
                mainViewModel.setAppBarTitle(stringResource(R.string.settings))
                SettingsView(onLogout = {
                    mainViewModel.logout(navController)
                })
            }
        }
    }
}
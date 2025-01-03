package com.example.servly_app.features

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.R
import com.example.servly_app.core.navigation.BottomNavItem
import com.example.servly_app.core.navigation.BottomNavigationBar
import com.example.servly_app.core.theme.AppTheme
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
        MainView()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainView() {
    val navController = rememberNavController()
    var appBarTitle by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary, // Tło AppBar
                    titleContentColor = MaterialTheme.colorScheme.onPrimary // Kolor tekstu
                ),
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(), // Wypełnij szerokość AppBar
                        contentAlignment = Alignment.Center // Wyśrodkuj zawartość
                    ) {
                        Text(
                            text = appBarTitle,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Offers.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Offers.route) {
                appBarTitle = stringResource(R.string.offers)
                OffersView()
            }
            composable(BottomNavItem.Requests.route) {
                appBarTitle = stringResource(R.string.requests)
                RequestsView()
            }
            composable(BottomNavItem.Profile.route) {
                appBarTitle = stringResource(R.string.profile)
                ProfileView()
            }
            composable(BottomNavItem.Schedule.route) {
                appBarTitle = stringResource(R.string.schedule)
                ScheduleView()
            }
            composable(BottomNavItem.Settings.route) {
                appBarTitle = stringResource(R.string.settings)
                SettingsView()
            }
        }
    }
}
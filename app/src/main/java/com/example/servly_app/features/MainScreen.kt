package com.example.servly_app.features

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.core.navigation.BottomNavItem
import com.example.servly_app.core.navigation.BottomNavigationBar
import com.example.servly_app.features.offers.OffersView
import com.example.servly_app.features.profile.ProfileView
import com.example.servly_app.features.requests.RequestsView
import com.example.servly_app.features.schedule.ScheduleView
import com.example.servly_app.features.settings.SettingsView

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomNavigationBar(navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomNavItem.Offers.route,
            Modifier.padding(innerPadding)
        ) {
            composable(BottomNavItem.Offers.route) { OffersView() }
            composable(BottomNavItem.Requests.route) { RequestsView() }
            composable(BottomNavItem.Profile.route) { ProfileView() }
            composable(BottomNavItem.Schedule.route) { ScheduleView() }
            composable(BottomNavItem.Settings.route) { SettingsView() }
        }
    }
}
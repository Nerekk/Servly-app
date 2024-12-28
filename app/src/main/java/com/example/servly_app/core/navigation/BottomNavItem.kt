package com.example.servly_app.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val title: String,
    val hasNews: Boolean,
    val badgeCount: Int? = null
) {
    object Offers : BottomNavItem(
        "offers",
        Icons.Filled.Home,
        Icons.Outlined.Home,
        "Offers",
        false
    )

    object Requests : BottomNavItem(
        "requests",
        Icons.Filled.Email,
        Icons.Outlined.Email,
        "Requests",
        false
    )

    object Profile : BottomNavItem(
        "profile",
        Icons.Filled.Person,
        Icons.Outlined.Person,
        "Profile",
        false
    )

    object Schedule : BottomNavItem(
        "schedule",
        Icons.Filled.DateRange,
        Icons.Outlined.DateRange,
        "Schedule",
        false
    )

    object Settings : BottomNavItem(
        "settings",
        Icons.Filled.Settings,
        Icons.Outlined.Settings,
        "Settings",
        false
    )
}
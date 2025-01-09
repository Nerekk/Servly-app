package com.example.servly_app.core.ui.navigation

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
import com.example.servly_app.R

sealed class BottomNavItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
) {
    fun getTitle(context: android.content.Context): String {
        return when (this) {
            is Offers -> context.getString(R.string.offers)
            is Requests -> context.getString(R.string.requests)
            is Profile -> context.getString(R.string.profile)
            is Schedule -> context.getString(R.string.schedule)
            is Settings -> context.getString(R.string.settings)
        }
    }

    object Offers : BottomNavItem(
        "offers",
        Icons.Filled.Home,
        Icons.Outlined.Home,
        false
    )

    object Requests : BottomNavItem(
        "requests",
        Icons.Filled.Email,
        Icons.Outlined.Email,
        false
    )

    object Profile : BottomNavItem(
        "profile",
        Icons.Filled.Person,
        Icons.Outlined.Person,
        false
    )

    object Schedule : BottomNavItem(
        "schedule",
        Icons.Filled.DateRange,
        Icons.Outlined.DateRange,
        false
    )

    object Settings : BottomNavItem(
        "settings",
        Icons.Filled.Settings,
        Icons.Outlined.Settings,
        false
    )
}
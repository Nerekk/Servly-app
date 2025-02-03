package com.example.servly_app.core.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.servly_app.R

sealed class NavItem(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean,
    val badgeCount: Int? = null
) {
    fun getTitle(context: android.content.Context): String {
        return when (this) {
            is Customer.Offers -> context.getString(R.string.offers_customer)
            is Provider.Offers -> context.getString(R.string.offers_provider)
            is Customer.Requests -> context.getString(R.string.requests_customer)
            is Provider.Requests -> context.getString(R.string.requests_provider)
            is Customer.Profile, Provider.Profile -> context.getString(R.string.profile)
            is Customer.Schedule, Provider.Schedule -> context.getString(R.string.schedule)
            is Customer.Settings, Provider.Settings -> context.getString(R.string.settings)
        }
    }

    object Customer {
        object Offers : NavItem(
            "customer_offers",
            Icons.Filled.AddCircle,
            Icons.Outlined.AddCircle,
            false
        )

        object Requests : NavItem(
            "customer_requests",
            Icons.AutoMirrored.Filled.List,
            Icons.AutoMirrored.Outlined.List,
            false
        )

        object Profile : NavItem(
            "customer_profile",
            Icons.Filled.Person,
            Icons.Outlined.Person,
            false
        )

        object Schedule : NavItem(
            "customer_schedule",
            Icons.Filled.DateRange,
            Icons.Outlined.DateRange,
            false
        )

        object Settings : NavItem(
            "customer_settings",
            Icons.Filled.Settings,
            Icons.Outlined.Settings,
            false
        )
    }

    object Provider {
        object Offers : NavItem(
            "provider_offers",
            Icons.AutoMirrored.Filled.List,
            Icons.AutoMirrored.Outlined.List,
            false
        )

        object Requests : NavItem(
            "provider_requests",
            Icons.Filled.Email,
            Icons.Outlined.Email,
            false
        )

        object Profile : NavItem(
            "provider_profile",
            Icons.Filled.Person,
            Icons.Outlined.Person,
            false
        )

        object Schedule : NavItem(
            "provider_schedule",
            Icons.Filled.DateRange,
            Icons.Outlined.DateRange,
            false
        )

        object Settings : NavItem(
            "provider_settings",
            Icons.Filled.Settings,
            Icons.Outlined.Settings,
            false
        )
    }

}
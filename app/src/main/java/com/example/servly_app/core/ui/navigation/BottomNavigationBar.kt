package com.example.servly_app.core.ui.navigation

import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController, items: List<NavItem>) {
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val context = LocalContext.current

    NavigationBar {
        items.forEachIndexed { _, item ->
            NavigationBarItem(
                selected = item.route == currentRoute,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(0) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                label = {
                    Text(
                        text = item.getTitle(context),
                        style = MaterialTheme.typography.labelSmall
                    )
                },
                icon = {
                    BadgedBox(
                        badge = {
                            if (item.badgeCount != null) {
                                Badge {
                                    Text(text = item.badgeCount.toString())
                                }
                            } else if (item.hasNews) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = if (item.route == currentRoute) {
                                item.selectedIcon
                            } else item.unselectedIcon,
                            contentDescription = item.route
                        )
                    }
                }

            )
        }
    }
}

val CUSTOMER_ITEMS = listOf(
    NavItem.Customer.Offers,
    NavItem.Customer.Requests,
    NavItem.Customer.Profile,
    NavItem.Customer.Schedule,
    NavItem.Customer.Settings
)

val PROVIDER_ITEMS = listOf(
    NavItem.Provider.Offers,
    NavItem.Provider.Requests,
    NavItem.Provider.Profile,
    NavItem.Provider.Schedule,
    NavItem.Provider.Settings
)
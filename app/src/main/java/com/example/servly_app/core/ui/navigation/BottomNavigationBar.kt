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

@Composable
fun BottomNavigationBar(navController: NavController, items: List<NavItem>) {
    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }
    val context = LocalContext.current

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = index == selectedItemIndex,
                onClick = {
                    selectedItemIndex = index
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
                            imageVector = if (index == selectedItemIndex) {
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
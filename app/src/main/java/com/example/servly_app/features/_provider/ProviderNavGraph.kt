package com.example.servly_app.features._provider

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.servly_app.R
import com.example.servly_app.core.MainState
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.ui.navigation.BottomNavigationBar
import com.example.servly_app.core.ui.navigation.NavItem
import com.example.servly_app.core.ui.navigation.PROVIDER_ITEMS
import com.example.servly_app.features._customer._navigation.CustomerNavItem
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.features._customer.profile.ProfileView
import com.example.servly_app.features._provider._navigation.ProviderNavItem
import com.example.servly_app.features.job_details.presentation.JobDetailsView
import com.example.servly_app.features._provider.job_list.presentation.ProviderJobListView
import com.example.servly_app.features._provider.profile.ProviderProfileView
import com.example.servly_app.features._provider.job_request_list.JobRequestListView
import com.example.servly_app.features._provider.schedule.ProviderScheduleView
import com.example.servly_app.features._provider.settings.ProviderSettingsView
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.features.chat.presentation.ChatView
import com.example.servly_app.features.role_selection.presentation.user_data.CustomerFormView
import com.example.servly_app.features.role_selection.presentation.user_data.ProviderFormView


private val topBarExcludedRoutes = listOf(
    AuthNavItem.ProviderData.route,
    ProviderNavItem.Chat.route + "/{jobRequestId}"
)

private val bottomBarIncludedRoutes = listOf(
    NavItem.Provider.Jobs.route,
    NavItem.Provider.Requests.route,
    NavItem.Provider.Profile.route,
    NavItem.Provider.Schedule.route,
    NavItem.Provider.Settings.route
)

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProviderNavGraph(
    state: State<MainState>,
    navController: NavHostController,
    setAppBarTitle: (String) -> Unit,
    checkUserStatus: () -> Unit,
    logout: () -> Unit
) {
    val isVisibleTop = remember { mutableStateOf(true) }
    val isVisibleBottom = remember { mutableStateOf(true) }

    val viewModel: ProviderNavViewModel = hiltViewModel()
    val providerState = viewModel.providerState.collectAsState()

    val context = LocalContext.current

    LaunchedEffect(navController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            isVisibleTop.value = destination.route !in topBarExcludedRoutes
            isVisibleBottom.value = destination.route in bottomBarIncludedRoutes
        }
    }

    Scaffold(
        topBar = {
            if (isVisibleTop.value) {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = state.value.appBarTitle,
                                style = MaterialTheme.typography.titleLarge
                            )
                        }
                    },
                    navigationIcon = {
                        if (navController.previousBackStackEntry != null) {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    }
                )
            }
        },
        bottomBar = {
            if (isVisibleBottom.value) {
                BottomNavigationBar(navController, PROVIDER_ITEMS)
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = NavItem.Provider.Jobs.route,
            modifier = if (isVisibleBottom.value || isVisibleTop.value) Modifier.padding(innerPadding) else Modifier
        ) {

            composable(AuthNavItem.CustomerData.route) {
                CustomerFormView(
                    navController,
                    onSuccess = { checkUserStatus() }
                )
            }

            composable(NavItem.Provider.Jobs.route) {
                setAppBarTitle(stringResource(R.string.offers_provider))
                ProviderJobListView(
                    onClickShowDetails = { order ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("orderDetails", order)
                        navController.navigate(ProviderNavItem.JobPostingDetails.route)
                    }
                )
            }
            composable(ProviderNavItem.JobPostingDetails.route) {
                val order: JobPostingInfo? = navController.previousBackStackEntry?.savedStateHandle?.get("orderDetails")

                setAppBarTitle(stringResource(R.string.order_details))
                Log.i("OrderDetailsView", "Composition")

                if (order != null) {
                    JobDetailsView(
                        order,
                        Role.PROVIDER,
                        showProviderProfile = {},
                        showCustomerProfile = { id ->
                            navController.navigate(ProviderNavItem.ProfilePreview.route + "/$id")
                        },
                        openChat = { },
                        providerId = providerState.value.providerId
                    )
                }
            }

            composable(NavItem.Provider.Requests.route) {
                setAppBarTitle(stringResource(R.string.requests_provider))
                JobRequestListView(
                    onClickShowDetails = { jobPosting ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("orderDetails", jobPosting)
                        navController.navigate(ProviderNavItem.JobDetailsView.route)
                    }
                )
            }
            composable(ProviderNavItem.JobDetailsView.route) {
                val order: JobPostingInfo? = navController.previousBackStackEntry?.savedStateHandle?.get("orderDetails")

                setAppBarTitle(stringResource(R.string.order_details))
                Log.i("OrderDetailsView", "Composition")

                if (order != null) {
                    JobDetailsView(
                        order,
                        Role.PROVIDER,
                        showProviderProfile = {},
                        showCustomerProfile = { id ->
                            navController.navigate(ProviderNavItem.ProfilePreview.route + "/$id")
                        },
                        openChat = { id ->
                            navController.navigate(ProviderNavItem.Chat.route + "/$id")
                        },
                        providerId = providerState.value.providerId
                    )
                }
            }
            composable(
                ProviderNavItem.ProfilePreview.route + "/{customerId}",
                arguments = listOf(navArgument("customerId") { type = NavType.LongType })
            ) { backstack ->
                val customerId = backstack.arguments?.getLong("customerId")
                if (customerId != null) {
                    ProfileView(customerId)
                } else {
                    Log.i("NAVIGATION", "CustomerId is null")
                }
            }
            composable(
                ProviderNavItem.Chat.route + "/{jobRequestId}",
                arguments = listOf(navArgument("jobRequestId") { type = NavType.LongType })
            ) { backstack ->
                val jobRequestId = backstack.arguments?.getLong("jobRequestId")
                if (jobRequestId != null) {
                    ChatView(
                        navController,
                        jobRequestId
                    )
                }
            }

            composable(NavItem.Provider.Profile.route) {
                setAppBarTitle(stringResource(R.string.profile))
                if (providerState.value.providerId != null) {
                    ProviderProfileView(
                        providerState.value.providerId!!,
                        onContactEdit = {
                            navController.navigate(AuthNavItem.ProviderData.route)
                        }
                    )
                }
            }
            composable(AuthNavItem.ProviderData.route) {
                ProviderFormView(
                    navController,
                    providerState.value.toProviderInfo(),
                    onSuccess = {
                        navController.popBackStack()
                        Toast.makeText(context, "Update profile success", Toast.LENGTH_SHORT).show()
                        viewModel.loadProvider()
                    }
                )
            }

            composable(NavItem.Provider.Schedule.route) {
                setAppBarTitle(stringResource(R.string.schedule))
                ProviderScheduleView()
            }
            composable(NavItem.Provider.Settings.route) {
                setAppBarTitle(stringResource(R.string.settings))
                ProviderSettingsView(
                    onSwitchRole = {
                        if (state.value.userRole == Role.BOTH) {
                            checkUserStatus()
                        } else {
                            navController.navigate(AuthNavItem.ProviderData.route)
                        }
                    },
                    onLogout = { logout() }
                )
            }
        }
    }
}
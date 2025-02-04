package com.example.servly_app.features._customer

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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.servly_app.R
import com.example.servly_app.core.MainState
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.ui.navigation.BottomNavigationBar
import com.example.servly_app.core.ui.navigation.CUSTOMER_ITEMS
import com.example.servly_app.core.ui.navigation.NavItem
import com.example.servly_app.features._customer._navigation.CustomerNavItem
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.features._customer.job_create.presentation.job_form.JobFormView
import com.example.servly_app.features._customer.job_create.presentation.main.JobCategoryView
import com.example.servly_app.features._customer.job_create.presentation.main.components.JobCategory
import com.example.servly_app.features._customer.job_list.presentation.details_view.OrderDetailsView
import com.example.servly_app.features._customer.profile.ProfileView
import com.example.servly_app.features._customer.job_list.presentation.main_view.OrderListView
import com.example.servly_app.features._customer.schedule.ScheduleView
import com.example.servly_app.features._customer.settings.SettingsView
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.features.role_selection.presentation.user_data.CustomerFormView
import com.example.servly_app.features.role_selection.presentation.user_data.ProviderFormView

val topBarExcludedRoutes = listOf(
    AuthNavItem.CustomerData.route
)

val bottomBarIncludedRoutes = listOf(
    NavItem.Customer.Offers.route,
    NavItem.Customer.Requests.route,
    NavItem.Customer.Profile.route,
    NavItem.Customer.Schedule.route,
    NavItem.Customer.Settings.route
)


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun CustomerNavGraph(
    state: State<MainState>,
    navController: NavHostController,
    setAppBarTitle: (String) -> Unit,
    checkUserStatus: () -> Unit,
    logout: () -> Unit
) {
    val isVisibleTop = remember { mutableStateOf(true) }
    val isVisibleBottom = remember { mutableStateOf(true) }

    val viewModel: CustomerNavViewModel = hiltViewModel()
    val customerState = viewModel.customerState.collectAsState()

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
                    },
                )
            }
        },
        bottomBar = {
            if (isVisibleBottom.value) {
                BottomNavigationBar(navController, CUSTOMER_ITEMS)
            }
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = NavItem.Customer.Offers.route,
            modifier = if (isVisibleBottom.value || isVisibleTop.value) Modifier.padding(innerPadding) else Modifier
        ) {

            composable(NavItem.Customer.Offers.route) {
                setAppBarTitle(stringResource(R.string.offers_customer))
                JobCategoryView(
                    onCategorySelect = { category ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("serviceCategory", category)
                        navController.navigate(CustomerNavItem.JobFormView.route)
                    }
                )
            }
            composable(CustomerNavItem.JobFormView.route) {
                val jobCategory: JobCategory? = navController.previousBackStackEntry?.savedStateHandle?.get("serviceCategory")
                jobCategory?.name?.let { setAppBarTitle(it) }
                JobFormView(
                    jobCategory,
                    customerState,
                    onSuccess = {
                        navController.popBackStack()
                        Toast.makeText(context, "Create job success", Toast.LENGTH_SHORT).show()
                    }
                )
            }


            composable(NavItem.Customer.Requests.route) {
                setAppBarTitle(stringResource(R.string.requests_customer))
                OrderListView(
                    onClickShowDetails = { order ->
                        navController.currentBackStackEntry?.savedStateHandle?.set("orderDetails", order)
                        navController.navigate(CustomerNavItem.OrderDetailsView.route)
                    }
                )
            }
            composable(CustomerNavItem.OrderDetailsView.route) {
                val order: JobPostingInfo? = navController.previousBackStackEntry?.savedStateHandle?.get("orderDetails")

                setAppBarTitle(stringResource(R.string.order_details))
                Log.i("OrderDetailsView", "Composition")

                if (order != null) {
                    OrderDetailsView(order)
                }
            }


            composable(NavItem.Customer.Profile.route) {
                setAppBarTitle(stringResource(R.string.profile))
                ProfileView(
                    customerState,
                    onEditClick = {
                        navController.navigate(AuthNavItem.CustomerData.route)
                    }
                )
            }
            composable(AuthNavItem.CustomerData.route) {
                CustomerFormView(
                    navController,
                    customerState.value.toCustomerInfo(),
                    onSuccess = {
                        navController.popBackStack()
                        Toast.makeText(context, "Update profile success", Toast.LENGTH_SHORT).show()
                        viewModel.loadCustomer()
                    }
                )
            }

            composable(NavItem.Customer.Schedule.route) {
                setAppBarTitle(stringResource(R.string.schedule))
                ScheduleView()
            }
            composable(NavItem.Customer.Settings.route) {
                setAppBarTitle(stringResource(R.string.settings))
                SettingsView(
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
            composable(AuthNavItem.ProviderData.route) {
                ProviderFormView(
                    navController,
                    onSuccess = { checkUserStatus() }
                )
            }
        }
    }
}
package com.example.servly_app.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.ui.navigation.NavItem
import com.example.servly_app.features.authentication.domain.usecase.AuthUseCases
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.core.domain.usecase.GetUserRoles
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class MainState(
    val appBarTitle: String = "",
    val isUserLoggedIn: Boolean = false,
    val startDestination: String = AuthNavItem.Welcome.route,
    val userRole: Role? = null,
    val isLoading: Boolean = false
)


@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val getUserRoles: GetUserRoles
) : ViewModel() {

    private val _mainState = MutableStateFlow(MainState())
    val mainState = _mainState.asStateFlow()


    init {
        checkUserStatus()
    }

    fun setAppBarTitle(title: String) {
        _mainState.update { it.copy(appBarTitle = title) }
    }

    fun setUserRole(role: Role) {
        _mainState.update { it.copy(userRole = role) }
    }

    fun checkUserStatus() {
        viewModelScope.launch {
            _mainState.update { it.copy(isLoading = true) }

            val isLoggedIn = authUseCases.checkUserLoggedIn()
            _mainState.update { it.copy(isUserLoggedIn = isLoggedIn) }

            if (isLoggedIn) {
                val fcmToken = getFcmToken()
                val result = getUserRoles(fcmToken ?: "")
                Log.i("USER_STATUS", "ROLE = $result, FCM: $fcmToken")

                result.fold(
                    onSuccess = { role ->
                        _mainState.update {
                            it.copy(
                                userRole = role,
                                startDestination = when (role) {
                                    Role.BOTH, Role.NONE -> AuthNavItem.RoleSelection.route
                                    Role.CUSTOMER -> NavItem.Customer.Services.route
                                    Role.PROVIDER -> NavItem.Provider.Jobs.route
                                }
                            )
                        }
                    },
                    onFailure = {
//                        _mainState.update { it.copy(startDestination = AuthNavItem.Welcome.route) }
                        logout()
                        Log.i("USER_STATUS", "USER ROLE FAILURE")
                    }
                )
            } else {
                _mainState.update { it.copy(startDestination = AuthNavItem.Welcome.route) }
            }
            Log.i("USER_STATUS", "IS LOGGED IN = $isLoggedIn")

            _mainState.update { it.copy(isLoading = false) }
        }
    }

    fun setDestinationAuth() {
        _mainState.update { it.copy(startDestination = AuthNavItem.Welcome.route) }
    }

    fun setDestinationRole() {
        _mainState.update { it.copy(startDestination = AuthNavItem.RoleSelection.route) }
    }

    fun setDestinationCustomer() {
        _mainState.update { it.copy(startDestination = NavItem.Customer.Services.route) }
    }

    fun setDestinationProvider() {
        _mainState.update { it.copy(startDestination = NavItem.Provider.Jobs.route) }
    }

    fun logout() {
        viewModelScope.launch {
            authUseCases.logout()
            _mainState.update { it.copy(isUserLoggedIn = false, userRole = null, startDestination = AuthNavItem.Welcome.route) }
        }
    }
}


suspend fun getFcmToken(): String? {
    return try {
        FirebaseMessaging.getInstance().token.await()
    } catch (e: Exception) {
        null
    }
}
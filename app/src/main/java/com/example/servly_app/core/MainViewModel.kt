package com.example.servly_app.core

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.servly_app.core.ui.navigation.BottomNavItem
import com.example.servly_app.features.authentication.domain.usecase.AuthUseCases
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _appBarTitle = MutableStateFlow("")
    val appBarTitle: StateFlow<String> = _appBarTitle.asStateFlow()

    private val _isUserLoggedIn = MutableStateFlow(false)
    val isUserLoggedIn: StateFlow<Boolean> = _isUserLoggedIn.asStateFlow()

    private val _startDestination = MutableStateFlow(AuthNavItem.Welcome.route)
    val startDestination: StateFlow<String> = _startDestination.asStateFlow()

    init {
        checkUserLoginStatus()
    }

    fun setAppBarTitle(title: String) {
        _appBarTitle.value = title
    }

    private fun checkUserLoginStatus() {
        viewModelScope.launch {
            val isLoggedIn = authUseCases.checkUserLoggedIn()
            _isUserLoggedIn.value = isLoggedIn
            _startDestination.value = if (isLoggedIn) BottomNavItem.Offers.route else AuthNavItem.Welcome.route
        }
    }

    fun navigateToMain(navController: NavHostController) {
        _isUserLoggedIn.value = true
        _startDestination.value = BottomNavItem.Offers.route
        navController.navigate(BottomNavItem.Offers.route) {
            popUpTo(0)
        }
    }

    fun logout(navController: NavHostController) {
        viewModelScope.launch {
            authUseCases.logout()
            _isUserLoggedIn.value = false
            _startDestination.value = AuthNavItem.Welcome.route
            navController.navigate(AuthNavItem.Welcome.route) {
                popUpTo(0)
            }
        }
    }
}
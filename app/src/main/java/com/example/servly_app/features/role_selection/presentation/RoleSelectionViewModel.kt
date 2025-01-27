package com.example.servly_app.features.role_selection.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.domain.usecase.GetUserRoles
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RoleState(
    val role: Role? = null
)

@HiltViewModel
class RoleSelectionViewModel @Inject constructor(
    private val getUserRoles: GetUserRoles
): ViewModel() {

    private val _roleState = MutableStateFlow(RoleState())
    val roleState: StateFlow<RoleState> = _roleState.asStateFlow()

    init {
        getUserRole()
    }

    private fun getUserRole() {
        viewModelScope.launch {
            Log.i("RSLAUNCH", "Getuserrole")
            val result = getUserRoles()
            result.fold(
                onSuccess = { userRole -> _roleState.update { it.copy(role = userRole) } },
                onFailure = {
                    Log.i("RSLAUNCH", "FAIL")
                }
            )
            Log.i("RSLAUNCH", "${_roleState.value.role}")
        }
    }
}
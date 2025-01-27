package com.example.servly_app.features.role_selection.presentation.user_data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.features.role_selection.data.ProviderInfo
import com.example.servly_app.features.role_selection.domain.usecase.ProviderFormUseCases
import com.example.servly_app.features.role_selection.presentation.components.RegexConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProviderState(
    val name: String = "",
    val phoneNumber: String = "",

    val city: String = "",
    val rangeInKm: Double = 30.0,

    val isValid: Boolean = true,
    val nameError: String? = null,
    val phoneError: String? = null,
    val cityError: String? = null,

    val isLoading: Boolean = false
) {
    fun toProviderInfo(): ProviderInfo {
        return ProviderInfo(
            name,
            phoneNumber,
            city,
            rangeInKm
        )
    }
}

@HiltViewModel
class ProviderFormViewModel @Inject constructor(
    private val providerFormUseCases: ProviderFormUseCases
): ViewModel() {
    private val _providerState = MutableStateFlow(ProviderState())
    val providerState: StateFlow<ProviderState> = _providerState.asStateFlow()

    fun updateName(name: String) {
        _providerState.update { it.copy(name = name) }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _providerState.update { it.copy(phoneNumber = phoneNumber) }
    }

    fun updateCity(city: String) {
        _providerState.update { it.copy(city = city) }
    }

    fun updateRange(rangeInKm: Double) {
        _providerState.update { it.copy(rangeInKm = rangeInKm) }
    }



    private fun validateInputs(): Boolean {
        val state = _providerState.value
        var isValid = true

        val nameError = when {
            state.name.isEmpty() -> "Name cannot be empty"
            state.name.any { it.isDigit() } -> "Name cannot contain numbers"
            !RegexConstants.NAME.matches(state.name) -> "Name cannot contain special characters"
            else -> null
        }
        val phoneError = when {
            state.phoneNumber.isEmpty() -> "Phone number cannot be empty"
            !RegexConstants.PHONE.matches(state.phoneNumber) -> "Incorrect phone number format"
            else -> null
        }
        val cityError = when {
            state.city.isEmpty() -> "City cannot be empty"
            else -> null
        }


        if (nameError != null || phoneError != null || cityError != null) {
            isValid = false
        }

        _providerState.update {
            it.copy(
                nameError = nameError,
                phoneError = phoneError,
                cityError = cityError
            )
        }
        Log.i("customerValidate", "$isValid")

        return isValid
    }

    fun createProvider(onSuccess: () -> Unit, onFailure: () -> Unit) {
        Log.i("createProvider", "Before validate")
        if (!validateInputs()) return
        Log.i("createProvider", "After validate")
        viewModelScope.launch {
            _providerState.update { it.copy(isLoading = true) }
            val result = providerFormUseCases.createProvider(_providerState.value.toProviderInfo())
            _providerState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { onFailure() }
            )
        }
    }
}
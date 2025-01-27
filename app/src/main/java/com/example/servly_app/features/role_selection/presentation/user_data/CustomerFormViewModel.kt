package com.example.servly_app.features.role_selection.presentation.user_data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.features.role_selection.data.CustomerInfo
import com.example.servly_app.features.role_selection.domain.usecase.CustomerFormUseCases
import com.example.servly_app.features.role_selection.presentation.components.RegexConstants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerState(
    val name: String = "",
    val phoneNumber: String = "",

    val city: String = "",
    val street: String = "",
    val houseNumber: String = "",

    val isValid: Boolean = true,
    val nameError: String? = null,
    val phoneError: String? = null,
    val cityError: String? = null,
    val streetError: String? = null,

    val isLoading: Boolean = false
) {
    fun toCustomerInfo(): CustomerInfo {
        return CustomerInfo(
            name,
            phoneNumber,
            city,
            street,
            houseNumber = houseNumber.ifEmpty { null }
        )
    }
}

@HiltViewModel
class CustomerFormViewModel @Inject constructor(
    private val customerFormUseCases: CustomerFormUseCases
): ViewModel() {
    private val _customerState = MutableStateFlow(CustomerState())
    val customerState: StateFlow<CustomerState> = _customerState.asStateFlow()

    fun updateName(name: String) {
        _customerState.update { it.copy(name = name) }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _customerState.update { it.copy(phoneNumber = phoneNumber) }
    }

    fun updateCity(city: String) {
        _customerState.update { it.copy(city = city) }
    }

    fun updateStreet(street: String) {
        _customerState.update { it.copy(street = street) }
    }

    fun updateHouseNumber(houseNumber: String) {
        _customerState.update { it.copy(houseNumber = houseNumber) }
    }

    private fun validateInputs(): Boolean {
        val state = _customerState.value
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
        val streetError = when {
            state.street.isEmpty() -> "Street cannot be empty"
            else -> null
        }

        if (nameError != null || phoneError != null || cityError != null || streetError != null) {
            isValid = false
        }

        _customerState.update {
            it.copy(
                nameError = nameError,
                phoneError = phoneError,
                cityError = cityError,
                streetError = streetError
            )
        }
        Log.i("customerValidate", "$isValid")

        return isValid
    }

    fun createCustomer(onSuccess: () -> Unit, onFailure: () -> Unit) {
        Log.i("createCustomer", "Before validate")
        if (!validateInputs()) return
        Log.i("createCustomer", "After validate")
        viewModelScope.launch {
            _customerState.update { it.copy(isLoading = true) }
            val result = customerFormUseCases.createCustomer(_customerState.value.toCustomerInfo())
            _customerState.update { it.copy(isLoading = false) }

            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { onFailure() }
            )
        }
    }

}
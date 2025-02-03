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

    val isLoading: Boolean = false,

    val isEditForm: Boolean = false,
    val isButtonEnabled: Boolean = true
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

    fun isEqualToCustomerInfo(customerInfo: CustomerInfo): Boolean {
        return name == customerInfo.name &&
                phoneNumber == customerInfo.phoneNumber &&
                city == customerInfo.city &&
                street == customerInfo.street &&
                houseNumber == customerInfo.houseNumber
    }
}

@HiltViewModel
class CustomerFormViewModel @Inject constructor(
    private val customerFormUseCases: CustomerFormUseCases
): ViewModel() {
    private val _customerState = MutableStateFlow(CustomerState())
    val customerState: StateFlow<CustomerState> = _customerState.asStateFlow()

    private var customerInfoCopy: CustomerInfo? = null

    fun updateName(name: String) {
        compareInputs()
        _customerState.update { it.copy(name = name) }
    }

    fun updatePhoneNumber(phoneNumber: String) {
        compareInputs()
        _customerState.update { it.copy(phoneNumber = phoneNumber) }
    }

    fun updateCity(city: String) {
        compareInputs()
        _customerState.update { it.copy(city = city) }
    }

    fun updateStreet(street: String) {
        compareInputs()
        _customerState.update { it.copy(street = street) }
    }

    fun updateHouseNumber(houseNumber: String) {
        compareInputs()
        _customerState.update { it.copy(houseNumber = houseNumber) }
    }

    fun setEditData(customerInfo: CustomerInfo) {
        _customerState.update {
            it.copy(
                name = customerInfo.name,
                phoneNumber = customerInfo.phoneNumber,
                city = customerInfo.city,
                street = customerInfo.street,
                houseNumber = customerInfo.houseNumber ?: "",
                isEditForm = true,
                isButtonEnabled = false
            )
        }
        customerInfoCopy = customerInfo
    }


    private fun compareInputs() {
        customerInfoCopy?.let { copy ->
            _customerState.update {
                it.copy(
                    isButtonEnabled = !it.isEqualToCustomerInfo(copy)
                )
            }
        }
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

    fun handleCustomer(onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (_customerState.value.isEditForm) {
            updateCustomer(onSuccess, onFailure)
        } else {
            createCustomer(onSuccess, onFailure)
        }
    }

    fun createCustomer(onSuccess: () -> Unit, onFailure: () -> Unit) {
        Log.i("createCustomer", "Before validate")
        if (!validateInputs()) return
        Log.i("createCustomer", "After validate")
        viewModelScope.launch {
            _customerState.update { it.copy(isLoading = true) }
            val result = customerFormUseCases.createCustomer(_customerState.value.toCustomerInfo())

            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { onFailure() }
            )
            _customerState.update { it.copy(isLoading = false) }
        }
    }

    fun updateCustomer(onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (!validateInputs()) return

        viewModelScope.launch {
            _customerState.update { it.copy(isLoading = true) }
            val result = customerFormUseCases.updateCustomer(_customerState.value.toCustomerInfo())

            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { onFailure() }
            )

            _customerState.update { it.copy(isLoading = false) }
        }
    }

}
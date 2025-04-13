package com.example.servly_app.features.role_selection.presentation.user_data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.features.role_selection.data.CustomerInfo
import com.example.servly_app.features.role_selection.domain.usecase.CustomerFormUseCases
import com.example.servly_app.features.role_selection.presentation.components.RegexConstants
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerState(
    val customerId: Long? = null,

    val name: String = "",
    val phoneNumber: String = "",

    val selectedAddress: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,

    val isValid: Boolean = true,
    val nameError: String? = null,
    val phoneError: String? = null,

    val addressError: String? = null,

    val isLoading: Boolean = false,

    val isEditForm: Boolean = false,
    val isButtonEnabled: Boolean = true
) {
    fun toCustomerInfo(): CustomerInfo {
        return CustomerInfo(
            customerId,
            name,
            phoneNumber,
            selectedAddress,
            latitude,
            longitude
        )
    }

    fun isEqualToCustomerInfo(customerInfo: CustomerInfo): Boolean {
        return name == customerInfo.name &&
                phoneNumber == customerInfo.phoneNumber &&
                selectedAddress == customerInfo.address &&
                latitude == customerInfo.latitude &&
                longitude == customerInfo.longitude
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
        _customerState.update { it.copy(name = name) }
        compareInputs()
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _customerState.update { it.copy(phoneNumber = phoneNumber) }
        compareInputs()
    }

    fun updateAddress(address: String, latLng: LatLng) {
        _customerState.update { it.copy(
            selectedAddress = address,
            longitude = latLng.longitude,
            latitude = latLng.latitude
        ) }
        compareInputs()
    }


    fun setEditData(customerInfo: CustomerInfo) {
        _customerState.update {
            it.copy(
                customerId = customerInfo.customerId,
                name = customerInfo.name,
                phoneNumber = customerInfo.phoneNumber,
                selectedAddress = customerInfo.address,
                latitude = customerInfo.latitude,
                longitude = customerInfo.longitude,
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
        val addressError = if (state.selectedAddress == null) { "Address is required" } else null

        if (nameError != null || phoneError != null || addressError != null) {
            isValid = false
        }

        _customerState.update {
            it.copy(
                nameError = nameError,
                phoneError = phoneError,
                addressError = addressError
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
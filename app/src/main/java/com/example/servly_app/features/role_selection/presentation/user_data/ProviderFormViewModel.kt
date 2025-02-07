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

    val isLoading: Boolean = false,

    val isEditForm: Boolean = false,
    val isButtonEnabled: Boolean = true
) {
    fun toProviderInfo(): ProviderInfo {
        return ProviderInfo(
            name,
            phoneNumber,
            city,
            rangeInKm
        )
    }

    fun isEqualToCustomerInfo(providerInfo: ProviderInfo): Boolean {
        return name == providerInfo.name &&
                phoneNumber == providerInfo.phoneNumber &&
                city == providerInfo.city &&
                rangeInKm == providerInfo.rangeInKm
    }
}

@HiltViewModel
class ProviderFormViewModel @Inject constructor(
    private val providerFormUseCases: ProviderFormUseCases
): ViewModel() {
    private val _providerState = MutableStateFlow(ProviderState())
    val providerState: StateFlow<ProviderState> = _providerState.asStateFlow()

    private var providerInfoCopy: ProviderInfo? = null

    fun updateName(name: String) {
        _providerState.update { it.copy(name = name) }
        compareInputs()
    }

    fun updatePhoneNumber(phoneNumber: String) {
        _providerState.update { it.copy(phoneNumber = phoneNumber) }
        compareInputs()
    }

    fun updateCity(city: String) {
        _providerState.update { it.copy(city = city) }
        compareInputs()
    }

    fun updateRange(rangeInKm: Double) {
        Log.i("SLIDER RANGE", "${_providerState.value.rangeInKm} == ${providerInfoCopy?.rangeInKm}")
        _providerState.update { it.copy(rangeInKm = rangeInKm) }
        compareInputs()
    }

    fun setEditData(providerInfo: ProviderInfo) {
        _providerState.update {
            it.copy(
                name = providerInfo.name,
                phoneNumber = providerInfo.phoneNumber,
                city = providerInfo.city,
                rangeInKm = providerInfo.rangeInKm,
                isEditForm = true,
                isButtonEnabled = false
            )
        }
        providerInfoCopy = providerInfo
    }

    private fun compareInputs() {
        providerInfoCopy?.let { copy ->
            _providerState.update {
                it.copy(
                    isButtonEnabled = !it.isEqualToCustomerInfo(copy)
                )
            }
        }
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

    fun handleProvider(onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (_providerState.value.isEditForm) {
            updateProvider(onSuccess, onFailure)
        } else {
            createProvider(onSuccess, onFailure)
        }
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

    fun updateProvider(onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (!validateInputs()) return

        viewModelScope.launch {
            _providerState.update { it.copy(isLoading = true) }
            val result = providerFormUseCases.updateProvider(_providerState.value.toProviderInfo())

            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { onFailure() }
            )

            _providerState.update { it.copy(isLoading = false) }
        }
    }
}
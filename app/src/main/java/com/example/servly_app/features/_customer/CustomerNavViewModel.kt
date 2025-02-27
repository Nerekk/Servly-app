package com.example.servly_app.features._customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.domain.usecase.CustomerUseCases
import com.example.servly_app.features.role_selection.data.CustomerInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CustomerState(
    val customerId: Long? = null,
    val name: String = "",
    val phoneNumber: String = "",
    val city: String = "",
    val street: String = "",
    val houseNumber: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val rating: Double? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    fun toCustomerInfo(): CustomerInfo {
        return CustomerInfo(
            customerId,
            name,
            phoneNumber,
            city,
            street,
            houseNumber,
            latitude,
            longitude
        )
    }
}

@HiltViewModel
class CustomerNavViewModel @Inject constructor(
    private val customerUseCases: CustomerUseCases
): ViewModel() {
    private val _customerState = MutableStateFlow(CustomerState())
    val customerState = _customerState.asStateFlow()

    init {
        loadCustomer()
    }

    fun loadCustomer() {
        viewModelScope.launch {
            _customerState.update { it.copy(isLoading = true) }

            val result = customerUseCases.getCustomer()

            result.fold(
                onSuccess = { customer ->
                    _customerState.update { it.copy(
                        customerId = customer.customerId,
                        name = customer.name,
                        phoneNumber = customer.phoneNumber,
                        city = customer.city,
                        street = customer.street,
                        houseNumber = customer.houseNumber,
                        latitude = customer.latitude,
                        longitude = customer.longitude,
                        rating = customer.rating
                    ) }
                },
                onFailure = { e ->
                    _customerState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _customerState.update { it.copy(isLoading = false) }
        }
    }
}
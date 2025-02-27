package com.example.servly_app.features._customer.profile

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.domain.usecase.CustomerUseCases
import com.example.servly_app.features._customer.CustomerState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ProfileViewModel.ProviderViewModelFactory::class)
class ProfileViewModel @AssistedInject constructor(
    private val customerUseCases: CustomerUseCases,
    @Assisted val customerId: Long
): ViewModel() {
    @AssistedFactory
    interface ProviderViewModelFactory {
        fun create(customerId: Long): ProfileViewModel
    }

    private val _customerState = MutableStateFlow(CustomerState())
    val customerState = _customerState.asStateFlow()

    private val _reviewsVisibilityState = MutableStateFlow(false)
    val reviewsVisibilityState = _reviewsVisibilityState.asStateFlow()

    init {
        loadCustomer()
    }

    fun changeReviewsVisibility(isVisible: Boolean) {
        _reviewsVisibilityState.value = isVisible
    }

    private fun loadCustomer() {
        viewModelScope.launch {
            _customerState.update { it.copy(isLoading = true) }

            val result = customerUseCases.getCustomerById(customerId)

            result.fold(
                onSuccess = { customer ->
                    Log.i("CUSTOMER LOAD", "$customer")
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
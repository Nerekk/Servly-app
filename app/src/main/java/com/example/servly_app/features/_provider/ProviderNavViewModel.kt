package com.example.servly_app.features._provider

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.domain.usecase.ProviderUseCases
import com.example.servly_app.features.role_selection.data.ProviderInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProviderState(
    val name: String = "",

    val phoneNumber: String = "",

    val city: String = "",

    val rangeInKm: Double = 0.0,

    val latitude: Double? = null,

    val longitude: Double? = null,

    val isLoading: Boolean = false,
    val errorMessage: String? = null
) {
    fun toProviderInfo() : ProviderInfo {
        return ProviderInfo(name, phoneNumber, city, rangeInKm, latitude, longitude)
    }
}

@HiltViewModel
class ProviderNavViewModel @Inject constructor(
    private val providerUseCases: ProviderUseCases
): ViewModel() {
    private val _providerState = MutableStateFlow(ProviderState())
    val providerState = _providerState.asStateFlow()

    init {
        loadProvider()
    }

    fun loadProvider() {
        viewModelScope.launch {
            _providerState.update { it.copy(isLoading = true) }

            val result = providerUseCases.getProvider()

            result.fold(
                onSuccess = { provider ->
                    _providerState.update { it.copy(
                        name = provider.name,
                        phoneNumber = provider.phoneNumber,
                        city = provider.city,
                        rangeInKm = provider.rangeInKm,
                        latitude = provider.latitude,
                        longitude = provider.longitude
                    ) }
                },
                onFailure = { e ->
                    _providerState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _providerState.update { it.copy(isLoading = false) }
        }
    }
}
package com.example.servly_app.features._provider.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.domain.usecase.ProviderUseCases
import com.example.servly_app.features._provider.ProviderState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ProviderProfileViewModel.ProviderProfileViewModelFactory::class)
class ProviderProfileViewModel @AssistedInject constructor(
    private val providerUseCases: ProviderUseCases,
    @Assisted val providerId: Long
): ViewModel() {
    @AssistedFactory
    interface ProviderProfileViewModelFactory {
        fun create(providerId: Long): ProviderProfileViewModel
    }

    private val _providerState = MutableStateFlow(ProviderState())
    val providerState = _providerState.asStateFlow()

    private val _reviewsVisibilityState = MutableStateFlow(false)
    val reviewsVisibilityState = _reviewsVisibilityState.asStateFlow()

    init {
        loadProvider()
    }

    fun changeReviewsVisibility(isVisible: Boolean) {
        _reviewsVisibilityState.value = isVisible
    }

    private fun loadProvider() {
        viewModelScope.launch {
            _providerState.update { it.copy(isLoading = true) }

            val result = providerUseCases.getProviderById(providerId)

            result.fold(
                onSuccess = { provider ->
                    _providerState.update { it.copy(
                        providerId = provider.providerId,
                        name = provider.name,
                        phoneNumber = provider.phoneNumber,
                        city = provider.city,
                        rangeInKm = provider.rangeInKm,
                        latitude = provider.latitude,
                        longitude = provider.longitude,
                        rating = provider.rating,
                        aboutMe = provider.aboutMe
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
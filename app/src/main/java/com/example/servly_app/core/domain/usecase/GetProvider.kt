package com.example.servly_app.core.domain.usecase

import com.example.servly_app.core.domain.repository.ProviderRepository
import com.example.servly_app.features.role_selection.data.ProviderInfo

class GetProvider(private val providerRepository: ProviderRepository) {
    suspend operator fun invoke(): Result<ProviderInfo> {
        return providerRepository.getProvider()
    }
}
package com.example.servly_app.core.domain.usecase

import com.example.servly_app.core.domain.repository.ProviderRepository
import com.example.servly_app.features.role_selection.data.ProviderInfo

class GetProviderById(private val providerRepository: ProviderRepository) {
    suspend operator fun invoke(id: Long): Result<ProviderInfo> {
        return providerRepository.getProviderById(id)
    }
}
package com.example.servly_app.features.role_selection.domain.usecase

import com.example.servly_app.features.role_selection.data.ProviderInfo
import com.example.servly_app.features.role_selection.domain.repository.UserFormRepository

class CreateProvider(private val repository: UserFormRepository) {
    suspend operator fun invoke(providerInfo: ProviderInfo): Result<Unit> {
        return repository.createProvider(providerInfo)
    }
}
package com.example.servly_app.features.role_selection.domain.usecase

import com.example.servly_app.features.role_selection.data.CustomerInfo
import com.example.servly_app.features.role_selection.domain.repository.UserFormRepository

class CreateCustomer(private val repository: UserFormRepository) {
    suspend operator fun invoke(customerInfo: CustomerInfo): Result<Unit> {
        return repository.createCustomer(customerInfo)
    }
}
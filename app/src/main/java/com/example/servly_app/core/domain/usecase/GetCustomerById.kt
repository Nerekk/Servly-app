package com.example.servly_app.core.domain.usecase

import com.example.servly_app.core.domain.repository.CustomerRepository
import com.example.servly_app.features.role_selection.data.CustomerInfo

class GetCustomerById(private val customerRepository: CustomerRepository) {
    suspend operator fun invoke(id: Long): Result<CustomerInfo> {
        return customerRepository.getCustomerById(id)
    }
}
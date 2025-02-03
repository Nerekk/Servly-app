package com.example.servly_app.core.domain.usecase

import com.example.servly_app.core.domain.repository.CustomerRepository
import com.example.servly_app.features.role_selection.data.CustomerInfo

class GetCustomer(private val customerRepository: CustomerRepository) {
    suspend operator fun invoke(): Result<CustomerInfo> {
        return customerRepository.getCustomer()
    }
}
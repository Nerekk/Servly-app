package com.example.servly_app.core.domain.repository

import com.example.servly_app.core.data.RoleService
import com.example.servly_app.features.role_selection.data.CustomerInfo

class CustomerRepository(private val roleService: RoleService) {
    suspend fun getCustomer(): Result<CustomerInfo> {
        return try {
            val response = roleService.getCustomer()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error fetching customer info: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
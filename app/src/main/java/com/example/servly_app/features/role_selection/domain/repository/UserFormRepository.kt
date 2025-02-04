package com.example.servly_app.features.role_selection.domain.repository

import com.example.servly_app.core.data.RoleService
import com.example.servly_app.features.role_selection.data.CustomerInfo
import com.example.servly_app.features.role_selection.data.ProviderInfo

class UserFormRepository(private val service: RoleService) {
    suspend fun createCustomer(customerInfo: CustomerInfo): Result<Unit> {
        return try {
            val response = service.createCustomer(customerInfo)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to create customer: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateCustomer(customerInfo: CustomerInfo): Result<Unit> {
        return try {
            val response = service.updateCustomer(customerInfo)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update customer: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProvider(providerInfo: ProviderInfo): Result<Unit> {
        return try {
            val response = service.createProvider(providerInfo)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to create provider: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProvider(providerInfo: ProviderInfo): Result<Unit> {
        return try {
            val response = service.updateProvider(providerInfo)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to update provider: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
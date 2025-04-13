package com.example.servly_app.core.domain.repository

import com.example.servly_app.core.data.RoleService
import com.example.servly_app.core.util.ErrorStore
import com.example.servly_app.features.role_selection.data.ProviderInfo

class ProviderRepository(private val roleService: RoleService) {
    suspend fun getProvider(): Result<ProviderInfo> {
        return try {
            val response = roleService.getProvider()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error fetching provider info: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun getProviderById(id: Long): Result<ProviderInfo> {
        return try {
            val response = roleService.getProviderById(id)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error fetching provider info: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }
}
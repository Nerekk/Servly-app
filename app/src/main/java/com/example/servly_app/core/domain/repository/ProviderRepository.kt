package com.example.servly_app.core.domain.repository

import com.example.servly_app.core.data.RoleService
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
            Result.failure(e)
        }
    }
}
package com.example.servly_app.core.domain.repository

import com.example.servly_app.core.data.ApiService
import com.example.servly_app.core.data.util.Role

class RoleRepository(private val apiService: ApiService) {
    suspend fun getUserRoles(): Result<Role> {
        return try {
            val response = apiService.getUserRoles()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error fetching user roles: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }

    }
}
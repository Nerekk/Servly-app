package com.example.servly_app.core.domain.repository

import android.util.Log
import com.example.servly_app.core.data.ApiService
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.util.ErrorStore
import com.google.firebase.messaging.FirebaseMessaging

class RoleRepository(private val apiService: ApiService) {
    suspend fun getUserRoles(fcmToken: String): Result<Role> {
        return try {
            val response = apiService.getUserRoles(fcmToken)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error fetching user roles: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }

    }
}

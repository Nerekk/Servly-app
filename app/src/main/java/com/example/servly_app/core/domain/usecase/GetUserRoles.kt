package com.example.servly_app.core.domain.usecase

import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.domain.repository.RoleRepository


class GetUserRoles(private val repository: RoleRepository) {
    suspend operator fun invoke(fcmToken: String): Result<Role> {
        return repository.getUserRoles(fcmToken)
    }
}
package com.example.servly_app.features.authentication.domain.usecase

import com.example.servly_app.features.authentication.data.AuthRepository

class Logout(private val authRepository: AuthRepository) {
    operator fun invoke() {
        authRepository.logout()
    }
}
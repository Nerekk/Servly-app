package com.example.servly_app.features.authentication.domain.usecase

import com.example.servly_app.features.authentication.data.AuthRepository

class CheckUserLoggedIn(private val authRepository: AuthRepository) {
    operator fun invoke(): Boolean {
        return authRepository.isUserLoggedIn()
    }
}
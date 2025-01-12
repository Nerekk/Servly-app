package com.example.servly_app.features.authentication.domain.usecase

import com.example.servly_app.features.authentication.data.AuthRepository

class VerifyCode(private val authRepository: AuthRepository) {
    suspend operator fun invoke(verificationId: String, code: String): Result<Unit> {
        return authRepository.verifyCode(verificationId, code)
    }
}
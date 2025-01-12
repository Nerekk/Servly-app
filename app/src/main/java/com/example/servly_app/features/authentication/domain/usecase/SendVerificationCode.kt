package com.example.servly_app.features.authentication.domain.usecase

import android.app.Activity
import com.example.servly_app.features.authentication.data.AuthRepository

class SendVerificationCode(private val authRepository: AuthRepository) {
    suspend operator fun invoke(
        activity: Activity,
        phoneNumber: String,
        onCodeSent: (String) -> Unit
    ): Result<Unit> {
        return authRepository.sendVerificationCode(activity, phoneNumber, onCodeSent)
    }
}
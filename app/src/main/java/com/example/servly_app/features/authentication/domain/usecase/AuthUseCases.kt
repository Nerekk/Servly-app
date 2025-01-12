package com.example.servly_app.features.authentication.domain.usecase

data class AuthUseCases(
    val checkUserLoggedIn: CheckUserLoggedIn,
    val sendVerificationCode: SendVerificationCode,
    val verifyCode: VerifyCode,
    val logout: Logout
)
package com.example.servly_app.features.authentication.domain.usecase

data class AuthUseCases(
    val checkUserLoggedIn: CheckUserLoggedIn,
    val signInWithEmail: SignInWithEmail,
    val signUpWithEmail: SignUpWithEmail,
    val signInWithGoogle: SignInWithGoogle,
    val logout: Logout
)
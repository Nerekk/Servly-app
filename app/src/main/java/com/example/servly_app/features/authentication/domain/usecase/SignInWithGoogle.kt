package com.example.servly_app.features.authentication.domain.usecase

import com.example.servly_app.features.authentication.data.AuthRepository
import com.example.servly_app.features.authentication.domain.User

class SignInWithGoogle(private val authRepository: AuthRepository) {
    suspend operator fun invoke(idToken: String): Result<User> {
        return authRepository.signInWithGoogle(idToken).mapCatching { firebaseUser ->
            firebaseUser?.let { User(it.uid) } ?: throw Exception("User not found")
        }
    }
}
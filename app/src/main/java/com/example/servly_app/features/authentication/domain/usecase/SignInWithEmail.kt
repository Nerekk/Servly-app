package com.example.servly_app.features.authentication.domain.usecase

import com.example.servly_app.features.authentication.data.AuthRepository
import com.example.servly_app.features.authentication.domain.User

class SignInWithEmail(private val authRepository: AuthRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return authRepository.signInWithEmail(email, password).mapCatching { firebaseUser ->
            firebaseUser?.let { User(it.uid) } ?: throw Exception("User not found")
        }
    }
}
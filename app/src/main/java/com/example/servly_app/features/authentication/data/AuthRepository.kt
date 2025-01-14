package com.example.servly_app.features.authentication.data

import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    suspend fun signInWithEmail(email: String, password: String): Result<FirebaseUser?>
    suspend fun signUpWithEmail(email: String, password: String): Result<FirebaseUser?>
    suspend fun signInWithGoogle(idToken: String): Result<FirebaseUser?>
    fun isUserLoggedIn(): Boolean
    fun logout()
}
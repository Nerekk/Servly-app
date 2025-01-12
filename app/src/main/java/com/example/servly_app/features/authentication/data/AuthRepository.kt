package com.example.servly_app.features.authentication.data

import android.app.Activity

interface AuthRepository {
    suspend fun sendVerificationCode(activity: Activity, phoneNumber: String, onCodeSent: (String) -> Unit): Result<Unit>
    suspend fun verifyCode(verificationId: String, code: String): Result<Unit>
    fun isUserLoggedIn(): Boolean
    fun logout()
}
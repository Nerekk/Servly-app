package com.example.servly_app.features.authentication.domain.repository

import android.app.Activity
import com.example.servly_app.features.authentication.data.AuthRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import kotlinx.coroutines.tasks.await
import java.util.concurrent.TimeUnit

class AuthRepositoryImpl(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun sendVerificationCode(
        activity: Activity,
        phoneNumber: String,
        onCodeSent: (String) -> Unit
    ): Result<Unit> {
        return try {
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    // Logika automatycznego logowania
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    throw e
                }

                override fun onCodeSent(verificationId: String, token: PhoneAuthProvider.ForceResendingToken) {
                    onCodeSent(verificationId)
                }
            }

            val options = PhoneAuthOptions.newBuilder(firebaseAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(activity)
                .setCallbacks(callbacks)
                .build()

            PhoneAuthProvider.verifyPhoneNumber(options)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyCode(verificationId: String, code: String): Result<Unit> {
        return try {
            val credential = PhoneAuthProvider.getCredential(verificationId, code)
            firebaseAuth.signInWithCredential(credential).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}
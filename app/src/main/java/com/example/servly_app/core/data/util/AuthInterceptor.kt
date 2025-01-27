package com.example.servly_app.core.data.util

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GetTokenResult
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        Log.i("RETROFIT", "Intercept")
        val tokenTask: Task<GetTokenResult> = FirebaseAuth.getInstance().currentUser?.getIdToken(false)
            ?: throw IllegalStateException("Firebase user is null")

        val token = try {
            val result = Tasks.await(tokenTask)
            result.token
        } catch (e: Exception) {
            throw IllegalStateException("Failed to retrieve token", e)
        }

        if (token.isNullOrEmpty()) {
            throw IllegalStateException("Token is null or empty")
        }

        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(newRequest)
    }
}
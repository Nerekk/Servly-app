package com.example.servly_app.core.data

import com.example.servly_app.BuildConfig
import com.example.servly_app.core.data.util.AuthInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private fun createRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> createService(apiServiceClass: Class<T>): T {
        return createRetrofit().create(apiServiceClass)
    }
}
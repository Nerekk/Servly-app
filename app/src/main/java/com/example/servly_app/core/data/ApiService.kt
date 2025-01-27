package com.example.servly_app.core.data

import com.example.servly_app.core.data.util.Role
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("api/users")
    suspend fun getUserRoles(): Response<Role>
}
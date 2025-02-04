package com.example.servly_app.core.data

import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.data.util.ControllerMappings
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("api/" + ControllerMappings.USER)
    suspend fun getUserRoles(): Response<Role>
}
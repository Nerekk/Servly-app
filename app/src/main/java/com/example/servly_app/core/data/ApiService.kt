package com.example.servly_app.core.data

import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.data.util.ControllerMappings
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/" + ControllerMappings.USER)
    suspend fun getUserRoles(@Query("fcmToken") fcmToken: String): Response<Role>
}
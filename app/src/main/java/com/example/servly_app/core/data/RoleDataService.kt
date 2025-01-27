package com.example.servly_app.core.data

import com.example.servly_app.features.role_selection.data.CustomerInfo
import com.example.servly_app.features.role_selection.data.ProviderInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface RoleDataService {
    @GET("api/customers")
    suspend fun getCustomer(): Response<CustomerInfo>

    @POST("api/customers")
    suspend fun createCustomer(@Body customerInfo: CustomerInfo): Response<Unit>

    @PUT("api/customers")
    suspend fun updateCustomer(@Body customerInfo: CustomerInfo): Response<Unit>


    @GET("api/providers")
    suspend fun getProvider(): Response<ProviderInfo>

    @POST("api/providers")
    suspend fun createProvider(@Body providerInfo: ProviderInfo): Response<Unit>

    @PUT("api/providers")
    suspend fun updateProvider(@Body providerInfo: ProviderInfo): Response<Unit>
}
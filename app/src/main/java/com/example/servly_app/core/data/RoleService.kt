package com.example.servly_app.core.data

import com.example.servly_app.core.data.util.ControllerMappings
import com.example.servly_app.features.role_selection.data.CustomerInfo
import com.example.servly_app.features.role_selection.data.ProviderInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface RoleService {
    @GET("api/" + ControllerMappings.CUSTOMER)
    suspend fun getCustomer(): Response<CustomerInfo>

    @POST("api/" + ControllerMappings.CUSTOMER)
    suspend fun createCustomer(@Body customerInfo: CustomerInfo): Response<Unit>

    @PUT("api/" + ControllerMappings.CUSTOMER)
    suspend fun updateCustomer(@Body customerInfo: CustomerInfo): Response<Unit>


    @GET("api/" + ControllerMappings.PROVIDER)
    suspend fun getProvider(): Response<ProviderInfo>

    @POST("api/" + ControllerMappings.PROVIDER)
    suspend fun createProvider(@Body providerInfo: ProviderInfo): Response<Unit>

    @PUT("api/" + ControllerMappings.PROVIDER)
    suspend fun updateProvider(@Body providerInfo: ProviderInfo): Response<Unit>
}
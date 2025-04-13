package com.example.servly_app.core.data

import com.example.servly_app.core.data.util.ControllerMappings
import com.example.servly_app.features.role_selection.data.CustomerInfo
import com.example.servly_app.features.role_selection.data.ProviderInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface RoleService {
    @GET("api/" + ControllerMappings.CUSTOMER)
    suspend fun getCustomer(): Response<CustomerInfo>

    @GET("api/" + ControllerMappings.CUSTOMER + "/{id}")
    suspend fun getCustomerById(@Path("id") id: Long): Response<CustomerInfo>

    @POST("api/" + ControllerMappings.CUSTOMER)
    suspend fun createCustomer(@Body customerInfo: CustomerInfo): Response<Unit>

    @PUT("api/" + ControllerMappings.CUSTOMER)
    suspend fun updateCustomer(@Body customerInfo: CustomerInfo): Response<Unit>


    @GET("api/" + ControllerMappings.PROVIDER)
    suspend fun getProvider(): Response<ProviderInfo>

    @GET("api/" + ControllerMappings.PROVIDER + "/{id}")
    suspend fun getProviderById(@Path("id") id: Long): Response<ProviderInfo>

    @POST("api/" + ControllerMappings.PROVIDER)
    suspend fun createProvider(@Body providerInfo: ProviderInfo): Response<Unit>

    @PUT("api/" + ControllerMappings.PROVIDER)
    suspend fun updateProvider(@Body providerInfo: ProviderInfo): Response<Unit>
}
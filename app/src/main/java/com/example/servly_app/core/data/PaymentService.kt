package com.example.servly_app.core.data

import com.example.servly_app.features.payments.data.JobPaymentInfo
import com.example.servly_app.features.payments.data.StripePaymentRequest
import com.example.servly_app.features.payments.data.StripePaymentResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PaymentService {
    @POST("/api/stripe/intent")
    suspend fun createIntent(@Body stripePaymentRequest: StripePaymentRequest): Response<StripePaymentResponse>

    @POST("/api/payments")
    suspend fun createPayment(@Body jobPaymentInfo: JobPaymentInfo): Response<JobPaymentInfo>

    @PUT("/api/payments")
    suspend fun updatePayment(@Body jobPaymentInfo: JobPaymentInfo): Response<JobPaymentInfo>

    @GET("/api/payments/{paymentId}")
    suspend fun getPayment(@Path("paymentId") paymentId: Long): Response<JobPaymentInfo>
}
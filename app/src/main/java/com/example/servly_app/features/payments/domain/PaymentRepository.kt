package com.example.servly_app.features.payments.domain

import android.util.Log
import com.example.servly_app.core.data.PaymentService
import com.example.servly_app.core.util.ErrorStore
import com.example.servly_app.features.payments.data.JobPaymentInfo
import com.example.servly_app.features.payments.data.StripePaymentRequest
import com.example.servly_app.features.payments.data.StripePaymentResponse

class PaymentRepository(private val paymentService: PaymentService) {
    suspend fun createIntent(stripePaymentRequest: StripePaymentRequest): Result<StripePaymentResponse> {
        return try {
            val response = paymentService.createIntent(stripePaymentRequest)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error intent: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun createPayment(jobPaymentInfo: JobPaymentInfo): Result<JobPaymentInfo> {
        Log.d("CREATE_PAYMENT", "$jobPaymentInfo")
        return try {
            val response = paymentService.createPayment(jobPaymentInfo)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error create: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun updatePayment(jobPaymentInfo: JobPaymentInfo): Result<JobPaymentInfo> {
        return try {
            val response = paymentService.updatePayment(jobPaymentInfo)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error update: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun getPayment(paymentId: Long): Result<JobPaymentInfo> {
        return try {
            val response = paymentService.getPayment(paymentId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error get: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }
}
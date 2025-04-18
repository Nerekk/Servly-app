package com.example.servly_app.features.payments.domain

import com.example.servly_app.features.payments.data.StripePaymentRequest
import com.example.servly_app.features.payments.data.StripePaymentResponse

class CreateIntent(private val paymentRepository: PaymentRepository) {
    suspend operator fun invoke(stripePaymentRequest: StripePaymentRequest): Result<StripePaymentResponse> {
        return paymentRepository.createIntent(stripePaymentRequest)
    }
}
package com.example.servly_app.features.payments.domain

import com.example.servly_app.features.payments.data.JobPaymentInfo

class GetPayment(private val paymentRepository: PaymentRepository) {
    suspend operator fun invoke(paymentId: Long): Result<JobPaymentInfo> {
        return paymentRepository.getPayment(paymentId)
    }
}
package com.example.servly_app.features.payments.domain

import com.example.servly_app.features.payments.data.JobPaymentInfo

class CreatePayment(private val paymentRepository: PaymentRepository) {
    suspend operator fun invoke(jobPaymentInfo: JobPaymentInfo): Result<JobPaymentInfo> {
        return paymentRepository.createPayment(jobPaymentInfo)
    }
}
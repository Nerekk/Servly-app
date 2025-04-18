package com.example.servly_app.features.payments.data

data class JobPaymentInfo(
    val id: Long? = null,
    val jobRequestId: Long,
    val totalAmount: Long,
    val depositAmount: Long? = null,
    val stripePaymentId: String? = null,
    val stripeDepositPaymentId: String? = null,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING
)

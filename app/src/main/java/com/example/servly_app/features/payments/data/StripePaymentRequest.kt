package com.example.servly_app.features.payments.data

data class StripePaymentRequest(
    val jobRequestId: Long,
    val amount: Long
)

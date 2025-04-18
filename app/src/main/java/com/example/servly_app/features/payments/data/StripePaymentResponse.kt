package com.example.servly_app.features.payments.data

data class StripePaymentResponse(
    val clientSecret: String,
    val paymentIntentId: String
)

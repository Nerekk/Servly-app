package com.example.servly_app.features.payments.domain

data class PaymentUseCases(
    val createIntent: CreateIntent,
    val createPayment: CreatePayment,
    val updatePayment: UpdatePayment,
    val getPayment: GetPayment
)

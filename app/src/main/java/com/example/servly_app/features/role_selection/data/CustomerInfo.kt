package com.example.servly_app.features.role_selection.data

data class CustomerInfo(
    val customerId: Long? = null,

    val name: String,

    val phoneNumber: String,

    val address: String? = null,

    val latitude: Double? = null,

    val longitude: Double? = null,

    val rating: Double? = null
)

package com.example.servly_app.features.role_selection.data

data class ProviderInfo(
    val providerId: Long? = null,

    val name: String,

    val phoneNumber: String,

    val address: String? = null,

    val rangeInKm: Double,

    val latitude: Double? = null,

    val longitude: Double? = null,

    val rating: Double? = null,

    val aboutMe: String = ""
)

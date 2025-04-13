package com.example.servly_app.features.reviews.data

data class ReviewFormData(
    val isLoading: Boolean = false,
    val rating: Int = 0,
    val review: String = "",
    val isButtonEnabled: Boolean = false
)

package com.example.servly_app.core.domain.usecase.reviews

data class ReviewUseCases(
    val createCustomerReview: CreateCustomerReview,
    val createProviderReview: CreateProviderReview,
    val getCustomerReviews: GetCustomerReviews,
    val getProviderReviews: GetProviderReviews
)

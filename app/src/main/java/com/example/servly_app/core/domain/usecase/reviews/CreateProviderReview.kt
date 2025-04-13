package com.example.servly_app.core.domain.usecase.reviews

import com.example.servly_app.core.domain.repository.ReviewRepository
import com.example.servly_app.features.reviews.data.ReviewInfo

class CreateProviderReview(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(reviewInfo: ReviewInfo): Result<ReviewInfo> {
        return reviewRepository.createProviderReview(reviewInfo)
    }
}
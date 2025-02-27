package com.example.servly_app.core.domain.usecase.reviews

import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.core.domain.repository.ReviewRepository
import com.example.servly_app.features.reviews.data.ReviewInfo

class GetProviderReviews(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(
        providerId: Long,
        page: Int,
        size: Int,
        sortType: SortType = SortType.DESCENDING
    ): Result<PagedResponse<ReviewInfo>> {
        return reviewRepository.getProviderReviews(providerId, page, size, sortType)
    }
}
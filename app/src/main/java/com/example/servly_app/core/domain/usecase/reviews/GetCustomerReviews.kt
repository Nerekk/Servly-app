package com.example.servly_app.core.domain.usecase.reviews

import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.core.domain.repository.ReviewRepository
import com.example.servly_app.features.reviews.data.ReviewInfo

class GetCustomerReviews(private val reviewRepository: ReviewRepository) {
    suspend operator fun invoke(
        customerId: Long,
        page: Int,
        size: Int,
        sortType: SortType = SortType.DESCENDING
    ): Result<PagedResponse<ReviewInfo>> {
        return reviewRepository.getCustomerReviews(customerId, page, size, sortType)
    }
}
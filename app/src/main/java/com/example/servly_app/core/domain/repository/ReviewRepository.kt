package com.example.servly_app.core.domain.repository

import com.example.servly_app.core.data.ReviewService
import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.features.reviews.data.ReviewInfo

class ReviewRepository(private val reviewService: ReviewService) {
    suspend fun createCustomerReview(reviewInfo: ReviewInfo): Result<ReviewInfo> {
        return try {
            val response = reviewService.createCustomerReview(reviewInfo)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createProviderReview(reviewInfo: ReviewInfo): Result<ReviewInfo> {
        return try {
            val response = reviewService.createProviderReview(reviewInfo)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCustomerReviews(
        customerId: Long,
        page: Int,
        size: Int,
        sortType: SortType = SortType.DESCENDING
    ): Result<PagedResponse<ReviewInfo>> {
        return try {
            val response = reviewService.getCustomerReviews(customerId, sortType, page, size)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProviderReviews(
        providerId: Long,
        page: Int,
        size: Int,
        sortType: SortType = SortType.DESCENDING
    ): Result<PagedResponse<ReviewInfo>> {
        return try {
            val response = reviewService.getProviderReviews(providerId, sortType, page, size)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
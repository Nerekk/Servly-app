package com.example.servly_app.core.data

import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.features.reviews.data.ReviewInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewService {
    @POST("api/review/customer")
    suspend fun createCustomerReview(@Body reviewInfo: ReviewInfo): Response<ReviewInfo>

    @POST("api/review/provider")
    suspend fun createProviderReview(@Body reviewInfo: ReviewInfo): Response<ReviewInfo>


    @GET("api/review/customer/list/{customerId}")
    suspend fun getCustomerReviews(
        @Path("customerId") customerId: Long,
        @Query("sortType") sortType: SortType,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<PagedResponse<ReviewInfo>>


    @GET("api/review/provider/list/{providerId}")
    suspend fun getProviderReviews(
        @Path("providerId") providerId: Long,
        @Query("sortType") sortType: SortType,
        @Query("page") page: Int,
        @Query("size") size: Int
    ): Response<PagedResponse<ReviewInfo>>


}
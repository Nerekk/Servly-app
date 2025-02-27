package com.example.servly_app.core.data

import com.example.servly_app.core.data.util.ControllerMappings
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface JobPostingService {
    @POST("api/" + ControllerMappings.JOB_POSTING)
    suspend fun createJobPosting(@Body jobPostingInfo: JobPostingInfo): Response<JobPostingInfo>

    @GET("api/${ControllerMappings.JOB_POSTING}/user")
    suspend fun getJobPostings(
        @Query("statuses") statuses: List<String>? = null,
        @Query("sortType") sortType: SortType = SortType.DESCENDING,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<PagedResponse<JobPostingInfo>>

    @GET("api/${ControllerMappings.JOB_POSTING}/user/ended")
    suspend fun getJobPostingsEnded(
        @Query("sortType") sortType: SortType = SortType.DESCENDING,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<PagedResponse<JobPostingInfo>>

    @GET("api/${ControllerMappings.JOB_POSTING}/global")
    suspend fun getActiveJobPostings(
        @Query("sortType") sortType: SortType = SortType.DESCENDING,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("search") search: String? = null,
        @Query("categories") categories: List<Long>? = null,
        @Query("days") days: Long? = null
    ): Response<PagedResponse<JobPostingInfo>>

    @PUT("api/${ControllerMappings.JOB_POSTING}/{jobPostingId}/status")
    suspend fun updateJobStatus(
        @Path("jobPostingId") jobPostingId: Long,
        @Query("status") status: JobStatus
    ): Response<JobPostingInfo>
}
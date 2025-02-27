package com.example.servly_app.core.data

import com.example.servly_app.core.data.util.ControllerMappings
import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.features.job_details.data.JobRequestInfo
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface JobRequestService {
    @POST("api/" + ControllerMappings.JOB_REQUEST)
    suspend fun createJobRequest(@Query("jobPostingId") jobPostingId: Long): Response<JobRequestInfo>

    @GET("api/${ControllerMappings.JOB_REQUEST}/user")
    suspend fun getJobRequests(
        @Query("jobRequestStatuses") jobRequestStatuses: List<String>,
        @Query("sortType") sortType: SortType = SortType.DESCENDING,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10
    ): Response<PagedResponse<JobRequestInfo>>

    @GET("api/${ControllerMappings.JOB_REQUEST}/posting/{jobPostingId}")
    suspend fun getJobRequestsPosting(
        @Path("jobPostingId") jobPostingId: Long,
    ): Response<List<JobRequestInfo>>

    @PUT("api/${ControllerMappings.JOB_REQUEST}")
    suspend fun updateJobRequestStatus(
        @Query("jobRequestId") jobRequestId: Long,
        @Query("jobRequestStatus") jobRequestStatus: JobRequestStatus
    ): Response<JobRequestInfo>


    @GET("api/${ControllerMappings.JOB_REQUEST}/provider/{jobPostingId}")
    suspend fun getJobRequestForProvider(
        @Path("jobPostingId") jobPostingId: Long,
        @Query("providerId") providerId: Long
    ): Response<JobRequestInfo>

    @GET("api/${ControllerMappings.JOB_REQUEST}/customer/{jobPostingId}")
    suspend fun getJobRequestSelectedForCustomer(
        @Path("jobPostingId") jobPostingId: Long
    ): Response<JobRequestInfo>
}
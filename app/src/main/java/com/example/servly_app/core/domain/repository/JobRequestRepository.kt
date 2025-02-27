package com.example.servly_app.core.domain.repository

import com.example.servly_app.core.data.JobRequestService
import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.features.job_details.data.JobRequestInfo

class JobRequestRepository(private val jobRequestService: JobRequestService) {
    suspend fun createJobRequest(jobPostingId: Long): Result<JobRequestInfo> {
        return try {
            val response = jobRequestService.createJobRequest(jobPostingId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error create job request: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJobRequests(
        statuses: List<JobRequestStatus>,
        sortType: SortType,
        page: Int,
        size: Int
    ): Result<PagedResponse<JobRequestInfo>> {
        return try {
            val statusStrings = statuses.map { it.name }
            val response = jobRequestService.getJobRequests(statusStrings, sortType, page, size)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error fetching job requests: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJobRequestsPosting(jobPostingId: Long): Result<List<JobRequestInfo>> {
        return try {
            val response = jobRequestService.getJobRequestsPosting(jobPostingId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error fetching job requests: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateJobRequestStatus(
        jobRequestId: Long,
        jobRequestStatus: JobRequestStatus
    ): Result<JobRequestInfo> {
        return try {
            val response = jobRequestService.updateJobRequestStatus(jobRequestId, jobRequestStatus)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error updating request status: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJobRequestForProvider(
        jobRequestId: Long,
        providerId: Long
    ): Result<JobRequestInfo?> {
        return try {
            val response = jobRequestService.getJobRequestForProvider(jobRequestId, providerId)

            if (response.isSuccessful || response.code() == 404) {
                Result.success(response.body())
            } else {
                throw Exception("Error updating request status: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getJobRequestSelectedForCustomer(
        jobRequestId: Long
    ): Result<JobRequestInfo?> {
        return try {
            val response = jobRequestService.getJobRequestSelectedForCustomer(jobRequestId)

            if (response.isSuccessful || response.code() == 404) {
                Result.success(response.body())
            } else {
                throw Exception("Error updating request status: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
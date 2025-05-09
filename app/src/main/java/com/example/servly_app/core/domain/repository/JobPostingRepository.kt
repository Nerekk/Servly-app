package com.example.servly_app.core.domain.repository

import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.core.data.JobPostingService
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.core.util.ErrorStore

class JobPostingRepository(private val jobPostingService: JobPostingService) {
    suspend fun createJobPosting(jobPostingInfo: JobPostingInfo): Result<JobPostingInfo> {
        return try {
            val response = jobPostingService.createJobPosting(jobPostingInfo)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error create job: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun getJobPosting(jobPostingId: Long): Result<JobPostingInfo> {
        return try {
            val response = jobPostingService.getJobPosting(jobPostingId)

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error fetching jobs: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun getJobPostings(statuses: List<JobStatus>? = null, sortType: SortType, page: Int, size: Int): Result<PagedResponse<JobPostingInfo>> {
        return try {
            val response = if (statuses != null) {
                val stringStatuses = statuses.map { it.toString() }
                jobPostingService.getJobPostings(stringStatuses, sortType, page, size)
            } else {
                jobPostingService.getJobPostingsEnded(sortType, page, size)
            }

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error fetching jobs: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun updateJobStatus(jobPostingId: Long, status: JobStatus): Result<JobPostingInfo> {
        return try {
            val response = jobPostingService.updateJobStatus(jobPostingId, status)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error update job status: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }
}
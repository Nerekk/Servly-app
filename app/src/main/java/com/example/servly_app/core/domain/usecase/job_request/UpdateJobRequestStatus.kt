package com.example.servly_app.core.domain.usecase.job_request

import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.core.domain.repository.JobRequestRepository
import com.example.servly_app.features.job_details.data.JobRequestInfo

class UpdateJobRequestStatus(private val jobRequestRepository: JobRequestRepository) {
    suspend operator fun invoke(
        jobRequestId: Long,
        jobRequestStatus: JobRequestStatus
    ): Result<JobRequestInfo> {
        return jobRequestRepository.updateJobRequestStatus(jobRequestId, jobRequestStatus)
    }
}
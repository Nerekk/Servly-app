package com.example.servly_app.core.domain.usecase.job_request

import com.example.servly_app.core.domain.repository.JobRequestRepository
import com.example.servly_app.features.job_details.data.JobRequestInfo

class CreateJobRequest(private val jobRequestRepository: JobRequestRepository) {
    suspend operator fun invoke(jobPostingId: Long): Result<JobRequestInfo> {
        return jobRequestRepository.createJobRequest(jobPostingId)
    }
}
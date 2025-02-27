package com.example.servly_app.core.domain.usecase.job_posting

import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.domain.repository.JobPostingRepository
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo

class UpdateJobStatus(private val jobPostingRepository: JobPostingRepository) {
    suspend operator fun invoke(jobPostingId: Long, status: JobStatus): Result<JobPostingInfo> {
        return jobPostingRepository.updateJobStatus(jobPostingId, status)
    }
}
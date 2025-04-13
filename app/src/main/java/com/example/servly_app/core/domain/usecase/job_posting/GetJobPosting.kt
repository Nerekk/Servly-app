package com.example.servly_app.core.domain.usecase.job_posting

import com.example.servly_app.core.domain.repository.JobPostingRepository
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo

class GetJobPosting(private val jobPostingRepository: JobPostingRepository) {
    suspend operator fun invoke(jobPostingId: Long): Result<JobPostingInfo> {
        return jobPostingRepository.getJobPosting(jobPostingId)
    }
}
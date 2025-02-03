package com.example.servly_app.features._customer.job_create.domain.usecase

import com.example.servly_app.core.domain.repository.JobPostingRepository
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo

class CreateJobPosting(private val jobPostingRepository: JobPostingRepository) {
    suspend operator fun invoke(jobPostingInfo: JobPostingInfo): Result<JobPostingInfo> {
        return jobPostingRepository.createJobPosting(jobPostingInfo)
    }
}
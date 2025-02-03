package com.example.servly_app.features._customer.job_list.domain.usecase

import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.core.domain.repository.JobPostingRepository
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo

class GetUserJobPostings(private val jobPostingRepository: JobPostingRepository) {
    suspend operator fun invoke(
        status: JobStatus? = null,
        sortType: SortType,
        page: Int, size: Int): Result<PagedResponse<JobPostingInfo>> {

        return jobPostingRepository.getJobPostings(status, sortType, page, size)
    }
}
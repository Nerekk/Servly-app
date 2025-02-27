package com.example.servly_app.core.domain.usecase.job_request

import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.core.domain.repository.JobRequestRepository
import com.example.servly_app.features.job_details.data.JobRequestInfo

class GetJobRequests(private val jobRequestRepository: JobRequestRepository) {
    suspend operator fun invoke(
        statuses: List<JobRequestStatus>,
        sortType: SortType,
        page: Int,
        size: Int
    ): Result<PagedResponse<JobRequestInfo>> {
        return jobRequestRepository.getJobRequests(statuses, sortType, page, size)
    }
}
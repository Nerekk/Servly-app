package com.example.servly_app.features._provider.job_list.domain.usecase

import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.features._provider.job_list.domain.repository.ProviderJobListRepository

class GetFilteredActiveJobs(
    private val provderJobListRepository: ProviderJobListRepository
) {
    suspend operator fun invoke(
        sortType: SortType = SortType.DESCENDING,
        page: Int,
        size: Int,
        search: String? = null,
        categories: List<Long>? = null,
        days: Long? = null
    ): Result<PagedResponse<JobPostingInfo>> {
        return provderJobListRepository.getFilteredActiveJobPostings(sortType, page, size, search, categories, days)
    }
}
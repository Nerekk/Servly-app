package com.example.servly_app.features._provider.job_list.domain.repository

import com.example.servly_app.core.data.JobPostingService
import com.example.servly_app.core.data.util.PagedResponse
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.core.util.ErrorStore
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo

class ProviderJobListRepository(
    private val jobPostingService: JobPostingService
) {
    suspend fun getFilteredActiveJobPostings(
        sortType: SortType = SortType.DESCENDING,
        page: Int,
        size: Int,
        search: String? = null,
        categories: List<Long>? = null,
        days: Long? = null,
        latitude: Double? = null,
        longitude: Double? = null,
        distanceInKm: Double? = null
    ): Result<PagedResponse<JobPostingInfo>> {
        return try {
            val response = jobPostingService.getActiveJobPostings(sortType, page, size, search, categories, days, latitude, longitude, distanceInKm)

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
}
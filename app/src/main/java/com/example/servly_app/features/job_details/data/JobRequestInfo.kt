package com.example.servly_app.features.job_details.data

import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import com.example.servly_app.features.reviews.data.ReviewInfo
import com.example.servly_app.features.role_selection.data.ProviderInfo

data class JobRequestInfo(
    val id: Long? = null,
    val jobPostingInfo: JobPostingInfo? = null,
    val provider: ProviderInfo? = null,
    val schedule: ScheduleInfo? = null,
    val customerReview: ReviewInfo? = null,
    val providerReview: ReviewInfo? = null,
    val jobRequestStatus: JobRequestStatus = JobRequestStatus.ACTIVE
)

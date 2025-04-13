package com.example.servly_app.core.domain.usecase.job_request

data class JobRequestUseCases(
    val createJobRequest: CreateJobRequest,
    val getJobRequests: GetJobRequests,
    val getJobRequestsPosting: GetJobRequestsPosting,
    val updateJobRequestStatus: UpdateJobRequestStatus,
    val getJobRequestForProvider: GetJobRequestForProvider,
    val getJobRequestSelectedForCustomer: GetJobRequestSelectedForCustomer
)

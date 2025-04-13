package com.example.servly_app.core.domain.usecase.job_posting

data class JobPostingUseCases(
    val createJobPosting: CreateJobPosting,
    val getUserJobPostings: GetUserJobPostings,
    val updateJobStatus: UpdateJobStatus,
    val getJobPosting: GetJobPosting
)

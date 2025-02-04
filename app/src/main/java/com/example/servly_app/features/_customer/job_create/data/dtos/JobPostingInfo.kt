package com.example.servly_app.features._customer.job_create.data.dtos

import com.example.servly_app.core.data.util.JobStatus

data class JobPostingInfo(
    val customerId: Long? = null,
    val title: String,
    val categoryId: Long,
    val city: String,
    val street: String,
    val houseNumber: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val answers: List<QuestionAnswer>,
    val status: JobStatus = JobStatus.ACTIVE
)
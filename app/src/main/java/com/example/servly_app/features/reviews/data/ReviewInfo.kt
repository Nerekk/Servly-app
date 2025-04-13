package com.example.servly_app.features.reviews.data

import com.example.servly_app.core.data.util.JobRequestStatus
import java.time.LocalDateTime

data class ReviewInfo(
    val id: Long? = null,
    val reviewerName: String? = null,
    val reviewerRating: Double? = null,
    val jobRequestId: Long,
    val jobRequestStatus: JobRequestStatus? = null,
    val rating: Int,
    val review: String,
    val createdAt: LocalDateTime? = null,
)
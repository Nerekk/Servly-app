package com.example.servly_app.features.job_schedule.data

import com.example.servly_app.core.data.util.ScheduleStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class ScheduleInfo(
    val id: Long? = null,
    val jobRequestId: Long = 0,
    val scheduleStatus: ScheduleStatus = ScheduleStatus.WAITING_FOR_CUSTOMER_APPROVAL,
    val updatedAt: LocalDateTime? = null,

    val startAt: LocalDate = LocalDate.now(),
    val endAt: LocalDate = LocalDate.now(),
    val title: String = "",
    val description: String = "",
    val price: Int = 0,

    val updatedStartAt: LocalDate? = null,
    val updatedEndAt: LocalDate? = null,
    val updatedTitle: String? = null,
    val updatedDescription: String? = null,
    val updatedPrice: Int? = null,
)

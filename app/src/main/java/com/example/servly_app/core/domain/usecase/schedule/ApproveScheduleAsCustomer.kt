package com.example.servly_app.core.domain.usecase.schedule

import com.example.servly_app.core.domain.repository.ScheduleRepository
import com.example.servly_app.features.job_schedule.data.ScheduleInfo

class ApproveScheduleAsCustomer(private val scheduleRepository: ScheduleRepository) {
    suspend operator fun invoke(scheduleId: Long): Result<ScheduleInfo> {
        return scheduleRepository.approveScheduleAsCustomer(scheduleId)
    }
}
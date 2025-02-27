package com.example.servly_app.core.domain.usecase.schedule

import com.example.servly_app.core.domain.repository.ScheduleRepository
import com.example.servly_app.features.job_schedule.data.ScheduleInfo

class CreateScheduleForJob(private val scheduleRepository: ScheduleRepository) {
    suspend operator fun invoke(scheduleInfo: ScheduleInfo): Result<ScheduleInfo> {
        return scheduleRepository.createScheduleForJob(scheduleInfo)
    }
}
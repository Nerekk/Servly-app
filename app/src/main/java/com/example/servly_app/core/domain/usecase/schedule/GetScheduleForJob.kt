package com.example.servly_app.core.domain.usecase.schedule

import android.util.Log
import com.example.servly_app.core.domain.repository.ScheduleRepository
import com.example.servly_app.features.job_schedule.data.ScheduleInfo

class GetScheduleForJob(private val scheduleRepository: ScheduleRepository) {
    suspend operator fun invoke(jobRequestId: Long): Result<ScheduleInfo?> {
        return scheduleRepository.getScheduleForJob(jobRequestId)
    }
}
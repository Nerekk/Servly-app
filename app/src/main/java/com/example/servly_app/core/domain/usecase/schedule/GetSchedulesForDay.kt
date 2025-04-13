package com.example.servly_app.core.domain.usecase.schedule

import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.domain.repository.ScheduleRepository
import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import java.time.LocalDate

class GetSchedulesForDay(private val scheduleRepository: ScheduleRepository) {
    suspend operator fun invoke(role: Role, day: LocalDate): Result<List<ScheduleInfo>> {
        return scheduleRepository.getSchedulesForDay(role, day)
    }
}
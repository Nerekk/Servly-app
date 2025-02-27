package com.example.servly_app.core.domain.usecase.schedule

import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.domain.repository.ScheduleRepository
import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import java.time.YearMonth

class GetSchedulesForUser(private val scheduleRepository: ScheduleRepository) {
    suspend operator fun invoke(role: Role, yearMonth: YearMonth): Result<List<ScheduleInfo>> {
        return scheduleRepository.getSchedulesForUser(role, yearMonth)
    }
}
package com.example.servly_app.core.domain.usecase.schedule

import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.domain.repository.ScheduleRepository
import com.example.servly_app.features.timetable.data.ScheduleSummary
import java.time.YearMonth

class GetScheduleSummaryForUser(private val scheduleRepository: ScheduleRepository) {
    suspend operator fun invoke(role: Role, yearMonth: YearMonth): Result<ScheduleSummary> {
        return scheduleRepository.getScheduleSummaryForUser(role, yearMonth)
    }
}
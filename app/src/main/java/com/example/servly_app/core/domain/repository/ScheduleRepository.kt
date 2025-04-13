package com.example.servly_app.core.domain.repository

import com.example.servly_app.core.data.ScheduleService
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.util.ErrorStore
import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import com.example.servly_app.features.timetable.data.ScheduleSummary
import java.time.LocalDate
import java.time.YearMonth

class ScheduleRepository(private val scheduleService: ScheduleService) {

    suspend fun getSchedulesForUser(
        role: Role,
        yearMonth: YearMonth
    ): Result<List<ScheduleInfo>> {
        return try {
            val response = scheduleService.getSchedulesForUser(role.toString(), yearMonth)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun getScheduleSummaryForUser(
        role: Role,
        yearMonth: YearMonth
    ): Result<ScheduleSummary> {
        return try {
            val response = scheduleService.getScheduleSummaryForUser(role.toString(), yearMonth)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun getSchedulesForDay(
        role: Role,
        day: LocalDate
    ): Result<List<ScheduleInfo>> {
        return try {
            val response = scheduleService.getSchedulesForDay(role.toString(), day)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun getScheduleForJob(jobRequestId: Long): Result<ScheduleInfo?> {
        return try {
            val response = scheduleService.getScheduleForJob(jobRequestId)
            if (response.isSuccessful) {
                Result.success(response.body())
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun createScheduleForJob(scheduleInfo: ScheduleInfo): Result<ScheduleInfo> {
        return try {
            val response = scheduleService.createScheduleForJob(scheduleInfo)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun updateScheduleForJob(scheduleInfo: ScheduleInfo): Result<ScheduleInfo> {
        return try {
            val response = scheduleService.updateScheduleForJob(scheduleInfo)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun approveScheduleAsCustomer(scheduleId: Long): Result<ScheduleInfo> {
        return try {
            val response = scheduleService.approveScheduleAsCustomer(scheduleId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }

    suspend fun rejectScheduleAsCustomer(scheduleId: Long): Result<ScheduleInfo> {
        return try {
            val response = scheduleService.rejectScheduleAsCustomer(scheduleId)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                throw Exception("Error: ${response.errorBody()?.string()}")
            }
        } catch (e: Exception) {
            ErrorStore.addError(e.message.toString())
            Result.failure(e)
        }
    }
}
package com.example.servly_app.core.data

import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import com.example.servly_app.features.timetable.data.ScheduleSummary
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query
import java.time.LocalDate
import java.time.YearMonth

interface ScheduleService {

    @GET("api/schedule/user")
    suspend fun getSchedulesForUser(
        @Query("role") role: String,
        @Query("yearMonth") yearMonth: YearMonth
    ): Response<List<ScheduleInfo>>

    @GET("api/schedule/summary/user")
    suspend fun getScheduleSummaryForUser(
        @Query("role") role: String,
        @Query("yearMonth") yearMonth: YearMonth
    ): Response<ScheduleSummary>

    @GET("api/schedule/day")
    suspend fun getSchedulesForDay(
        @Query("role") role: String,
        @Query("day") day: LocalDate
    ): Response<List<ScheduleInfo>>

    @GET("api/schedule/job")
    suspend fun getScheduleForJob(@Query("jobRequestId") jobRequestId: Long): Response<ScheduleInfo?>

    @POST("api/schedule")
    suspend fun createScheduleForJob(@Body scheduleInfo: ScheduleInfo): Response<ScheduleInfo>

    @PUT("api/schedule")
    suspend fun updateScheduleForJob(@Body scheduleInfo: ScheduleInfo): Response<ScheduleInfo>

    @PUT("api/schedule/approve")
    suspend fun approveScheduleAsCustomer(@Query("scheduleId") scheduleId: Long): Response<ScheduleInfo>

    @PUT("api/schedule/reject")
    suspend fun rejectScheduleAsCustomer(@Query("scheduleId") scheduleId: Long): Response<ScheduleInfo>
}
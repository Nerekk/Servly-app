package com.example.servly_app.core.data

import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface ScheduleService {

    @GET("api/schedule/user")
    suspend fun getSchedulesForUser(
        @Query("role") role: String,
        @Query("yearMonth") yearMonth: String
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
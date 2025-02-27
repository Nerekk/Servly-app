package com.example.servly_app.core.domain.usecase.schedule

data class ScheduleUseCases(
    val getSchedulesForUser: GetSchedulesForUser,
    val getScheduleForJob: GetScheduleForJob,
    val createScheduleForJob: CreateScheduleForJob,
    val updateScheduleForJob: UpdateScheduleForJob,
    val approveScheduleAsCustomer: ApproveScheduleAsCustomer,
    val rejectScheduleAsCustomer: RejectScheduleAsCustomer
)
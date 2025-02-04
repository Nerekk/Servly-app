package com.example.servly_app.features._customer.job_list.presentation.job_schedule

import java.time.LocalDate

data class Service(
    val title: String,
    val subtasks: List<String>,
    val price: Double
)

data class Schedule(
    val workDays: List<LocalDate>,
    val services: List<Service>
)

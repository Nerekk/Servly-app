package com.example.servly_app.features.customer.requests.request_schedule

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

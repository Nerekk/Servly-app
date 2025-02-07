package com.example.servly_app.features._customer.job_list.presentation.main_view.components

import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.data.util.JobStatus.DONE

data class Order(
    val id: Long,
    val title: String,
    val location: String,
    val category: String,
    val status: JobStatus,
    val person: String? = null
)

fun getInProgressRequests() = listOf(
    Order(1, "Zapytanie 1", "Łódź, Górna", "Mechanik", JobStatus.ACTIVE, "Tomasz"),
    Order(2, "Zapytanie 2", "Łódź, Polesie", "Elektryk", JobStatus.ACTIVE, "Jan"),
)

fun getFinishedRequests() = listOf(
    Order(8, "Zapytanie 8", "Opis zapytania zakończonego", "Zrobione", DONE),
    Order(9, "Zapytanie 9", "Opis zapytania zakończonego", "Zrobione", DONE),
)
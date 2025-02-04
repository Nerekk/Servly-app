package com.example.servly_app.features._customer.job_list.presentation.main_view

import com.example.servly_app.core.data.util.JobStatus

// Przykładowe dane
data class Order(val title: String, val location: String, val category: String, val status: JobStatus)

fun getInProgressRequests() = listOf(
    Order("Zapytanie 1", "Łódź, Górna", "Jan", JobStatus.ACTIVE),
    Order("Zapytanie 2", "Łódź, Polesie", "Tomasz", JobStatus.ACTIVE),
    Order("Zapytanie 3", "Pabianice", "Robert", JobStatus.ACTIVE),
    Order("Zapytanie 4", "Łódź, Widzew", "Włodzimierz", JobStatus.ACTIVE),
    Order("Zapytanie 5", "Pabianice", "Kamil", JobStatus.ACTIVE),
    Order("Zapytanie 6", "Łódź, Górna", "Jakub", JobStatus.ACTIVE),
    Order("Zapytanie 7", "Łódź, Centrum", "Jacek", JobStatus.ACTIVE)
)

fun getFinishedRequests() = listOf(
    Order("Zapytanie 8", "Opis zapytania zakończonego", "Zrobione", JobStatus.DONE),
    Order("Zapytanie 9", "Opis zapytania zakończonego", "Zrobione", JobStatus.DONE),
    Order("Zapytanie 10", "Opis zapytania anulowanego", "Anulowane", JobStatus.CANCELED),
    Order("Zapytanie 11", "Opis zapytania zakończonego", "Zrobione", JobStatus.DONE),
    Order("Zapytanie 12", "Opis zapytania anulowanego", "Anulowane", JobStatus.CANCELED),
    Order("Zapytanie 13", "Opis zapytania zakończonego", "Zrobione", JobStatus.DONE),
    Order("Zapytanie 14", "Opis zapytania anulowanego", "Anulowane", JobStatus.CANCELED)
)
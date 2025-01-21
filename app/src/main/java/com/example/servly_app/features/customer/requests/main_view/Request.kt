package com.example.servly_app.features.customer.requests.main_view

// Przykładowe dane
data class Request(val title: String, val location: String, val person: String, val status: OfferStatus)

fun getInProgressRequests() = listOf(
    Request("Zapytanie 1", "Łódź, Górna", "Jan", OfferStatus.ACTIVE),
    Request("Zapytanie 2", "Łódź, Polesie", "Tomasz", OfferStatus.ACTIVE),
    Request("Zapytanie 3", "Pabianice", "Robert", OfferStatus.ACTIVE),
    Request("Zapytanie 4", "Łódź, Widzew", "Włodzimierz", OfferStatus.ACTIVE),
    Request("Zapytanie 5", "Pabianice", "Kamil", OfferStatus.ACTIVE),
    Request("Zapytanie 6", "Łódź, Górna", "Jakub", OfferStatus.ACTIVE),
    Request("Zapytanie 7", "Łódź, Centrum", "Jacek", OfferStatus.ACTIVE)
)

fun getFinishedRequests() = listOf(
    Request("Zapytanie 8", "Opis zapytania zakończonego", "Zrobione", OfferStatus.DONE),
    Request("Zapytanie 9", "Opis zapytania zakończonego", "Zrobione", OfferStatus.DONE),
    Request("Zapytanie 10", "Opis zapytania anulowanego", "Anulowane", OfferStatus.CANCELED),
    Request("Zapytanie 11", "Opis zapytania zakończonego", "Zrobione", OfferStatus.DONE),
    Request("Zapytanie 12", "Opis zapytania anulowanego", "Anulowane", OfferStatus.CANCELED),
    Request("Zapytanie 13", "Opis zapytania zakończonego", "Zrobione", OfferStatus.DONE),
    Request("Zapytanie 14", "Opis zapytania anulowanego", "Anulowane", OfferStatus.CANCELED)
)
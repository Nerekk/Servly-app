package com.example.servly_app.core.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object LanguageUtils {
    fun getSystemLanguage(): String {
        return if (Locale.getDefault().language == "pl") "pl" else "en"
    }

    fun formatDate(date: LocalDate): String {
        val locale = if (Locale.getDefault().language == "pl") Locale("pl", "PL") else Locale.ENGLISH
        val pattern = "d MMMM"

        val formatter = DateTimeFormatter.ofPattern(pattern, locale)
        return date.format(formatter)
    }

    fun formatReviewDateTime(dateTime: LocalDateTime?): String {
        val locale = if (Locale.getDefault().language == "pl") Locale("pl", "PL") else Locale.ENGLISH
        val pattern = "d MMMM HH:mm"

        val formatter = DateTimeFormatter.ofPattern(pattern, locale)

        if (dateTime != null) {
            return dateTime.format(formatter)
        } else {
            return "null"
        }
    }
}
package com.example.servly_app.core.util

import java.text.DecimalFormat

fun formatRating(rating: Double): String {
    val decimalFormat = DecimalFormat("#.0")
    return decimalFormat.format(rating)
}
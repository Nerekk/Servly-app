package com.example.servly_app.core.util

import java.util.Locale

object LanguageUtils {
    fun getSystemLanguage(): String {
        return if (Locale.getDefault().language == "pl") "pl" else "en"
    }
}
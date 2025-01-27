package com.example.servly_app.features.role_selection.presentation.components

class RegexConstants {
    companion object {
        val PHONE = Regex("^\\+[0-9]{10,15}$")
        val NAME = Regex("^[A-Za-zżźćńółęąśŻŹĆĄŚĘŁÓŃ ]+$")
    }
}
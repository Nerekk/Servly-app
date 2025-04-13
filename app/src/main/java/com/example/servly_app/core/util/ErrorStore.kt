package com.example.servly_app.core.util

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ErrorStore {
    private val _errors = MutableStateFlow<List<String>>(emptyList())
    val errors: StateFlow<List<String>> = _errors

    fun addError(message: String) {
        _errors.value += message
    }

    fun clearErrors() {
        _errors.value = emptyList()
    }
}
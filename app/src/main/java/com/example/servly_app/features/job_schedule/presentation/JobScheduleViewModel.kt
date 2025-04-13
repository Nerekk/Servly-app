package com.example.servly_app.features.job_schedule.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.domain.usecase.schedule.ScheduleUseCases
import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JobScheduleState(
    val isLoading: Boolean = false,
    val schedule: ScheduleInfo? = null,

    val errorMessage: String? = null
)

@HiltViewModel
class JobScheduleViewModel @Inject constructor(
    private val scheduleUseCases: ScheduleUseCases
): ViewModel() {
    private val _jobScheduleState = MutableStateFlow(JobScheduleState())
    val jobScheduleState = _jobScheduleState.asStateFlow()

    fun setSchedule(schedule: ScheduleInfo) {
        _jobScheduleState.update { it.copy(schedule = schedule) }
    }

    fun approveSchedule(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _jobScheduleState.update { it.copy(isLoading = true) }

            val result = scheduleUseCases.approveScheduleAsCustomer(_jobScheduleState.value.schedule!!.id!!)
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { e ->
                    _jobScheduleState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _jobScheduleState.update { it.copy(isLoading = false) }
        }
    }

    fun rejectSchedule(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _jobScheduleState.update { it.copy(isLoading = true) }

            val result = scheduleUseCases.rejectScheduleAsCustomer(_jobScheduleState.value.schedule!!.id!!)
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { e ->
                    _jobScheduleState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _jobScheduleState.update { it.copy(isLoading = false) }
        }
    }
}
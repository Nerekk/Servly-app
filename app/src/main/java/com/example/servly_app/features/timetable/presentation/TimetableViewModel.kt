package com.example.servly_app.features.timetable.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.domain.usecase.schedule.ScheduleUseCases
import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import com.example.servly_app.features.timetable.data.DateRange
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

data class TimetableState(
    val isLoading: Boolean = false,

    val selectedDay: LocalDate = LocalDate.now(),
    val selectedYearMonth: YearMonth = YearMonth.of(selectedDay.year, selectedDay.month),

    val totalTasks: Int = 0,
    val taskDates: List<DateRange> = emptyList()
)

data class DayTasksState(
    val isLoading: Boolean = false,

    val tasks: List<ScheduleInfo> = emptyList()
)

@HiltViewModel(assistedFactory = TimetableViewModel.TimetableViewModelFactory::class)
class TimetableViewModel @AssistedInject constructor(
    private val scheduleUseCases: ScheduleUseCases,
    @Assisted val role: Role
): ViewModel() {
    @AssistedFactory
    interface TimetableViewModelFactory {
        fun create(role: Role): TimetableViewModel
    }

    private val _timetableState = MutableStateFlow(TimetableState())
    val timetableState = _timetableState.asStateFlow()

    private val _dayTasksState = MutableStateFlow(DayTasksState())
    val dayTasksState = _dayTasksState.asStateFlow()

    init {
        loadData()
    }

    fun setSelectedDay(selectedDay: LocalDate) {
        _timetableState.update { it.copy(selectedDay = selectedDay) }
        loadDayTasks()
    }


    fun setPreviousYearMonth() {
        _timetableState.update {
            val newSelectedYearMonth = it.selectedYearMonth.minusMonths(1)
            it.copy(
                selectedYearMonth = newSelectedYearMonth,
                selectedDay = LocalDate.of(newSelectedYearMonth.year, newSelectedYearMonth.month, 1)
            )
        }
        loadData()
    }

    fun setNextYearMonth() {
        _timetableState.update {
            val newSelectedYearMonth = it.selectedYearMonth.plusMonths(1)
            it.copy(
                selectedYearMonth = newSelectedYearMonth,
                selectedDay = LocalDate.of(newSelectedYearMonth.year, newSelectedYearMonth.month, 1)
            )
        }
        loadData()
    }

    fun loadData() {
        loadTaskDateRanges()
        loadDayTasks()
    }

    fun loadTaskDateRanges() {
        viewModelScope.launch {
            _timetableState.update { it.copy(isLoading = true) }

            val result = scheduleUseCases.getScheduleSummaryForUser(role, _timetableState.value.selectedYearMonth)
            result.fold(
                onSuccess = { summary ->
                    _timetableState.update {
                        it.copy(
                            totalTasks = summary.totalSchedules,
                            taskDates = summary.dateRanges
                        )
                    }
                },
                onFailure = { e ->
                    Log.i("Timetable", "FAIL ${e.message}")
                }
            )

            _timetableState.update { it.copy(isLoading = false) }
        }
    }

    fun loadDayTasks() {
        viewModelScope.launch {
            _dayTasksState.update { it.copy(isLoading = true) }

            val result = scheduleUseCases.getSchedulesForDay(role, _timetableState.value.selectedDay)
            result.fold(
                onSuccess = { schedules ->
                    _dayTasksState.update { it.copy(tasks = schedules) }
                },
                onFailure = { e ->
                    Log.i("Timetable", "FAIL ${e.message}")
                }
            )

            _dayTasksState.update { it.copy(isLoading = false) }
        }
    }

}
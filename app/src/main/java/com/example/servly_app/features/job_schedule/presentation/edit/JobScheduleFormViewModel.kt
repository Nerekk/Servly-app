package com.example.servly_app.features.job_schedule.presentation.edit

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.domain.usecase.schedule.ScheduleUseCases
import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class JobScheduleFormState(
    val isLoading: Boolean = false,
    val scheduleInfo: ScheduleInfo? = null,
    val isButtonEnabled: Boolean = true,

    val errorMessage: String? = null
)

@HiltViewModel
class JobScheduleFormViewModel @Inject constructor(
    private val scheduleUseCases: ScheduleUseCases
): ViewModel() {
    private val _jobScheduleFormState = MutableStateFlow(JobScheduleFormState())
    val jobScheduleFormState = _jobScheduleFormState.asStateFlow()

    fun setSchedule(scheduleToDo: ScheduleInfo, isEdit: Boolean = false) {
        if (isEdit) {
            val editSchedule = scheduleToDo.copy(
                updatedStartAt = scheduleToDo.startAt,
                updatedEndAt = scheduleToDo.endAt,
                updatedTitle = scheduleToDo.title,
                updatedDescription = scheduleToDo.description,
                updatedPrice = scheduleToDo.price
            )
            _jobScheduleFormState.update { it.copy(scheduleInfo = editSchedule) }
            validButton()
        } else {
            _jobScheduleFormState.update { it.copy(scheduleInfo = scheduleToDo) }
        }
    }

    fun updateStartDate(localDate: LocalDate) {
        _jobScheduleFormState.value.scheduleInfo?.let {
            it.updatedEndAt?.let { endAt ->
                if (localDate.isAfter(endAt)) return
            }

            _jobScheduleFormState.update { state ->
                state.copy(
                    scheduleInfo = state.scheduleInfo?.copy(
                        updatedStartAt = localDate
                    )
                )
            }
            validButton()
        }
    }

    fun updateEndDate(localDate: LocalDate) {
        _jobScheduleFormState.value.scheduleInfo?.let {
            it.updatedStartAt?.let { startAt ->
                if (localDate.isBefore(startAt)) return
            }

            _jobScheduleFormState.update { state ->
                state.copy(scheduleInfo = state.scheduleInfo?.copy(updatedEndAt = localDate))
            }
            validButton()
        }
    }

    fun updateTitle(title: String) {
        _jobScheduleFormState.value.scheduleInfo?.let {
            _jobScheduleFormState.update { it.copy(scheduleInfo = it.scheduleInfo?.copy(updatedTitle = title)) }
            validButton()
        }
    }

    fun updateDescription(description: String) {
        _jobScheduleFormState.value.scheduleInfo?.let {
            if (description.isNotEmpty()) {
                _jobScheduleFormState.update { it.copy(scheduleInfo = it.scheduleInfo?.copy(updatedDescription = description)) }
            } else {
                _jobScheduleFormState.update { it.copy(scheduleInfo = it.scheduleInfo?.copy(updatedDescription = null)) }
            }
            validButton()
        }
    }

    fun updatePrice(price: String) {
        if (price.isNotEmpty()) {
            price.toIntOrNull()?.let {
                _jobScheduleFormState.value.scheduleInfo?.let {
                    _jobScheduleFormState.update { it.copy(scheduleInfo = it.scheduleInfo?.copy(updatedPrice = price.toInt())) }
                }
            }
        } else {
            _jobScheduleFormState.value.scheduleInfo?.let {
                _jobScheduleFormState.update { it.copy(scheduleInfo = it.scheduleInfo?.copy(updatedPrice = null)) }
            }
        }
        validButton()
    }

    fun createSchedule(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _jobScheduleFormState.update { it.copy(isLoading = true) }

            val result = scheduleUseCases.createScheduleForJob(_jobScheduleFormState.value.scheduleInfo!!)
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { e ->
                    _jobScheduleFormState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _jobScheduleFormState.update { it.copy(isLoading = false) }
        }
    }

    fun updateSchedule(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _jobScheduleFormState.update { it.copy(isLoading = true) }

            val result = scheduleUseCases.updateScheduleForJob(_jobScheduleFormState.value.scheduleInfo!!)
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { e ->
                    _jobScheduleFormState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _jobScheduleFormState.update { it.copy(isLoading = false) }
        }
    }

    private fun validButton() {
        _jobScheduleFormState.update { state ->
            Log.i("VALIDBUTTON", "StartAt ${state.scheduleInfo!!.startAt != state.scheduleInfo!!.updatedStartAt}")
            Log.i("VALIDBUTTON", "EndAt ${state.scheduleInfo!!.endAt != state.scheduleInfo!!.updatedEndAt}")
            Log.i("VALIDBUTTON", "Title ${state.scheduleInfo!!.title != state.scheduleInfo!!.updatedTitle}")
            Log.i("VALIDBUTTON", "Price ${state.scheduleInfo!!.price != state.scheduleInfo!!.updatedPrice}")
            Log.i("VALIDBUTTON", "Description ${state.scheduleInfo!!.description != state.scheduleInfo!!.updatedDescription}")

            state.copy(
                isButtonEnabled = _jobScheduleFormState.value.scheduleInfo?.let {
                    it.price != it.updatedPrice || it.startAt != it.updatedStartAt ||
                            it.endAt != it.updatedEndAt || it.title != it.updatedTitle ||
                            it.description != it.updatedDescription
                } ?: false
            )
        }
    }
}
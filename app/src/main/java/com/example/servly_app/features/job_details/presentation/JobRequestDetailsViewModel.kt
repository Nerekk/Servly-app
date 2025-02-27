package com.example.servly_app.features.job_details.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.domain.usecase.job_request.JobRequestUseCases
import com.example.servly_app.core.domain.usecase.schedule.ScheduleUseCases
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.features.job_details.data.JobRequestInfo
import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import com.example.servly_app.features.reviews.data.ReviewInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JobRequestDetailsState(
    val isLoading: Boolean = true,

    val jobRequests: List<JobRequestInfo> = emptyList(),

    val bottomSheetJobRequest: JobRequestInfo? = null,

    val selectedJobRequest: JobRequestInfo? = null,

    val providerId: Long? = null,
    val providersJobRequest: JobRequestInfo? = null,

    val schedule: ScheduleInfo? = null,
    val showDialog: Boolean = false,
    val showDialogEdit: Boolean = false,

    val errorMessage: String? = null
)

@HiltViewModel(assistedFactory = JobRequestDetailsViewModel.JobRequestDetailsViewModelFactory::class)
class JobRequestDetailsViewModel @AssistedInject constructor(
    private val jobRequestUseCases: JobRequestUseCases,
    private val scheduleUseCases: ScheduleUseCases,
    @Assisted val order: JobPostingInfo,
    @Assisted val providerId: Long?
): ViewModel() {
    @AssistedFactory
    interface JobRequestDetailsViewModelFactory {
        fun create(order: JobPostingInfo, providerId: Long?): JobRequestDetailsViewModel
    }

    private val _jobRequestDetailsState = MutableStateFlow(JobRequestDetailsState())
    val orderHeaderState = _jobRequestDetailsState.asStateFlow()

    init {
        providerId?.let {
            fetchJobRequestForProvider()
        } ?: run {
            fetchJobRequestForCustomer()
        }
    }

    fun fetchJobRequestForProvider() {
        viewModelScope.launch {
            _jobRequestDetailsState.update { it.copy(isLoading = true) }

            val result = jobRequestUseCases.getJobRequestForProvider(order.id!!, providerId!!)
            result.fold(
                onSuccess = { jobRequest ->
                    Log.i("PROVIDER_DETAILS", "SUCCESS")
                    jobRequest?.let {
                        Log.i("PROVIDER_DETAILS", "$jobRequest")
                        _jobRequestDetailsState.update { it.copy(
                            providersJobRequest = jobRequest,
                            schedule = jobRequest.schedule
                        ) }
                    }
                },
                onFailure = { e ->
                    Log.i("PROVIDER_DETAILS", "FAIL ${e.message}")
                    _jobRequestDetailsState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _jobRequestDetailsState.update { it.copy(isLoading = false) }
        }
    }

    fun fetchJobRequestForCustomer() {
        viewModelScope.launch {
            _jobRequestDetailsState.update { it.copy(isLoading = true) }

            val result = jobRequestUseCases.getJobRequestSelectedForCustomer(order.id!!)
            result.fold(
                onSuccess = { jobRequest ->
                    Log.i("CUSTOMER_DETAILS", "SUCCESS")
                    jobRequest?.let {
                        Log.i("CUSTOMER_DETAILS", "$jobRequest")
                        _jobRequestDetailsState.update { it.copy(
                            selectedJobRequest = jobRequest,
                            schedule = jobRequest.schedule
                        ) }
                    } ?: run {
                        val list = loadRequests(order.id)
                        _jobRequestDetailsState.update { it.copy(jobRequests = list) }
                    }
                },
                onFailure = { e ->
                    Log.i("CUSTOMER_DETAILS", "FAIL ${e.message}")
                    _jobRequestDetailsState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _jobRequestDetailsState.update { it.copy(isLoading = false) }
        }
    }

    private suspend fun loadRequests(jobPostingId: Long): List<JobRequestInfo> {
        val result = jobRequestUseCases.getJobRequestsPosting(jobPostingId)
        return result.getOrElse {
            Log.i("REQUESTS_FETCH_ERROR", it.message.toString())
            emptyList()
        }
    }

    fun updateCustomerReview(reviewInfo: ReviewInfo) {
        _jobRequestDetailsState.update { currentState ->
            if (currentState.selectedJobRequest != null) {
                currentState.copy(
                    selectedJobRequest = currentState.selectedJobRequest.copy(customerReview = reviewInfo)
                )
            } else {
                currentState
            }
        }
    }

    fun updateProviderReview(reviewInfo: ReviewInfo) {
        _jobRequestDetailsState.update { currentState ->
            if (currentState.providersJobRequest != null) {
                currentState.copy(
                    providersJobRequest = currentState.providersJobRequest.copy(providerReview = reviewInfo)
                )
            } else {
                currentState
            }
        }
    }

    fun updateBottomSheetProvider(jobRequestInfo: JobRequestInfo?) {
        _jobRequestDetailsState.update { it.copy(bottomSheetJobRequest = jobRequestInfo) }
    }

    fun clearError() {
        _jobRequestDetailsState.update { it.copy(errorMessage = null) }
    }

    fun updateShowDialog(showDialog: Boolean) {
        _jobRequestDetailsState.update { it.copy(showDialog = showDialog) }
    }

    fun updateShowDialogEdit(showDialog: Boolean) {
        _jobRequestDetailsState.update { it.copy(showDialogEdit = showDialog) }
    }

    // provider
    fun createJobRequest(jobPostingId: Long) {
        viewModelScope.launch {
            _jobRequestDetailsState.update { it.copy(isLoading = true) }

            val result = jobRequestUseCases.createJobRequest(jobPostingId)
            result.fold(
                onSuccess = { jobRequest ->
                    _jobRequestDetailsState.update { it.copy(providersJobRequest = jobRequest) }
                },
                onFailure = { e ->
                    _jobRequestDetailsState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _jobRequestDetailsState.update { it.copy(isLoading = false) }
        }
    }

    // provider
    fun withdrawnJobRequest(jobRequestId: Long) {
        updateJobRequestStatus(jobRequestId, JobRequestStatus.WITHDRAWN)
    }

    // customer
    fun selectProvider(jobRequestId: Long) {
        updateJobRequestStatus(jobRequestId, JobRequestStatus.WAITING_FOR_PROVIDER_APPROVE)
    }

    // provider
    fun acceptJob(jobRequestId: Long) {
        updateJobRequestStatus(jobRequestId, JobRequestStatus.IN_PROGRESS)
    }

    // both
    fun sendFinishRequest(role: Role) {
        if (role == Role.CUSTOMER) {
            if (_jobRequestDetailsState.value.selectedJobRequest == null) return
            updateJobRequestStatus(_jobRequestDetailsState.value.selectedJobRequest!!.id!!, JobRequestStatus.WAITING_FOR_PROVIDER_COMPLETE)
        } else {
            if (_jobRequestDetailsState.value.providersJobRequest == null) return
            updateJobRequestStatus(_jobRequestDetailsState.value.providersJobRequest!!.id!!, JobRequestStatus.WAITING_FOR_CUSTOMER_COMPLETE)
        }
    }

    // both
    fun rejectFinishRequest(role: Role) {
        if (role == Role.CUSTOMER) {
            if (_jobRequestDetailsState.value.selectedJobRequest == null) return
            updateJobRequestStatus(_jobRequestDetailsState.value.selectedJobRequest!!.id!!, JobRequestStatus.IN_PROGRESS)
        } else {
            if (_jobRequestDetailsState.value.providersJobRequest == null) return
            updateJobRequestStatus(_jobRequestDetailsState.value.providersJobRequest!!.id!!, JobRequestStatus.IN_PROGRESS)
        }

    }

    // both
    fun finishJob(role: Role) {
        if (role == Role.CUSTOMER) {
            if (_jobRequestDetailsState.value.selectedJobRequest == null) return
            updateJobRequestStatus(_jobRequestDetailsState.value.selectedJobRequest!!.id!!, JobRequestStatus.COMPLETED)
        } else {
            if (_jobRequestDetailsState.value.providersJobRequest == null) return
            updateJobRequestStatus(_jobRequestDetailsState.value.providersJobRequest!!.id!!, JobRequestStatus.COMPLETED)
        }
    }

    // both
    fun cancelJob(role: Role) {
        if (role == Role.CUSTOMER) {
            if (_jobRequestDetailsState.value.selectedJobRequest == null) return
            updateJobRequestStatus(_jobRequestDetailsState.value.selectedJobRequest!!.id!!, JobRequestStatus.CANCELED_IN_PROGRESS_BY_CUSTOMER)
        } else {
            if (_jobRequestDetailsState.value.providersJobRequest == null) return
            updateJobRequestStatus(_jobRequestDetailsState.value.providersJobRequest!!.id!!, JobRequestStatus.CANCELED_IN_PROGRESS_BY_PROVIDER)
        }
    }

    private fun updateJobRequestStatus(jobRequestId: Long, jobRequestStatus: JobRequestStatus) {
        viewModelScope.launch {
            _jobRequestDetailsState.update { it.copy(isLoading = true) }


            val result = jobRequestUseCases.updateJobRequestStatus(
                jobRequestId,
                jobRequestStatus
            )
            result.fold(
                onSuccess = { jobRequest ->
                    updateJobRequest(jobRequest)
                },
                onFailure = { e ->
                    _jobRequestDetailsState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )
        }

        _jobRequestDetailsState.update { it.copy(isLoading = false) }
    }

    private fun updateJobRequest(updatedJobRequest: JobRequestInfo) {

        _jobRequestDetailsState.update { currentState ->
            providerId?.let {
                currentState.copy(
                    providersJobRequest = updatedJobRequest,
                    schedule = updatedJobRequest.schedule
                )
            } ?: run {
                currentState.selectedJobRequest?.let {
                    currentState.copy(
                        selectedJobRequest = updatedJobRequest,
                        schedule = updatedJobRequest.schedule
                    )
                } ?: run {
                    currentState.copy(
                        jobRequests = currentState.jobRequests.map { jobRequest ->
                            if (jobRequest.id == updatedJobRequest.id) updatedJobRequest else jobRequest
                        }
                    )
                }
            }

        }

    }

    private fun refreshRoleStates(scheduleCallback: (Long) -> Unit) {
        refreshSelectedJobRequest(scheduleCallback)
        if (_jobRequestDetailsState.value.jobRequests.isNotEmpty()) {
            checkForProvidersRequest(scheduleCallback)
        }
    }

    private fun refreshSelectedJobRequest(scheduleCallback: (Long) -> Unit) {
        val validStatuses = setOf(
            JobRequestStatus.IN_PROGRESS,
            JobRequestStatus.WAITING_FOR_PROVIDER_COMPLETE,
            JobRequestStatus.WAITING_FOR_CUSTOMER_COMPLETE,
            JobRequestStatus.COMPLETED,
            JobRequestStatus.CANCELED_IN_PROGRESS_BY_CUSTOMER,
            JobRequestStatus.CANCELED_IN_PROGRESS_BY_PROVIDER
        )

        val newSelectedJobRequest = _jobRequestDetailsState.value.jobRequests.firstOrNull { it.jobRequestStatus in validStatuses }

        if (_jobRequestDetailsState.value.selectedJobRequest?.id != newSelectedJobRequest?.id) {
            _jobRequestDetailsState.update { it.copy(selectedJobRequest = newSelectedJobRequest) }
        }

        newSelectedJobRequest?.let {
            if (_jobRequestDetailsState.value.schedule?.jobRequestId != it.id) {

//                getSchedule(it.id!!)
            }
        }
    }

    private fun checkForProvidersRequest(scheduleCallback: (Long) -> Unit) {
        _jobRequestDetailsState.value.providerId?.let {
            _jobRequestDetailsState.update { currentState ->
                currentState.copy(
                    providersJobRequest = currentState.jobRequests.firstOrNull { it.provider!!.providerId == currentState.providerId }
                )
            }

            _jobRequestDetailsState.value.providersJobRequest?.let {
                scheduleCallback(it.id!!)
//                getSchedule(it.id!!)
            }
        }
    }
}
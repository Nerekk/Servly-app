package com.example.servly_app.features._provider.job_request_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.core.domain.usecase.job_request.JobRequestUseCases
import com.example.servly_app.features._customer.job_create.data.dtos.CategoryInfo
import com.example.servly_app.features._customer.job_create.domain.usecase.CategoryUseCases
import com.example.servly_app.features.job_details.data.JobRequestInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JobRequestListState(
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false,

    val activeJobRequests: List<JobRequestInfo> = emptyList(),
    val hasMoreActive: Boolean = true,
    val activePage: Int = 0,

    val endedJobRequests: List<JobRequestInfo> = emptyList(),
    val hasMoreEnded: Boolean = true,
    val endedPage: Int = 0,

    val sortType: SortType = SortType.DESCENDING,
    val errorMessage: String? = null,

    val categories: List<CategoryInfo> = emptyList()
) {
    fun getCategoryName(index: Long): String {
        val category = categories.find { it.id == index }
        return category?.name ?: "Unknown category"
    }
}

@HiltViewModel
class JobRequestListViewModel @Inject constructor(
    private val jobRequestUseCases: JobRequestUseCases,
    private val categoryUseCases: CategoryUseCases
): ViewModel() {
    private val _jobRequestListState = MutableStateFlow(JobRequestListState())
    val jobRequestListState = _jobRequestListState.asStateFlow()

    private val activeJobRequestStatuses: List<JobRequestStatus> = listOf(
        JobRequestStatus.ACTIVE,
        JobRequestStatus.WAITING_FOR_PROVIDER_APPROVE,
        JobRequestStatus.IN_PROGRESS,
        JobRequestStatus.WAITING_FOR_CUSTOMER_COMPLETE,
        JobRequestStatus.WAITING_FOR_PROVIDER_COMPLETE,
    )

    private val endedJobRequestStatuses: List<JobRequestStatus> = listOf(
        JobRequestStatus.COMPLETED,
        JobRequestStatus.REJECTED,
        JobRequestStatus.CANCELED_IN_PROGRESS_BY_CUSTOMER,
        JobRequestStatus.CANCELED_IN_PROGRESS_BY_PROVIDER,
        JobRequestStatus.WITHDRAWN,
    )

    init {
        loadCategories()
        loadActiveOrders()
        loadEndedOrders()
    }

    fun setTabIndex(index: Int) {
        if (index == 0 || index == 1) _jobRequestListState.update { it.copy(selectedTabIndex = index) }
    }

    fun loadOrders() {
        if (_jobRequestListState.value.selectedTabIndex==0) {
            loadActiveOrders()
        } else {
            loadEndedOrders()
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _jobRequestListState.update { it.copy(isLoading = true) }

            val result = categoryUseCases.getCategories()

            result.fold(
                onSuccess = { list ->
                    _jobRequestListState.update { it.copy(categories = list) }
                },
                onFailure = { e ->
                    _jobRequestListState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _jobRequestListState.update { it.copy(isLoading = false) }
        }
    }

    private fun loadActiveOrders() {
        if (!_jobRequestListState.value.hasMoreActive) return
        Log.i("LOAD ACTIVE ORDER", "LOADING")

        viewModelScope.launch {
            _jobRequestListState.update { it.copy(isLoading = true) }
            delay(500)

            val result = jobRequestUseCases.getJobRequests(
                statuses = activeJobRequestStatuses,
                sortType = _jobRequestListState.value.sortType,
                page = _jobRequestListState.value.activePage,
                size = 7
            )

            result.fold(
                onSuccess = { pagedResponse ->
                    Log.i("JOB_REQUEST", "SUCCESS: ${pagedResponse.content}")
                    _jobRequestListState.update {
                        it.copy(
                            hasMoreActive = pagedResponse.content.isNotEmpty(),
                            activeJobRequests = it.activeJobRequests + pagedResponse.content,
                            activePage = if (pagedResponse.content.isNotEmpty()) it.activePage+1 else it.activePage
                        )
                    }
                },
                onFailure = { e ->
                    Log.i("JOB_REQUEST", "FAILED: ${e.message}")
                    _jobRequestListState.update {
                        it.copy(
                            errorMessage = "Error: ${e.message}"
                        )
                    }
                }
            )

            _jobRequestListState.update { it.copy(isLoading = false) }
        }
    }

    private fun loadEndedOrders() {
        if (!_jobRequestListState.value.hasMoreEnded) return
        Log.i("LOAD ENDED ORDER", "LOADING")
        viewModelScope.launch {
            _jobRequestListState.update { it.copy(isLoading = true) }

            val result = jobRequestUseCases.getJobRequests(
                statuses = endedJobRequestStatuses,
                sortType = _jobRequestListState.value.sortType,
                page = _jobRequestListState.value.endedPage,
                size = 10
            )

            result.fold(
                onSuccess = { pagedResponse ->
                    _jobRequestListState.update {
                        it.copy(
                            hasMoreEnded = pagedResponse.content.isNotEmpty(),
                            endedJobRequests = it.endedJobRequests + pagedResponse.content,
                            endedPage = if (pagedResponse.content.isNotEmpty()) it.endedPage+1 else it.endedPage
                        )
                    }
                },
                onFailure = { e ->
                    _jobRequestListState.update {
                        it.copy(
                            errorMessage = "Error: ${e.message}"
                        )
                    }
                }
            )

            _jobRequestListState.update { it.copy(isLoading = false) }
        }
    }
}
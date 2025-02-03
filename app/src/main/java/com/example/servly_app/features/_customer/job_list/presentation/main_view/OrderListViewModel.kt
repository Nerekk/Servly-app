package com.example.servly_app.features._customer.job_list.presentation.main_view

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.features._customer.job_create.data.dtos.CategoryInfo
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.features._customer.job_create.domain.usecase.CategoryUseCases
import com.example.servly_app.features._customer.job_list.domain.usecase.RequestUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class OrderListState(
    val selectedTabIndex: Int = 0,
    val isLoading: Boolean = false,

    val activeOrders: List<JobPostingInfo> = emptyList(),
    val hasMoreActive: Boolean = true,
    val activePage: Int = 0,

    val endedOrders: List<JobPostingInfo> = emptyList(),
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
class OrderListViewModel @Inject constructor(
    private val requestUseCases: RequestUseCases,
    private val categoryUseCases: CategoryUseCases
): ViewModel() {
    private val _orderListState = MutableStateFlow(OrderListState())
    val orderListState = _orderListState.asStateFlow()

    init {
        loadCategories()
        loadActiveOrders()
        loadEndedOrders()
    }

    fun setTabIndex(index: Int) {
        if (index == 0 || index == 1) _orderListState.update { it.copy(selectedTabIndex = index) }
    }

    fun loadOrders() {
        if (_orderListState.value.selectedTabIndex==0) {
            loadActiveOrders()
        } else {
            loadEndedOrders()
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _orderListState.update { it.copy(isLoading = true) }

            val result = categoryUseCases.getCategories()

            result.fold(
                onSuccess = { list ->
                    _orderListState.update { it.copy(categories = list) }
                },
                onFailure = { e ->
                    _orderListState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _orderListState.update { it.copy(isLoading = false) }
        }
    }

    private fun loadActiveOrders() {
        if (!_orderListState.value.hasMoreActive) return
        Log.i("LOAD ACTIVE ORDER", "LOADING")

        viewModelScope.launch {
            _orderListState.update { it.copy(isLoading = true) }
            delay(500)

            val result = requestUseCases.getUserJobPostings(
                status = JobStatus.ACTIVE,
                sortType = _orderListState.value.sortType,
                page = _orderListState.value.activePage,
                size = 7
            )

            result.fold(
                onSuccess = { pagedResponse ->
                    _orderListState.update {
                        it.copy(
                            hasMoreActive = pagedResponse.content.isNotEmpty(),
                            activeOrders = it.activeOrders + pagedResponse.content,
                            activePage = if (pagedResponse.content.isNotEmpty()) it.activePage+1 else it.activePage
                        )
                    }
                },
                onFailure = { e ->
                    _orderListState.update {
                        it.copy(
                            errorMessage = "Error: ${e.message}"
                        )
                    }
                }
            )

            _orderListState.update { it.copy(isLoading = false) }
        }
    }

    private fun loadEndedOrders() {
        if (!_orderListState.value.hasMoreEnded) return
        Log.i("LOAD ENDED ORDER", "LOADING")
        viewModelScope.launch {
            _orderListState.update { it.copy(isLoading = true) }

            val result = requestUseCases.getUserJobPostings(
                status = null,
                sortType = _orderListState.value.sortType,
                page = _orderListState.value.endedPage,
                size = 10
            )

            result.fold(
                onSuccess = { pagedResponse ->
                    _orderListState.update {
                        it.copy(
                            hasMoreEnded = pagedResponse.content.isNotEmpty(),
                            endedOrders = it.endedOrders + pagedResponse.content,
                            endedPage = if (pagedResponse.content.isNotEmpty()) it.endedPage+1 else it.endedPage
                        )
                    }
                },
                onFailure = { e ->
                    _orderListState.update {
                        it.copy(
                            errorMessage = "Error: ${e.message}"
                        )
                    }
                }
            )

            _orderListState.update { it.copy(isLoading = false) }
        }
    }
}
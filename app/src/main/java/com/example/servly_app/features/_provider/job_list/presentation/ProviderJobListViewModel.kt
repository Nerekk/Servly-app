package com.example.servly_app.features._provider.job_list.presentation

import android.util.Log
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.R
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.features._customer.job_create.data.dtos.CategoryInfo
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.features._customer.job_create.domain.usecase.CategoryUseCases
import com.example.servly_app.features._provider.job_list.domain.usecase.ProviderJobListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProviderJobListState(
    val isLoading: Boolean = false,

    val categories: List<CategoryInfo> = emptyList(),

    val sortType: SortType = SortType.DESCENDING,
    val searchQuery: String = "",
    val selectedCategories: List<Long> = emptyList(),
    val selectedDays: Long = 7,

    val hasMorePages: Boolean = true,
    val page: Int = 0,

    val totalElements: Int = 0,
    val jobs: List<JobPostingInfo> = emptyList(),

    val isSortSheetVisible: Boolean = false,
    val isCategorySheetVisible: Boolean = false,
    val isDateSheetVisible: Boolean = false,
    val isSearched: Boolean = false,

    val errorMessage: String? = null
) {
    fun getCategoryName(index: Long): String {
        val category = categories.find { it.id == index }
        return category?.name ?: "Unknown category"
    }

    companion object {
        const val PAGE_SIZE = 5

        val DATE_OPTIONS = listOf(
            1L to R.string.job_list_date_1d,
            3L to R.string.job_list_date_3d,
            7L to R.string.job_list_date_7d,
            30L to R.string.job_list_date_30d
        )
    }
}

@HiltViewModel
class ProviderJobListViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val providerJobListUseCases: ProviderJobListUseCases
): ViewModel() {
    private val _providerJobListState = MutableStateFlow(ProviderJobListState())
    val providerJobListState = _providerJobListState.asStateFlow()

    init {
        loadCategories(
            onSuccess = {
                loadJobs(false)
            }
        )
    }

    fun updateSelectedCategories(selectedCategories: List<Long>) {
        _providerJobListState.update { it.copy(selectedCategories = selectedCategories) }

        reapplyFiltersAndReloadJobs()
    }

    fun updateSortType(sortType: SortType) {
        _providerJobListState.update { it.copy(sortType = sortType) }

        reapplyFiltersAndReloadJobs()
    }

    fun updateDays(days: Long) {
        _providerJobListState.update { it.copy(selectedDays = days) }

        reapplyFiltersAndReloadJobs()
    }

    fun updateSearchQuery(query: String) {
        _providerJobListState.update { it.copy(searchQuery = query) }
    }

    fun updateSheetVisibility(isSortVisible: Boolean, isCategoryVisible: Boolean, isDateVisible: Boolean) {
        _providerJobListState.update {
            it.copy(
                isSortSheetVisible = isSortVisible,
                isCategorySheetVisible = isCategoryVisible,
                isDateSheetVisible = isDateVisible
            )
        }
    }

    fun reapplyFiltersAndReloadJobs(isQuery: Boolean? = null, isCancelQuery: Boolean? = null) {
        isCancelQuery?.let {
            if (_providerJobListState.value.isSearched) {
                _providerJobListState.update { it.copy(isSearched = false) }
            } else return
        }

        isQuery?.let {
            _providerJobListState.update { it.copy(isSearched = true) }
        }

        loadJobs(true)
    }

    private fun loadCategories(onSuccess: () -> Unit) {
        viewModelScope.launch {
            _providerJobListState.update { it.copy(isLoading = true) }

            val result = categoryUseCases.getCategories()

            result.fold(
                onSuccess = { list ->
                    _providerJobListState.update { it.copy(categories = list) }
                    onSuccess()
                },
                onFailure = { e ->
                    _providerJobListState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _providerJobListState.update { it.copy(isLoading = false) }
        }
    }

    fun loadJobs(hasAnyFilterChanged: Boolean) {
        if (hasAnyFilterChanged) {
            _providerJobListState.update {
                it.copy(
                    isLoading = true,
                    page = 0,
                    hasMorePages = true,
                    totalElements = 0,
                    jobs = emptyList()
                )
            }
        }
        if (!_providerJobListState.value.hasMorePages) return
        Log.i("LOAD JOBS", "LOADING")

        viewModelScope.launch {
            _providerJobListState.update { it.copy(isLoading = true) }
            delay(500)

            val result = providerJobListUseCases.getFilteredActiveJobs(
                sortType = _providerJobListState.value.sortType,
                page = _providerJobListState.value.page,
                size = ProviderJobListState.PAGE_SIZE,
                search = _providerJobListState.value.searchQuery.ifEmpty { null },
                categories = _providerJobListState.value.selectedCategories.ifEmpty { null },
                days = _providerJobListState.value.selectedDays
            )

            result.fold(
                onSuccess = { pagedResponse ->
                    _providerJobListState.update {
                        it.copy(
                            hasMorePages = pagedResponse.content.isNotEmpty(),
                            jobs = it.jobs + pagedResponse.content,
                            page = if (pagedResponse.content.isNotEmpty()) it.page+1 else it.page,
                            totalElements = pagedResponse.totalElements
                        )
                    }
                },
                onFailure = { e ->
                    _providerJobListState.update {
                        it.copy(
                            errorMessage = "Error: ${e.message}"
                        )
                    }
                }
            )

            _providerJobListState.update { it.copy(isLoading = false) }
        }
    }
}
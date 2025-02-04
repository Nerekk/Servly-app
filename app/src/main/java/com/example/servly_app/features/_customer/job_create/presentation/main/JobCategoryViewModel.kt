package com.example.servly_app.features._customer.job_create.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.features._customer.job_create.data.dtos.CategoryInfo
import com.example.servly_app.features._customer.job_create.domain.usecase.CategoryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class JobCategoryState(
    val isLoading: Boolean = false,
    val categories: List<CategoryInfo> = emptyList(),

    val errorMessage: String? = null
)

@HiltViewModel
class OffersViewModel @Inject constructor(
    private val categoryUseCases: CategoryUseCases
): ViewModel() {
    private val _jobCategoryState = MutableStateFlow(JobCategoryState())
    val jobCategoryState: StateFlow<JobCategoryState> = _jobCategoryState.asStateFlow()

    init {
        initCategories()
    }

    private fun initCategories() {
        viewModelScope.launch {
            _jobCategoryState.update { it.copy(isLoading = true) }

            val result = categoryUseCases.getCategories()

            result.fold(
                onSuccess = { list ->
                    _jobCategoryState.update { it.copy(categories = list) }
                },
                onFailure = { e ->
                    _jobCategoryState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _jobCategoryState.update { it.copy(isLoading = false) }
        }
    }
}
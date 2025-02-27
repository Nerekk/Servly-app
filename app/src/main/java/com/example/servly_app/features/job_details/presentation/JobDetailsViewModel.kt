package com.example.servly_app.features.job_details.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.domain.usecase.job_posting.JobPostingUseCases
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.features._customer.job_create.data.dtos.QuestionInfo
import com.example.servly_app.features._customer.job_create.domain.usecase.CategoryUseCases
import com.example.servly_app.features.job_details.presentation.details_card.JobDetails
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class JobDetailsState(
    val isLoading: Boolean = false,

    val jobPosting: JobPostingInfo,
    val questions: List<QuestionInfo> = emptyList(),

    val errorMessage: String? = null
) {
    fun toJobDetails(): JobDetails {
        return JobDetails(
            title = jobPosting.title,
            city = jobPosting.city,
            street = jobPosting.street,
            status = jobPosting.status,
            questions = questions,
            answers = jobPosting.answers
        )
    }
}

@HiltViewModel(assistedFactory = JobDetailsViewModel.OrderDetailsViewModelFactory::class)
class JobDetailsViewModel @AssistedInject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val jobPostingUseCases: JobPostingUseCases,
    @Assisted val order: JobPostingInfo
): ViewModel() {
    @AssistedFactory
    interface OrderDetailsViewModelFactory {
        fun create(order: JobPostingInfo): JobDetailsViewModel
    }

    private val _jobDetailsState = MutableStateFlow(JobDetailsState(jobPosting = order))
    val orderDetailsState = _jobDetailsState.asStateFlow()

    init {
        initQuestions(order.categoryId)
    }

    fun cancelJobPosting() {
        viewModelScope.launch {
            val result = jobPostingUseCases.updateJobStatus(_jobDetailsState.value.jobPosting.id!!, JobStatus.CANCELED)
            result.fold(
                onSuccess = { updatedJobPosting ->
                    _jobDetailsState.update { it.copy(jobPosting = updatedJobPosting) }
                },
                onFailure = { e ->
                    _jobDetailsState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )
        }
    }

    private fun initQuestions(categoryId: Long) {
        viewModelScope.launch {
            _jobDetailsState.update { it.copy(isLoading = true) }

            val result = categoryUseCases.getQuestions(categoryId)
            result.fold(
                onSuccess = { list ->
                    _jobDetailsState.update { it.copy(questions = list) }
                },
                onFailure = { e ->
                    _jobDetailsState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _jobDetailsState.update { it.copy(isLoading = false) }
        }
    }
}
package com.example.servly_app.features._customer.job_create.presentation.job_form

import android.util.Log
import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.features._customer.CustomerState
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.features._customer.job_create.data.dtos.QuestionAnswer
import com.example.servly_app.features._customer.job_create.data.dtos.QuestionInfo
import com.example.servly_app.features._customer.job_create.domain.usecase.CategoryUseCases
import com.example.servly_app.core.domain.usecase.job_posting.CreateJobPosting
import com.example.servly_app.core.domain.usecase.job_posting.JobPostingUseCases
import com.example.servly_app.features._customer.job_create.presentation.job_form.JobFormState.Companion.MAX_ANSWER_SIZE
import com.example.servly_app.features._customer.job_create.presentation.job_form.JobFormState.Companion.MAX_TITLE_SIZE
import com.example.servly_app.features._customer.job_create.presentation.main.components.JobCategory
import com.google.android.gms.maps.model.LatLng
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class JobFormState(
    val isLoading: Boolean = false,
    val isLoadingButton: Boolean = false,
    val categoryName: String = "",
    val categoryId: Long = -1,

    val title: String = "",


    val selectedAddress: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,

    val questions: List<QuestionInfo> = emptyList(),

    val answers: List<JobViewAnswer> = emptyList(),

    val errorMessage: String? = null,

    val titleError: String? = null,
    val addressError: String? = null,
) {
    fun toJobPostingInfo(categoryId: Long): JobPostingInfo {
        return JobPostingInfo(
            title = title,
            categoryId = categoryId,
            address = selectedAddress,
            latitude = latitude,
            longitude = longitude,
            answers = answers.map { it.toQuestionAnswer() },
            status = JobStatus.ACTIVE
        )
    }

    companion object {
        const val MAX_ANSWER_SIZE = 200
        const val MAX_TITLE_SIZE = 40
    }
}

data class JobViewAnswer(
    val id: Long,
    val answer: String = "",
    val errorMessage: String? = null
) {
    fun toQuestionAnswer(): QuestionAnswer { return QuestionAnswer(id, answer) }
}

@HiltViewModel(assistedFactory = JobFormViewModel.JobFormViewModelFactory::class)
class JobFormViewModel @AssistedInject constructor(
    private val categoryUseCases: CategoryUseCases,
    private val jobPostingUseCases: JobPostingUseCases,
    @Assisted val jobCategory: JobCategory
): ViewModel() {
    @AssistedFactory
    interface JobFormViewModelFactory {
        fun create(jobCategory: JobCategory): JobFormViewModel
    }

    private val _jobFormState = MutableStateFlow(JobFormState())
    val jobFormState: StateFlow<JobFormState> = _jobFormState.asStateFlow()

    init {
        _jobFormState.update { it.copy(categoryId = jobCategory.id, categoryName = jobCategory.name) }
        initQuestions(jobCategory)
    }

    fun updateAddress(address: String, latLng: LatLng) {
        _jobFormState.update { it.copy(
            selectedAddress = address,
            longitude = latLng.longitude,
            latitude = latLng.latitude
        ) }
    }

    fun updateTitle(title: String) {
        if (title.contains("\n") || title.length > MAX_TITLE_SIZE) return
        _jobFormState.update { it.copy(title = title) }
    }

    fun updateAnswer(questionId: Long, answer: String) {
        if (answer.length > MAX_ANSWER_SIZE) return
        _jobFormState.update {
            val updatedAnswers = it.answers.map { qa ->
                if (qa.id == questionId) qa.copy(answer = answer) else qa
            }
            it.copy(answers = updatedAnswers)
        }
    }

    fun autofillAddress(customerState: State<CustomerState>) {
        _jobFormState.update { it.copy(
            selectedAddress = customerState.value.address,
            longitude = customerState.value.longitude,
            latitude = customerState.value.latitude
        ) }
    }

    fun createJob(onSuccess: () -> Unit, onFailure: () -> Unit) {
        if (!validateInputs()) return

        viewModelScope.launch {
            _jobFormState.update { it.copy(isLoadingButton = true) }

            val result = jobPostingUseCases.createJobPosting(_jobFormState.value.toJobPostingInfo(jobCategory.id))
            result.fold(
                onSuccess = {
                    onSuccess()
                },
                onFailure = {
                    onFailure()
                }
            )

            _jobFormState.update { it.copy(isLoadingButton = false) }
        }
    }

    private fun validateInputs(): Boolean {
        val state = _jobFormState.value
        var isValid = true

        val titleError = when {
            state.title.isBlank() -> "Title cannot be empty"
            else -> null
        }

        val addressError = when {
            state.selectedAddress == null -> "Address cannot be empty"
            else -> null
        }

        val updatedAnswers = state.answers.map { answer ->
            if (answer.answer.length < 3) {
                answer.copy(errorMessage = "Answer must contains at least 3 characters")
            } else {
                answer.copy(errorMessage = null)
            }
        }

        val hasEmptyAnswers = state.answers.any { it.answer.isBlank() }

        if (titleError != null || hasEmptyAnswers || addressError != null) {
            isValid = false
        }

        _jobFormState.update {
            it.copy(
                titleError = titleError,
                answers = updatedAnswers,
                addressError = addressError
            )
        }
        Log.i("jobFormValidate", "$isValid")

        return isValid
    }

    private fun initQuestions(category: JobCategory) {
        viewModelScope.launch {
            _jobFormState.update { it.copy(isLoading = true) }

            val result = categoryUseCases.getQuestions(category.id)
            result.fold(
                onSuccess = { list ->
                    val answerList: List<JobViewAnswer> = list.map { question ->
                        JobViewAnswer(id = question.id)
                    }

                    _jobFormState.update { it.copy(questions = list, answers = answerList) }
                },
                onFailure = { e ->
                    _jobFormState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _jobFormState.update { it.copy(isLoading = false) }
        }
    }
}
package com.example.servly_app.features._customer.job_list.presentation.details_view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.features._customer.job_create.data.dtos.QuestionInfo
import com.example.servly_app.features._customer.job_create.domain.usecase.CategoryUseCases
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class OrderDetailsState(
    val isLoading: Boolean = false,

    val order: JobPostingInfo,
    val questions: List<QuestionInfo> = emptyList(),

    val errorMessage: String? = null
)

@HiltViewModel(assistedFactory = OrderDetailsViewModel.OrderDetailsViewModelFactory::class)
class OrderDetailsViewModel @AssistedInject constructor(
    private val categoryUseCases: CategoryUseCases,
    @Assisted val order: JobPostingInfo
): ViewModel() {
    @AssistedFactory
    interface OrderDetailsViewModelFactory {
        fun create(order: JobPostingInfo): OrderDetailsViewModel
    }

    private val _orderDetailsState = MutableStateFlow(OrderDetailsState(order = order))
    val orderDetailsState = _orderDetailsState.asStateFlow()

    init {
        initQuestions(order.categoryId)
    }

    private fun initQuestions(categoryId: Long) {
        viewModelScope.launch {
            _orderDetailsState.update { it.copy(isLoading = true) }

            val result = categoryUseCases.getQuestions(categoryId)
            result.fold(
                onSuccess = { list ->
                    _orderDetailsState.update { it.copy(questions = list) }
                },
                onFailure = { e ->
                    _orderDetailsState.update { it.copy(errorMessage = "Error: ${e.message}") }
                }
            )

            _orderDetailsState.update { it.copy(isLoading = false) }
        }
    }
}
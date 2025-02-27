package com.example.servly_app.features.reviews.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.domain.usecase.reviews.ReviewUseCases
import com.example.servly_app.features.reviews.data.ReviewInfo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ReviewListState(
    val isLoading: Boolean = false,

    val reviews: List<ReviewInfo> = emptyList(),

    val page: Int = 0,
    val totalPages: Int = 0,
)

@HiltViewModel(assistedFactory = ReviewListViewModel.ReviewListViewModelFactory::class)
class ReviewListViewModel @AssistedInject constructor(
    private val reviewUseCases: ReviewUseCases,
    @Assisted val userId: Long,
    @Assisted val role: Role
): ViewModel() {
    @AssistedFactory
    interface ReviewListViewModelFactory {
        fun create(userId: Long, role: Role): ReviewListViewModel
    }

    private val _reviewListState = MutableStateFlow(ReviewListState())
    val reviewListState = _reviewListState.asStateFlow()

    init {
        loadPage()
    }

    fun loadPage(page: Int = 0) {
        if (page > _reviewListState.value.totalPages) return

        if (role == Role.CUSTOMER) {
            loadReviewsForCustomer(page)
        } else {
            loadReviewsForProvider(page)
        }
    }

    private fun loadReviewsForCustomer(page: Int) {
        viewModelScope.launch {
            _reviewListState.update { it.copy(isLoading = true) }

            val result = reviewUseCases.getCustomerReviews(
                customerId = userId,
                page = page,
                size = 5
            )
            result.fold(
                onSuccess = { pagedReviews ->
                    Log.i("REVIEWS_LIST", "SUCCESS: ${pagedReviews.content}")
                    _reviewListState.update { it.copy(
                        reviews = pagedReviews.content,
                        page = page,
                        totalPages = pagedReviews.totalPages
                    ) }
                },
                onFailure = { e ->
                    Log.i("REVIEWS_LIST", "FAIL: ${e.message}")
                }
            )

            _reviewListState.update { it.copy(isLoading = false) }
        }
    }

    private fun loadReviewsForProvider(page: Int) {
        viewModelScope.launch {
            _reviewListState.update { it.copy(isLoading = true) }

            val result = reviewUseCases.getProviderReviews(
                providerId = userId,
                page = page,
                size = 5
            )
            result.fold(
                onSuccess = { pagedReviews ->
                    _reviewListState.update { it.copy(
                        reviews = pagedReviews.content,
                        page = page,
                        totalPages = pagedReviews.totalPages
                    ) }
                },
                onFailure = { e ->
                    Log.i("REVIEWS_LIST", "FAIL: ${e.message}")
                }
            )

            _reviewListState.update { it.copy(isLoading = false) }
        }
    }
}
package com.example.servly_app.features.reviews.presentation.form

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.domain.usecase.reviews.ReviewUseCases
import com.example.servly_app.features.reviews.data.ReviewFormData
import com.example.servly_app.features.reviews.data.ReviewInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ReviewFormViewModel @Inject constructor(
    private val reviewUseCases: ReviewUseCases
): ViewModel() {
    private val _formState = MutableStateFlow(ReviewFormData())
    val formState = _formState.asStateFlow()

    fun updateRating(rating: Int) {
        _formState.update { it.copy(
            rating = rating,
            isButtonEnabled = rating != 0 || it.review.isNotBlank()
        ) }
    }

    fun updateReview(review: String) {
        _formState.update { it.copy(
            review = review,
            isButtonEnabled = it.rating != 0 || review.isNotBlank()
        ) }
    }

    fun createReviewForProvider(jobRequestId: Long, onSuccess: (ReviewInfo) -> Unit) {
        viewModelScope.launch {

            _formState.update { it.copy(isLoading = true) }

            val result = reviewUseCases.createProviderReview(
                ReviewInfo(
                    jobRequestId = jobRequestId,
                    rating = _formState.value.rating,
                    review = _formState.value.review
                )
            )
            result.fold(
                onSuccess = { review ->
                    onSuccess(review)
                },
                onFailure = { e ->
                    Log.i("REVIEW_FORM", "FAIL ${e.message}")
                }
            )

            _formState.update { it.copy(isLoading = false) }
        }
    }

    fun createReviewForCustomer(jobRequestId: Long, onSuccess: (ReviewInfo) -> Unit) {
        viewModelScope.launch {
            _formState.update { it.copy(isLoading = true) }

            val result = reviewUseCases.createCustomerReview(
                ReviewInfo(
                    jobRequestId = jobRequestId,
                    rating = _formState.value.rating,
                    review = _formState.value.review
                )
            )
            result.fold(
                onSuccess = { review ->
                    onSuccess(review)
                },
                onFailure = { e ->
                    Log.i("REVIEW_FORM", "FAIL ${e.message}")
                }
            )

            _formState.update { it.copy(isLoading = false) }
        }
    }
}
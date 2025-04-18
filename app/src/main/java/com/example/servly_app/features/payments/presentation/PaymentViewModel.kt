package com.example.servly_app.features.payments.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.core.util.ErrorStore
import com.example.servly_app.features.payments.data.JobPaymentInfo
import com.example.servly_app.features.payments.data.PaymentStatus
import com.example.servly_app.features.payments.data.StripePaymentRequest
import com.example.servly_app.features.payments.data.StripePaymentResponse
import com.example.servly_app.features.payments.domain.PaymentUseCases
import com.stripe.android.paymentsheet.PaymentSheetResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PaymentState(
    val isLoading: Boolean = false,
    val jobPaymentInfo: JobPaymentInfo? = null,

    val dialogVisible: Boolean = false
)

data class StripeState(
    val isLoading: Boolean = false,
    val isTransaction: Boolean = false,
    val isDepositPayment: Boolean = false,
    val stripePaymentResponse: StripePaymentResponse? = null
)

data class PaymentParams(
    val jobRequestId: Long,
    val paymentId: Long?
)

@HiltViewModel(assistedFactory = PaymentViewModel.PaymentViewModelFactory::class)
class PaymentViewModel @AssistedInject constructor(
    private val paymentUseCases: PaymentUseCases,
    @Assisted val paymentParams: PaymentParams
): ViewModel() {
    @AssistedFactory
    interface PaymentViewModelFactory {
        fun create(paymentParams: PaymentParams): PaymentViewModel
    }

    private val _paymentState = MutableStateFlow(PaymentState())
    val paymentState = _paymentState.asStateFlow()

    private val _stripeState = MutableStateFlow(StripeState())
    val stripeState = _stripeState.asStateFlow()


    init {
        if (paymentParams.paymentId != null) {
            loadPayment(paymentParams.paymentId)
        }
    }

    fun updateDialogVisibility(isVisible: Boolean) {
        _paymentState.update { it.copy(dialogVisible = isVisible) }
    }

    fun updatePaymentType(isDepositPayment: Boolean) {
        _stripeState.update { it.copy(isDepositPayment = isDepositPayment) }
    }

    fun loadPayment(paymentId: Long) {
        viewModelScope.launch {
            _paymentState.update { it.copy(isLoading = true) }

            val result = paymentUseCases.getPayment(paymentId)
            result.fold(
                onSuccess = { payment ->
                    _paymentState.update { it.copy(jobPaymentInfo = payment) }
                },
                onFailure = { e ->
                    Log.d("PAYMENT_GET", e.message.toString())
                }
            )

            _paymentState.update { it.copy(isLoading = false) }
        }
    }

    private fun createPayment(jobPaymentInfo: JobPaymentInfo) {
        viewModelScope.launch {
            _paymentState.update { it.copy(isLoading = true) }

            val result = paymentUseCases.createPayment(jobPaymentInfo)
            result.fold(
                onSuccess = { payment ->
                    _paymentState.update { it.copy(jobPaymentInfo = payment) }
                },
                onFailure = { e ->
                    Log.d("PAYMENT_CREATE", e.message.toString())
                }
            )

            _paymentState.update { it.copy(isLoading = false) }
        }
    }

    private fun updatePayment(jobPaymentInfo: JobPaymentInfo) {
        viewModelScope.launch {
//            _paymentState.update { it.copy(isLoading = true) }

            val result = paymentUseCases.updatePayment(jobPaymentInfo)
            result.fold(
                onSuccess = { payment ->
                    _paymentState.update { it.copy(jobPaymentInfo = payment) }
                },
                onFailure = { e ->
                    Log.d("PAYMENT_UPDATE", e.message.toString())
                }
            )

//            _paymentState.update { it.copy(isLoading = false) }
        }
    }

    fun handleFormUpdate(totalAmount: Long, depositAmount: Long?) {
        if (_paymentState.value.jobPaymentInfo == null) {
            createPayment(
                JobPaymentInfo(
                    jobRequestId = paymentParams.jobRequestId,
                    totalAmount = totalAmount,
                    depositAmount = depositAmount
                )
            )
        } else {
            updatePayment(
                _paymentState.value.jobPaymentInfo!!.copy(
                    totalAmount = totalAmount,
                    depositAmount = depositAmount
                )
            )
        }
    }

    fun finishPaymentManually() {
        updatePayment(
            _paymentState.value.jobPaymentInfo!!.copy(
                paymentStatus = PaymentStatus.FULLY_PAID
            )
        )
    }

    fun createStripePaymentIntentForDeposit(onSuccess: (String) -> Unit) {
        if (_paymentState.value.jobPaymentInfo!!.depositAmount == null) return
        viewModelScope.launch {
            _stripeState.update { it.copy(isLoading = true) }
            _stripeState.update { it.copy(isTransaction = true) }

            val result = paymentUseCases.createIntent(
                StripePaymentRequest(
                    _paymentState.value.jobPaymentInfo!!.jobRequestId,
                    _paymentState.value.jobPaymentInfo!!.depositAmount!! * 100
                )
            )
            result.fold(
                onSuccess = { response ->
                    _stripeState.update { it.copy(stripePaymentResponse = response) }
                    onSuccess(response.clientSecret)
                },
                onFailure = { e ->
                    Log.d("STRIPE_INTENT", e.message.toString())
                }
            )

            _stripeState.update { it.copy(isLoading = false) }
        }

    }

    fun createStripePaymentIntentForFull(onSuccess: (String) -> Unit) {
        viewModelScope.launch {
            _stripeState.update { it.copy(isLoading = true) }
            _stripeState.update { it.copy(isTransaction = true) }

            val amountToPay = if (_paymentState.value.jobPaymentInfo!!.paymentStatus == PaymentStatus.DEPOSIT_PAID) {
                _paymentState.value.jobPaymentInfo!!.totalAmount - _paymentState.value.jobPaymentInfo!!.depositAmount!!
            } else {
                _paymentState.value.jobPaymentInfo!!.totalAmount
            }

            val result = paymentUseCases.createIntent(
                StripePaymentRequest(
                    _paymentState.value.jobPaymentInfo!!.jobRequestId,
                    amountToPay * 100
                )
            )
            result.fold(
                onSuccess = { response ->
                    _stripeState.update { it.copy(stripePaymentResponse = response) }
                    onSuccess(response.clientSecret)
                },
                onFailure = { e ->
                    Log.d("STRIPE_INTENT", e.message.toString())
                }
            )

            _stripeState.update { it.copy(isLoading = false) }
        }
    }

    fun onPaymentSheetResult(paymentSheetResult: PaymentSheetResult) {
        _stripeState.update { it.copy(isTransaction = false) }
        when(paymentSheetResult) {
            is PaymentSheetResult.Canceled -> {
                Log.d("STRIPE_RESULT","Canceled")
            }
            is PaymentSheetResult.Failed -> {
                Log.d("STRIPE_RESULT","Error: ${paymentSheetResult.error}")
                ErrorStore.addError("Error: ${paymentSheetResult.error}")
            }
            is PaymentSheetResult.Completed -> {
                Log.d("STRIPE_RESULT","Completed")
                if (_stripeState.value.isDepositPayment) {
                    updatePayment(
                        _paymentState.value.jobPaymentInfo!!.copy(
                            paymentStatus = PaymentStatus.DEPOSIT_PAID,
                            stripeDepositPaymentId = _stripeState.value.stripePaymentResponse?.paymentIntentId
                        )
                    )
                } else {
                    updatePayment(
                        _paymentState.value.jobPaymentInfo!!.copy(
                            paymentStatus = PaymentStatus.FULLY_PAID,
                            stripePaymentId = _stripeState.value.stripePaymentResponse?.paymentIntentId
                        )
                    )
                }
            }
        }
    }
}
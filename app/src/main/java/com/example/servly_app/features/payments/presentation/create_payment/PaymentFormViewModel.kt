package com.example.servly_app.features.payments.presentation.create_payment

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class PaymentFormState(
    val totalAmount: Long? = null,
    val depositAmount: Long? = null,

    val isButtonEnabled: Boolean = false
)

@HiltViewModel
class PaymentFormViewModel @Inject constructor(): ViewModel() {

    private val _paymentFormState = MutableStateFlow(PaymentFormState())
    val paymentFormState = _paymentFormState.asStateFlow()

    fun setEditState(totalAmount: Long?, depositAmount: Long?) {
        _paymentFormState.update { it.copy(totalAmount = totalAmount, depositAmount = depositAmount) }
        updateButtonStatus()
    }

    fun sendData(onClick: (Long, Long?) -> Unit) {
        if (_paymentFormState.value.totalAmount == null) return
        onClick(_paymentFormState.value.totalAmount!!, _paymentFormState.value.depositAmount)
    }

    fun updateTotalAmount(totalAmount: String) {
        if (totalAmount.isNotBlank()) {
            totalAmount.toLongOrNull()?.let {
                _paymentFormState.update { it.copy(totalAmount = totalAmount.toLong()) }
            }
        } else {
            _paymentFormState.update { it.copy(totalAmount = null) }
        }
        updateButtonStatus()
    }

    fun updateDepositAmount(depositAmount: String) {
        if (depositAmount.isNotBlank()) {
            depositAmount.toLongOrNull()?.let {
                _paymentFormState.update { it.copy(depositAmount = depositAmount.toLong()) }
            }
        } else {
            _paymentFormState.update { it.copy(depositAmount = null) }
        }
        updateButtonStatus()
    }

    private fun validData(): Boolean {
        val totalAmount = _paymentFormState.value.totalAmount
        val depositAmount = _paymentFormState.value.depositAmount
        val con1 = totalAmount != null
        if (!con1) return false

        val con2 = depositAmount?.let {
            it < totalAmount!!
        }
        return con2 ?: con1
    }

    fun updateButtonStatus() {
        _paymentFormState.update { it.copy(isButtonEnabled = validData()) }
    }

}
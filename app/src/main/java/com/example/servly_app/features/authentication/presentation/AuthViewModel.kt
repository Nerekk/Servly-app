package com.example.servly_app.features.authentication.presentation

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.features.authentication.domain.usecase.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {
    var phoneNumber by mutableStateOf("")
        private set

    var verificationCode by mutableStateOf("")
        private set

    var isValid by mutableStateOf(true)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var isUserLoggedIn by mutableStateOf(false)
        private set

    var verificationId by mutableStateOf<String?>(null)
    var errorMessage by mutableStateOf<String?>(null)

    init {
        isUserLoggedIn = authUseCases.checkUserLoggedIn.invoke()
    }

    fun updatePhoneNumber(newPhoneNumber: String) {
        phoneNumber = newPhoneNumber
    }

    fun updateVerificationCode(newVerificationCode: String) {
        if (isCodeInputValid(newVerificationCode))
            verificationCode = newVerificationCode
    }

    fun isPhoneNumberValid() : Boolean {
        isValid = phoneNumber.startsWith("+") && phoneNumber.length >= 9
        return isValid
    }

    fun isCodeInputValid(code: String) : Boolean {
        return code.length <= 6 && (code.isEmpty() || code.toIntOrNull() != null)
    }


    fun sendVerificationCode(activity: Activity) {
        isLoading = true
        viewModelScope.launch {
            val result = authUseCases.sendVerificationCode.invoke(activity, phoneNumber) { id ->
                verificationId = id
            }
            Log.i("SEND-VERIFY", "RESULT: ${result} VERIFICATION-ID: ${verificationId}")
            isLoading = false
            if (result.isFailure) {
                errorMessage = result.exceptionOrNull()?.localizedMessage
                Log.i("SEND-VERIFY", "ERROR: ${errorMessage} VERIFICATION-ID: ${verificationId}")
            }
        }
    }

    fun verifyCode() {
        val id = verificationId ?: return
        isLoading = true
        viewModelScope.launch {
            val result = authUseCases.verifyCode.invoke(id, verificationCode)
            isLoading = false
            Log.i("VERIFY", "RESULT: ${result} VERIFICATION-ID: ${verificationId}")
            if (result.isSuccess) {
                isUserLoggedIn = true
            } else {
                errorMessage = result.exceptionOrNull()?.localizedMessage
                Log.i("VERIFY", "ERROR: ${errorMessage} VERIFICATION-ID: ${verificationId}")

            }
        }
    }

    fun logout() {
        authUseCases.logout.invoke()
        isUserLoggedIn = false
    }

}
package com.example.servly_app.features.authentication.presentation

import android.content.Context
import android.credentials.GetCredentialException.TYPE_NO_CREDENTIAL
import android.util.Log
import android.widget.Toast
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.servly_app.BuildConfig
import com.example.servly_app.R
import com.example.servly_app.features.authentication.domain.usecase.AuthUseCases
import com.example.servly_app.features.authentication.presentation.login_view.AuthType
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",

    val isValid: Boolean = true,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,

    val isLoading: Boolean = false,
    val isUserLoggedIn: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {

    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState

    init {
        _authState.update { it.copy(isUserLoggedIn = authUseCases.checkUserLoggedIn.invoke()) }
    }

    fun updateEmail(email: String) {
        _authState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _authState.update { it.copy(password = password) }
    }

    fun updateConfirmPassword(confirmPassword: String) {
        _authState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun clearErrorMessage() {
        _authState.update { it.copy(errorMessage = null) }
    }

    fun validateInputs(isRegister: Boolean? = false): Boolean {
        val state = _authState.value
        var isValid = true

        val emailError = if (state.email.isEmpty()) {
            isValid = false
            "Email cannot be empty"
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(state.email).matches()) {
            isValid = false
            "Invalid email address"
        } else null

        val passwordError = if (state.password.length < 6) {
            isValid = false
            "Password must be at least 6 characters long"
        } else null

        val confirmPasswordError = if (isRegister == true && state.confirmPassword != state.password) {
            isValid = false
            "Passwords do not match"
        } else null

        _authState.update {
            it.copy(
                isValid = isValid,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError
            )
        }

        return isValid
    }

    fun signInWithEmail(email: String, password: String) {
        if (!validateInputs()) return
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authUseCases.signInWithEmail(email, password)
            result.onSuccess {
                Log.i("SIGN-IN", "SUCCESS")
                _authState.update { it.copy(isUserLoggedIn = true, isLoading = false) }
            }.onFailure { exception ->
                Log.i("SIGN-IN", "FAIL $exception")
                _authState.update { it.copy(isLoading = false, errorMessage = exception.message) }
            }
        }
    }

    fun signUpWithEmail(email: String, password: String) {
        if (!validateInputs(isRegister = true)) return
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authUseCases.signUpWithEmail(email, password)
            result.onSuccess {
                Log.i("SIGN-UP", "SUCCESS")
                _authState.update { it.copy(isUserLoggedIn = true, isLoading = false) }
            }.onFailure { exception ->
                Log.i("SIGN-UP", "FAIL $exception")
                _authState.update { it.copy(isLoading = false, errorMessage = exception.message) }
            }
        }
    }

    fun initGoogleDialog(context: Context) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, errorMessage = null) }

            val credentialManager = CredentialManager.create(context = context)
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.DEFAULT_WEB_CLIENT_ID)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )

                if (result.credential is CustomCredential && result.credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(result.credential.data)
                    signInWithGoogle(googleIdTokenCredential.idToken)
                } else {
                    _authState.update { it.copy(isLoading = false, errorMessage = context.getString(R.string.google_error1)) }
                }
            } catch (e: Exception) {
                when (e) {
                    is GetCredentialCancellationException -> _authState.update { it.copy(isLoading = false) }
                    is GetCredentialException -> {
                        if (e.type == TYPE_NO_CREDENTIAL) {
                            _authState.update { it.copy(isLoading = false, errorMessage = context.getString(R.string.google_error2)) }
                        }
                    }
                    else -> {
                        _authState.update { it.copy(isLoading = false, errorMessage = e.message) }
                    }
                }
                e.printStackTrace()
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _authState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = authUseCases.signInWithGoogle(idToken)
            result.onSuccess {
                Log.i("GOOGLE", "SUCCESS")
                _authState.update { it.copy(isUserLoggedIn = true, isLoading = false) }
            }.onFailure { exception ->
                Log.i("GOOGLE", "FAIL $exception")
                _authState.update { it.copy(isLoading = false, errorMessage = exception.message) }
            }
        }
    }
}
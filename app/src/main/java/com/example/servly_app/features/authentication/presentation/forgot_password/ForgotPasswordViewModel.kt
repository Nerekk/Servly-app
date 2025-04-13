package com.example.servly_app.features.authentication.presentation.forgot_password

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.servly_app.core.util.ErrorStore
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(): ViewModel() {
    private val _emailState = MutableStateFlow("")
    val emailState = _emailState.asStateFlow()

    fun updateEmail(email: String) {
        _emailState.value = email
    }

    fun sendPasswordResetLink(context: Context, onSuccess: () -> Unit, success: String, failed: String) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(_emailState.value)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "$success ${_emailState.value}", Toast.LENGTH_SHORT).show()
                    onSuccess()
                } else {
                    Toast.makeText(context, failed, Toast.LENGTH_SHORT).show()
                    ErrorStore.addError("${task.exception?.localizedMessage}")
                }
            }
    }
}
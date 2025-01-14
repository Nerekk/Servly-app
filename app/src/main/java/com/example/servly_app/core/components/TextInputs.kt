package com.example.servly_app.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun PhoneNumberInput(
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    isValid: Boolean
) {
    OutlinedTextField(
        value = phoneNumber,
        onValueChange = { newValue ->
            onPhoneNumberChange(newValue.filter { it.isDigit() || it == '+' })
        },
        label = { Text("Numer telefonu") },
        placeholder = { Text("+48123456789") },
        isError = !isValid,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Phone
        ),
        modifier = Modifier
            .fillMaxWidth(),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Phone,
                contentDescription = "Phone icon"
            )
        },
        supportingText = {
            if (!isValid) {
                Text(
                    text = "Nieprawidłowy numer telefonu",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}

@Composable
fun EmailTextField(
    email: String,
    updateEmail: (String) -> Unit,
    errorMessage: String?
) {
    OutlinedTextField(
        value = email,
        onValueChange = { updateEmail(it) },
        label = { Text("Email") },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = errorMessage != null,
        supportingText = {
            errorMessage?.let {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}

@Composable
fun PasswordTextField(
    password: String,
    updatePassword: (String) -> Unit,
    label: String,
    errorMessage: String?
) {
    OutlinedTextField(
        value = password,
        onValueChange = { updatePassword(it) },
        label = { Text(label) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        isError = errorMessage != null,
        supportingText = {
            errorMessage?.let {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    )
}


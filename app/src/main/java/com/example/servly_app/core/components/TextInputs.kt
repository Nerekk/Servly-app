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



package com.example.servly_app.core.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.servly_app.R
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar

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
        label = { Text(stringResource(R.string.role_form_field_phone_number)) },
        placeholder = { Text("+48111222333") },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    date: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier
) {
    val datePickerState = rememberDatePickerState()
    var showDatePicker by remember { mutableStateOf(false) }
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")

    OutlinedTextField(
        value = date?.format(formatter) ?: "",
        onValueChange = {},
        label = { Text(label) },
        modifier = modifier
            .clickable { showDatePicker = true },
        readOnly = true,
        trailingIcon = {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "date",
                modifier = Modifier.clickable { showDatePicker = true }
            )
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate()
                        onDateSelected(selectedDate)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Anuluj")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}


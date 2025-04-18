package com.example.servly_app.features.payments.presentation.create_payment

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.servly_app.R
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.payments.data.JobPaymentInfo

@Preview(
    showBackground = true,
    locale = "en",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "DefaultPreviewLightEN"
)
@Preview(
    showBackground = true,
    locale = "pl",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "DefaultPreviewDarkPL"
)
@Composable
fun PreviewPaymentView() {
    AppTheme {
        PaymentFormView(null, {it1, it2 ->})
    }
}

@Composable
fun PaymentFormView(jobPaymentInfo: JobPaymentInfo? = null, onClick: (Long, Long?) -> Unit) {
    val viewModel: PaymentFormViewModel = hiltViewModel()

    if (jobPaymentInfo != null) {
        viewModel.setEditState(jobPaymentInfo.totalAmount, jobPaymentInfo.depositAmount)
    }

    val state = viewModel.paymentFormState.collectAsState()

    BasicScreenLayout {
        Column(
            Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.create_payment),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            OutlinedTextField(
                value = state.value.totalAmount?.let {
                    "$it zł"
                } ?: run {""},
                onValueChange = { viewModel.updateTotalAmount(it.removeSuffix(" zł")) },
                label = {
                    Text(stringResource(R.string.total_amount))
                },
                placeholder = {
                    Text(stringResource(R.string.total_amount))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )

            OutlinedTextField(
                value = state.value.depositAmount?.let {
                    "$it zł"
                } ?: run {""},
                onValueChange = { viewModel.updateDepositAmount(it.removeSuffix(" zł")) },
                label = {
                    Text(stringResource(R.string.optional_deposit))
                },
                placeholder = {
                    Text(stringResource(R.string.optional_deposit))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number
                )
            )

            Button(
                onClick = { viewModel.sendData(onClick) },
                enabled = state.value.isButtonEnabled,
                modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
            ) {
                Text(stringResource(R.string.save))
            }
        }
    }
}
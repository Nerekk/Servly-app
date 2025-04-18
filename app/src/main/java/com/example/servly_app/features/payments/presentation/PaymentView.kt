package com.example.servly_app.features.payments.presentation

import android.content.res.Configuration
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.servly_app.BuildConfig
import com.example.servly_app.R
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.core.util.EnumUtils
import com.example.servly_app.features.job_details.presentation.schedule_dialog.ScheduleDialog
import com.example.servly_app.features.payments.data.JobPaymentInfo
import com.example.servly_app.features.payments.data.PaymentStatus
import com.example.servly_app.features.payments.presentation.create_payment.PaymentFormView
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.rememberPaymentSheet


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
        PaymentView(Role.CUSTOMER, 1,"Tytul pracy", 1)
    }
}

@Composable
fun PaymentView(role: Role, jobRequestId: Long, jobTitle: String, paymentId: Long? = null) {
    val viewModel: PaymentViewModel = hiltViewModel<PaymentViewModel, PaymentViewModel.PaymentViewModelFactory> { factory ->
        factory.create(PaymentParams(jobRequestId, paymentId))
    }
    val state = viewModel.paymentState.collectAsState()

    if (state.value.isLoading) {
        LoadingScreen()
        return
    }

    val context = LocalContext.current
    PaymentConfiguration.init(context, BuildConfig.STRIPE_PUBLIC_KEY)

    val stripeState = viewModel.stripeState.collectAsState()
    val paymentSheet = rememberPaymentSheet(viewModel::onPaymentSheetResult)

    if (state.value.dialogVisible) {
        ScheduleDialog(
            showDialog = state.value.dialogVisible,
            onDismiss = { viewModel.updateDialogVisibility(false) }
        ) {
            PaymentFormView(
                state.value.jobPaymentInfo,
                onClick = { totalAmount, depositAmount ->
                    viewModel.handleFormUpdate(totalAmount, depositAmount)
                    viewModel.updateDialogVisibility(false)
                }
            )
        }
    }

    BasicScreenLayout {
        Column {
            if (state.value.jobPaymentInfo == null) {
                if (role == Role.PROVIDER) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, MaterialTheme.colorScheme.onBackground, RoundedCornerShape(20))
                            .clickable { viewModel.updateDialogVisibility(true) }
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)

                            Text(
                                text = stringResource(R.string.create_payment),
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                    }
                } else {
                    Text(
                        text = stringResource(R.string.no_payments),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }

                return@Column
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.service_payment),
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = jobTitle,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.total_amount),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = "${state.value.jobPaymentInfo!!.totalAmount} zł",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = stringResource(R.string.deposit),
                            style = MaterialTheme.typography.bodyMedium
                        )

                        Text(
                            text = state.value.jobPaymentInfo!!.depositAmount?.let {
                                "$it zł"
                            } ?: run { "Brak" },
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    HorizontalDivider(Modifier.padding(vertical = 8.dp))

                    if (state.value.jobPaymentInfo!!.paymentStatus != PaymentStatus.FULLY_PAID) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = stringResource(R.string.remaining_balance)
                            )

                            Text(
                                text = if (state.value.jobPaymentInfo!!.paymentStatus == PaymentStatus.PENDING) {
                                    "${state.value.jobPaymentInfo!!.totalAmount} zł"
                                } else if (state.value.jobPaymentInfo!!.depositAmount != null) {
                                    "${state.value.jobPaymentInfo!!.totalAmount - state.value.jobPaymentInfo!!.depositAmount!!} zł"
                                } else "",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Status",
                            style = MaterialTheme.typography.titleMedium
                        )

                        Text(
                            text = stringResource(EnumUtils.getStatusString(state.value.jobPaymentInfo!!.paymentStatus)),
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }


            when (role) {
                Role.CUSTOMER -> {
                    if (stripeState.value.isLoading || stripeState.value.isTransaction) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            CircularProgressIndicator()
                            return@Column
                        }
                    }

                    if (state.value.jobPaymentInfo!!.depositAmount != null && state.value.jobPaymentInfo!!.paymentStatus == PaymentStatus.PENDING) {
                        OutlinedButton(
                            onClick = {
                                viewModel.updatePaymentType(true)
                                viewModel.createStripePaymentIntentForDeposit(
                                    onSuccess = { clientSecret -> presentPaymentSheet(paymentSheet, clientSecret) }
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.pay_deposit))
                        }
                    }

                    if (state.value.jobPaymentInfo!!.paymentStatus != PaymentStatus.FULLY_PAID) {
                        Button(
                            onClick = {
                                viewModel.updatePaymentType(false)
                                viewModel.createStripePaymentIntentForFull(
                                    onSuccess = { clientSecret -> presentPaymentSheet(paymentSheet, clientSecret) }
                                )
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.pay_in_full))
                        }
                    }
                }
                Role.PROVIDER -> {
                    if (state.value.jobPaymentInfo!!.paymentStatus == PaymentStatus.PENDING) {
                        OutlinedButton(
                            onClick = { viewModel.updateDialogVisibility(true) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.edit))
                        }
                    }

                    if (state.value.jobPaymentInfo!!.paymentStatus == PaymentStatus.PENDING ||
                        state.value.jobPaymentInfo!!.paymentStatus == PaymentStatus.DEPOSIT_PAID) {
                        Button(
                            onClick = { viewModel.finishPaymentManually() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(R.string.mark_as_paid))
                        }
                    }
                }
                else -> {}
            }

        }
    }
}

private fun presentPaymentSheet(
    paymentSheet: PaymentSheet,
    paymentIntentClientSecret: String
) {
    paymentSheet.presentWithPaymentIntent(
        paymentIntentClientSecret,
        PaymentSheet.Configuration(
            merchantDisplayName = "Servly",
            allowsDelayedPaymentMethods = true
        )
    )
}
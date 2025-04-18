package com.example.servly_app.features.job_details.presentation.header

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.components.ConfirmableButton
import com.example.servly_app.core.components.ConfirmableOutlinedButton
import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.data.util.ScheduleStatus
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.core.util.EnumUtils
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.features.job_details.data.JobRequestInfo
import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import com.example.servly_app.features.role_selection.data.ProviderInfo

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
fun PreviewChosenProvider() {
    AppTheme {
        val provider = ProviderInfo(
            name = "Tomasz",
            phoneNumber = "+48234123456",
            address = "Warszawa",
            rangeInKm = 30.0
        )

        val schedule = ScheduleInfo(

        )

        val jobPosting = JobPostingInfo(
            customerName = "Roman",
            title = "Praca",
            categoryId = 0,
            address = "",
            answers = emptyList(),
            status = JobStatus.ACTIVE
        )
        val job = JobRequestInfo()

        ProviderStatusSection(
            jobPostingInfo = jobPosting,
            jobRequestInfo = job, schedule, {} ,{}, {} ,{}, {i1, i2, i3 ->}
        )
    }
}

@Composable
fun ProviderStatusSection(
    jobPostingInfo: JobPostingInfo,
    jobRequestInfo: JobRequestInfo?,
    schedule: ScheduleInfo? = null,
    showSchedule: () -> Unit,
    createSchedule: () -> Unit,
    showProfile: () -> Unit,
    openChat: () -> Unit,
    onPaymentClick: (Long, String, Long?) -> Unit,
    sendFinishRequest: (() -> Unit)? = null,
    rejectFinishRequest: (() -> Unit)? = null,
    finishJob: (() -> Unit)? = null,
    cancelJob: (() -> Unit)? = null,
    acceptJob: (() -> Unit)? = null,
    withdrawnJob: (() -> Unit)? = null
) {
    val validScheduleStatuses = setOf(
        JobRequestStatus.IN_PROGRESS,
        JobRequestStatus.WAITING_FOR_PROVIDER_COMPLETE,
        JobRequestStatus.WAITING_FOR_CUSTOMER_COMPLETE,
        JobRequestStatus.COMPLETED,
        JobRequestStatus.CANCELED_IN_PROGRESS_BY_CUSTOMER,
        JobRequestStatus.CANCELED_IN_PROGRESS_BY_PROVIDER
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = stringResource(R.string.profile_customer),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(4.dp))
            HeaderCustomerInfo(jobPostingInfo, showProfile, jobRequestInfo, openChat)

            Spacer(modifier = Modifier.height(16.dp))

            HeaderProviderStatuses(jobRequestInfo, schedule, jobPostingInfo)

            if (jobRequestInfo == null) {
                return@Column
            }


            Spacer(modifier = Modifier.height(16.dp))

            if (jobRequestInfo.jobRequestStatus == JobRequestStatus.WAITING_FOR_PROVIDER_APPROVE) {
                acceptJob?.let {
                    InfoTextField(
                        text = stringResource(R.string.info_job_provider_selected)
                    )

                    ConfirmableButton(
                        onConfirmed = { acceptJob() },
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    ) {
                        Text(stringResource(R.string.details_button_job_accept))
                    }

//                    Button(
//                        onClick = { acceptJob() },
//                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
//                    ) {
//                        Text(stringResource(R.string.details_button_job_accept))
//                    }

                    withdrawnJob?.let {
                        ConfirmableOutlinedButton(
                            onConfirmed = { withdrawnJob() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.details_button_job_withdrawn),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                        
//                        OutlinedButton(
//                            onClick = { withdrawnJob() },
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            Text(
//                                text = stringResource(R.string.details_button_job_withdrawn),
//                                color = MaterialTheme.colorScheme.error
//                            )
//                        }
                    }
                }
            }

            if (jobRequestInfo.jobRequestStatus == JobRequestStatus.ACTIVE) {
                withdrawnJob?.let {
                    InfoTextField(
                        text = stringResource(R.string.info_job_provider_await_selection)
                    )

                    ConfirmableOutlinedButton(
                        onConfirmed = { withdrawnJob() },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.details_button_job_withdrawn),
                            color = MaterialTheme.colorScheme.error
                        )
                    }

//                    OutlinedButton(
//                        onClick = { withdrawnJob() },
//                        modifier = Modifier.fillMaxWidth()
//                    ) {
//                        Text(
//                            text = stringResource(R.string.details_button_job_withdrawn),
//                            color = MaterialTheme.colorScheme.error
//                        )
//                    }
                }
            }


            if (jobRequestInfo.jobRequestStatus in validScheduleStatuses) {
                if (schedule?.scheduleStatus == ScheduleStatus.WAITING_FOR_CUSTOMER_APPROVAL ||
                    schedule?.scheduleStatus == ScheduleStatus.UPDATED_WAITING_FOR_CUSTOMER_APPROVAL) {
                    InfoTextField(
                        text = stringResource(R.string.info_job_schedule_action)
                    )
                }
                schedule?.let {
                    OutlinedButton(
                        onClick = showSchedule,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.details_button_schedule_show))
                    }
                } ?: run {
                    OutlinedButton(
                        onClick = createSchedule,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.details_button_schedule_create))
                    }
                }


                Spacer(modifier = Modifier.height(16.dp))
                InfoTextField(
                    text = if (jobRequestInfo.jobPayment == null) {
                        stringResource(R.string.payments)
                    } else {
                        stringResource(EnumUtils.getStatusString(jobRequestInfo.jobPayment.paymentStatus))
                    }
                )
                Button(
                    onClick = { onPaymentClick(jobRequestInfo.id!!, jobRequestInfo.jobPostingInfo!!.title, jobRequestInfo.jobPayment?.id) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.payments))
                }
            }

            if (jobRequestInfo.jobRequestStatus == JobRequestStatus.IN_PROGRESS) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    sendFinishRequest?.let {
                        ConfirmableButton(
                            onConfirmed = { sendFinishRequest() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.details_button_job_finish_request))
                        }
                        
//                        Button(
//                            onClick = { sendFinishRequest() },
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            Text(stringResource(R.string.details_button_job_finish_request))
//                        }
                    }

                    cancelJob?.let {
                        ConfirmableOutlinedButton(
                            onConfirmed = { cancelJob() },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.details_button_job_cancel),
                                color = MaterialTheme.colorScheme.error
                            )
                        }

//                        OutlinedButton(
//                            onClick = { cancelJob() },
//                            modifier = Modifier.fillMaxWidth()
//                        ) {
//                            Text(
//                                text = stringResource(R.string.details_button_job_cancel),
//                                color = MaterialTheme.colorScheme.error
//                            )
//                        }
                    }
                }
            }

            if (jobRequestInfo.jobRequestStatus == JobRequestStatus.WAITING_FOR_PROVIDER_COMPLETE) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                InfoTextField(
                    text = stringResource(R.string.info_job_customer_finish)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    rejectFinishRequest?.let {
                        ConfirmableOutlinedButton(
                            onConfirmed = { rejectFinishRequest() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.details_button_reject),
                                color = MaterialTheme.colorScheme.error
                            )
                        }

//                        OutlinedButton(
//                            onClick = { rejectFinishRequest() },
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            Text(
//                                text = stringResource(R.string.details_button_reject),
//                                color = MaterialTheme.colorScheme.error
//                            )
//                        }
                    }

                    finishJob?.let {
                        ConfirmableButton(
                            onConfirmed = { finishJob() },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(stringResource(R.string.details_button_finish))
                        }

//                        Button(
//                            onClick = { finishJob() },
//                            modifier = Modifier.weight(1f)
//                        ) {
//                            Text(stringResource(R.string.details_button_finish))
//                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun HeaderProviderStatuses(
    jobRequestInfo: JobRequestInfo?,
    schedule: ScheduleInfo?,
    jobPostingInfo: JobPostingInfo
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        jobRequestInfo?.let {
            Column {
                Text(
                    text = stringResource(R.string.details_job_request_status),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(EnumUtils.getStatusString(jobRequestInfo.jobRequestStatus)),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )

                Text(
                    text = stringResource(R.string.details_schedule),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = if (schedule != null) {
                        stringResource(EnumUtils.getStatusString(schedule.scheduleStatus))
                    } else {
                        stringResource(R.string.details_schedule_none)
                    },
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        } ?: run {
            Column {
                Text(
                    text = stringResource(R.string.details_job_status),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = stringResource(EnumUtils.getStatusString(jobPostingInfo.status)),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}

@Composable
private fun HeaderCustomerInfo(
    jobPostingInfo: JobPostingInfo,
    showProfile: () -> Unit,
    jobRequestInfo: JobRequestInfo?,
    openChat: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.test_square_image_large),
                contentDescription = "avatar",
                modifier = Modifier
                    .size(44.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = jobPostingInfo.customerName!!,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }


        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.details_show_profile),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable { showProfile() }
            )
        }
    }
}
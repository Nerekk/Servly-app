package com.example.servly_app.features.job_details.presentation.core

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.features.job_details.presentation.JobDetailsState
import com.example.servly_app.features.job_details.presentation.JobRequestDetailsState
import com.example.servly_app.features.job_details.presentation.details_card.JobDetailsCard
import com.example.servly_app.features.job_details.presentation.header.InfoTextField
import com.example.servly_app.features.job_details.presentation.header.ProviderStatusSection
import com.example.servly_app.features.reviews.data.ReviewInfo
import com.example.servly_app.features.reviews.presentation.ReviewCard
import com.example.servly_app.features.reviews.presentation.form.ReviewFormView

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderDetailsContentForProvider(
    jobState: State<JobDetailsState>,
    jobRequestState: State<JobRequestDetailsState>,
    createJobRequest: (Long) -> Unit,
    withdrawnJobRequest: (Long) -> Unit,
    acceptJob: (Long) -> Unit,
    sendFinishRequest: () -> Unit,
    rejectFinishRequest: () -> Unit,
    finishJob: () -> Unit,
    cancelJob: () -> Unit,

    openChat: (Long) -> Unit,
    showProfile: (Long) -> Unit,
    createSchedule: () -> Unit,
    showSchedule: () -> Unit,

    onCreatedReview: (ReviewInfo) -> Unit,
    onPaymentClick: (Long, String, Long?) -> Unit
) {
    Scaffold(
        floatingActionButton = {
            jobRequestState.value.providersJobRequest?.let {
                if (it.jobRequestStatus != JobRequestStatus.WITHDRAWN &&
                    it.jobRequestStatus != JobRequestStatus.REJECTED) {
                    FloatingActionButton(
                        onClick = { openChat(it.id!!) }
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.chat_24px),
                            contentDescription = "Chat"
                        )
                    }
                }
            }
        }
    ) {
        BasicScreenLayout {
            if (jobState.value.isLoading) {
                LoadingScreen()
                return@BasicScreenLayout
            }
            LazyColumn(
                contentPadding = PaddingValues(bottom = 16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        when (jobState.value.jobPosting.status) {
                            JobStatus.ACTIVE -> {
                                ProviderStatusSection(
                                    jobPostingInfo = jobRequestState.value.providersJobRequest?.jobPostingInfo
                                        ?: jobState.value.jobPosting,
                                    jobRequestInfo = jobRequestState.value.providersJobRequest,
                                    schedule = jobRequestState.value.schedule,
                                    showSchedule = showSchedule,
                                    createSchedule = createSchedule,
                                    showProfile = { showProfile(jobState.value.jobPosting.customerId!!) },
                                    openChat = {  },
                                    onPaymentClick = onPaymentClick,
                                    acceptJob = { acceptJob(jobRequestState.value.providersJobRequest!!.id!!) },
                                    withdrawnJob = { withdrawnJobRequest(jobRequestState.value.providersJobRequest!!.id!!) }
                                )
                            }

                            JobStatus.CANCELED -> {
                                ProviderStatusSection(
                                    jobPostingInfo = jobRequestState.value.providersJobRequest?.jobPostingInfo
                                        ?: jobState.value.jobPosting,
                                    jobRequestInfo = jobRequestState.value.providersJobRequest,
                                    schedule = jobRequestState.value.schedule,
                                    showSchedule = showSchedule,
                                    createSchedule = createSchedule,
                                    showProfile = { showProfile(jobState.value.jobPosting.customerId!!) },
                                    openChat = {  },
                                    onPaymentClick = onPaymentClick
                                )
                            }

                            JobStatus.COMPLETED -> {
                                ProviderStatusSection(
                                    jobPostingInfo = jobRequestState.value.providersJobRequest?.jobPostingInfo
                                        ?: jobState.value.jobPosting,
                                    jobRequestInfo = jobRequestState.value.providersJobRequest,
                                    schedule = jobRequestState.value.schedule,
                                    showSchedule = showSchedule,
                                    createSchedule = createSchedule,
                                    showProfile = { showProfile(jobState.value.jobPosting.customerId!!) },
                                    openChat = {  },
                                    onPaymentClick = onPaymentClick
                                )
                            }

                            JobStatus.IN_PROGRESS -> {
                                ProviderStatusSection(
                                    jobPostingInfo = jobRequestState.value.providersJobRequest?.jobPostingInfo
                                        ?: jobState.value.jobPosting,
                                    jobRequestInfo = jobRequestState.value.providersJobRequest,
                                    schedule = jobRequestState.value.schedule,
                                    showSchedule = showSchedule,
                                    createSchedule = createSchedule,
                                    showProfile = { showProfile(jobState.value.jobPosting.customerId!!) },
                                    openChat = {  },
                                    onPaymentClick = onPaymentClick,
                                    sendFinishRequest = sendFinishRequest,
                                    rejectFinishRequest = rejectFinishRequest,
                                    finishJob = finishJob,
                                    cancelJob = cancelJob
                                )
                            }
                        }

                        if (jobRequestState.value.providersJobRequest?.jobRequestStatus == JobRequestStatus.CANCELED_IN_PROGRESS_BY_CUSTOMER ||
                            jobRequestState.value.providersJobRequest?.jobRequestStatus == JobRequestStatus.COMPLETED) {

                            jobRequestState.value.providersJobRequest?.providerReview?.let {
                                ReviewCard(it)
                            } ?: run {
                                InfoTextField(stringResource(R.string.rate_this_job))
                                ReviewFormView(
                                    role = Role.PROVIDER,
                                    jobRequestId = jobRequestState.value.providersJobRequest!!.id!!,
                                    jobRequestStatus = jobRequestState.value.providersJobRequest!!.jobRequestStatus,
                                    onSuccess = onCreatedReview
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                        JobDetailsCard(
                            details = jobState.value.toJobDetails()
                        )

                        if (jobRequestState.value.providersJobRequest == null) {
                            Button(
                                onClick = {
                                    createJobRequest(jobState.value.jobPosting.id!!)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.create_request),
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
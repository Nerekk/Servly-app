package com.example.servly_app.features.job_details.presentation.core

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import com.example.servly_app.core.components.ConfirmableOutlinedButton
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.features.job_details.data.JobRequestInfo
import com.example.servly_app.features.job_details.presentation.JobDetailsState
import com.example.servly_app.features.job_details.presentation.JobRequestDetailsState
import com.example.servly_app.features.job_details.presentation.components.BottomSheetProviderOperations
import com.example.servly_app.features.job_details.presentation.details_card.JobDetailsCard
import com.example.servly_app.features.job_details.presentation.header.CustomerStatusSection
import com.example.servly_app.features.job_details.presentation.header.InfoTextField
import com.example.servly_app.features.job_details.presentation.header.InterestedProviders
import com.example.servly_app.features.reviews.data.ReviewInfo
import com.example.servly_app.features.reviews.presentation.ReviewCard
import com.example.servly_app.features.reviews.presentation.form.ReviewFormView
import com.example.servly_app.features.role_selection.data.ProviderInfo

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OrderDetailsContentForCustomer(
    jobState: State<JobDetailsState>,
    cancelJobPosting: () -> Unit,
    jobRequestState: State<JobRequestDetailsState>,
    selectBottomSheetProvider: (JobRequestInfo?) -> Unit,
    sendFinishRequest: () -> Unit,
    rejectFinishRequest: () -> Unit,
    finishJob: () -> Unit,
    cancelJob: () -> Unit,

    showProfile: (Long) -> Unit,
    openChat: (Long) -> Unit,
    selectJobProvider: (JobRequestInfo) -> Unit,

    showSchedule: () -> Unit,
    onCreatedReview: (ReviewInfo) -> Unit
) {

    Scaffold(
        floatingActionButton = {
            jobRequestState.value.selectedJobRequest?.let {
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
                        when (jobRequestState.value.selectedJobRequest?.jobPostingInfo?.status
                            ?: jobState.value.jobPosting.status) {
                            JobStatus.ACTIVE -> {
                                InterestedProviders(jobRequestState, selectBottomSheetProvider)
                            }

                            JobStatus.CANCELED -> {
                                jobRequestState.value.selectedJobRequest?.let {
                                    CustomerStatusSection(
                                        jobRequestState.value.selectedJobRequest!!,
                                        showProfile = { showProfile(jobRequestState.value.selectedJobRequest!!.provider!!.providerId!!) },
                                        schedule = jobRequestState.value.schedule,
                                        showSchedule = showSchedule,
                                        openChat = {  }
                                    )
                                }
                            }

                            JobStatus.COMPLETED -> {
                                jobRequestState.value.selectedJobRequest?.let {
                                    CustomerStatusSection(
                                        jobRequestState.value.selectedJobRequest!!,
                                        showProfile = { showProfile(jobRequestState.value.selectedJobRequest!!.provider!!.providerId!!) },
                                        schedule = jobRequestState.value.schedule,
                                        showSchedule = showSchedule,
                                        openChat = {  }
                                    )
                                }
                            }

                            JobStatus.IN_PROGRESS -> {
                                jobRequestState.value.selectedJobRequest?.let {
                                    CustomerStatusSection(
                                        jobRequestState.value.selectedJobRequest!!,
                                        showProfile = { showProfile(jobRequestState.value.selectedJobRequest!!.provider!!.providerId!!) },
                                        schedule = jobRequestState.value.schedule,
                                        showSchedule = showSchedule,
                                        openChat = {  },
                                        sendFinishRequest = sendFinishRequest,
                                        rejectFinishRequest = rejectFinishRequest,
                                        finishJob = finishJob,
                                        cancelJob = cancelJob
                                    )
                                }
                            }
                        }

                        if (jobRequestState.value.selectedJobRequest?.jobRequestStatus == JobRequestStatus.CANCELED_IN_PROGRESS_BY_PROVIDER ||
                            jobRequestState.value.selectedJobRequest?.jobRequestStatus == JobRequestStatus.COMPLETED) {

                            jobRequestState.value.selectedJobRequest?.customerReview?.let {
                                ReviewCard(it)
                            } ?: run {
                                InfoTextField(stringResource(R.string.rate_this_job))
                                ReviewFormView(
                                    role = Role.CUSTOMER,
                                    jobRequestId = jobRequestState.value.selectedJobRequest!!.id!!,
                                    jobRequestStatus = jobRequestState.value.selectedJobRequest!!.jobRequestStatus,
                                    onSuccess = onCreatedReview
                                )
                            }
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                        JobDetailsCard(
                            details = jobState.value.toJobDetails()
                        )
                        if (jobState.value.jobPosting.status == JobStatus.ACTIVE) {
                            ConfirmableOutlinedButton(
                                onConfirmed = { cancelJobPosting() },
                                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                            ) {
                                Text(
                                    text = stringResource(R.string.details_button_job_cancel),
                                    color = MaterialTheme.colorScheme.error
                                )
                            }

//                            OutlinedButton(
//                                onClick = {
//                                    cancelJobPosting()
//                                },
//                                modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
//                            ) {
//                                Text(
//                                    text = stringResource(R.string.details_button_job_cancel),
//                                    color = MaterialTheme.colorScheme.error
//                                )
//                            }
                        }
                    }
                }
            }

            if (jobState.value.jobPosting.status == JobStatus.ACTIVE) {
                BottomSheetProviderOperations(
                    jobRequestState,
                    clearBottomSheetProvider = { selectBottomSheetProvider(null) },
                    showProfile = showProfile,
                    openChat = openChat,
                    selectProvider = selectJobProvider
                )
            }
        }
    }
}
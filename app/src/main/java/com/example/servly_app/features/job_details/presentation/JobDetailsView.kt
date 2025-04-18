package com.example.servly_app.features.job_details.presentation

import android.content.res.Configuration
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.core.data.util.JobStatus
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.data.util.ScheduleStatus
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.features.job_details.data.JobRequestInfo
import com.example.servly_app.features.job_details.presentation.core.OrderDetailsContentForCustomer
import com.example.servly_app.features.job_details.presentation.core.OrderDetailsContentForProvider
import com.example.servly_app.features.job_details.presentation.schedule_dialog.ScheduleDialog
import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import com.example.servly_app.features.job_schedule.presentation.JobScheduleView
import com.example.servly_app.features.job_schedule.presentation.JobScheduleViewModel
import com.example.servly_app.features.job_schedule.presentation.edit.JobScheduleFormView
import com.example.servly_app.features.job_schedule.presentation.edit.JobScheduleFormViewModel
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
fun PreviewRequestDetailsView() {
    val jobPosting = JobPostingInfo(
        customerName = "Roman",
        title = "Praca",
        categoryId = 0,
        address = "",
        answers = emptyList(),
        status = JobStatus.ACTIVE
    )

    val provider = ProviderInfo(
        name = "Tomasz",
        phoneNumber = "+48234123456",
        address = "Warszawa",
        rangeInKm = 30.0
    )

    val schedule = ScheduleInfo(
        scheduleStatus = ScheduleStatus.APPROVED
    )

    val job = JobRequestInfo(
        jobPostingInfo = jobPosting,
        provider = provider,
        schedule = schedule,
        jobRequestStatus = JobRequestStatus.COMPLETED
    )

    val state = remember { mutableStateOf(
        JobDetailsState(jobPosting = JobPostingInfo(
        title = "Asd",
        categoryId = 1,
        address = "asd",
        answers = emptyList()
    )
    )
    ) }

    val customerState = remember { mutableStateOf(JobRequestDetailsState(
        providersJobRequest = job,
        schedule = schedule
    )) }
    AppTheme {
        OrderDetailsContentForProvider(state, customerState, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {}, {i1, i2, i3 ->})
    }
}

@Composable
fun JobDetailsView(
    order: JobPostingInfo,
    role: Role = Role.CUSTOMER,
    showProviderProfile: (Long) -> Unit,
    showCustomerProfile: (Long) -> Unit,
    openChat: (Long) -> Unit,
    providerId: Long? = null,
    onPaymentClick: (Long, String, Long?) -> Unit
) {
    val jobViewModel: JobDetailsViewModel = hiltViewModel<JobDetailsViewModel, JobDetailsViewModel.OrderDetailsViewModelFactory> { factory ->
        factory.create(order)
    }

    val jobRequestViewModel: JobRequestDetailsViewModel = hiltViewModel<JobRequestDetailsViewModel, JobRequestDetailsViewModel.JobRequestDetailsViewModelFactory> { factory ->
        factory.create(order, providerId)
    }

    val state = jobViewModel.orderDetailsState.collectAsState()
    val jobRequestState = jobRequestViewModel.orderHeaderState.collectAsState()

    if (state.value.isLoading || jobRequestState.value.isLoading) {
        Log.i("LOADING", "JobLoading: ${state.value.isLoading} JobRequestLoading: ${jobRequestState.value.isLoading}")
        LoadingScreen()
        return
    } else {
        Log.i("NOT_LOADING", "JobLoading: ${state.value.isLoading} JobRequestLoading: ${jobRequestState.value.isLoading}")
    }

    if (role == Role.CUSTOMER) {

        OrderDetailsContentForCustomer(
            jobState = state,
            cancelJobPosting = jobViewModel::cancelJobPosting,
            jobRequestState = jobRequestState,
            selectBottomSheetProvider = jobRequestViewModel::updateBottomSheetProvider,
            sendFinishRequest = { jobRequestViewModel.sendFinishRequest(Role.CUSTOMER) },
            rejectFinishRequest = { jobRequestViewModel.rejectFinishRequest(Role.CUSTOMER) },
            finishJob = { jobRequestViewModel.finishJob(Role.CUSTOMER) },
            cancelJob = { jobRequestViewModel.cancelJob(Role.CUSTOMER) },
            showProfile = showProviderProfile,
            openChat = openChat,
            selectJobProvider = { jobRequest ->
                jobRequestViewModel.selectProvider(jobRequest.id!!)
            },
            showSchedule = { jobRequestViewModel.updateShowDialog(true) },
            onCreatedReview = jobRequestViewModel::updateCustomerReview,
            onPaymentClick = onPaymentClick
        )

        ScheduleDialog(
            jobRequestState.value.showDialog,
            onDismiss = { jobRequestViewModel.updateShowDialog(false) }
        ) {
            val viewModel: JobScheduleViewModel = hiltViewModel()
            val scheduleState = viewModel.jobScheduleState.collectAsState()

            JobScheduleView(
                schedule = jobRequestState.value.schedule!!,
                onSuccess = {
                    jobRequestViewModel.fetchJobRequestForCustomer()
                    jobRequestViewModel.updateShowDialog(false)
                },
                onEdit = {},
                viewModel = viewModel,
                state = scheduleState
            )
        }
    } else {
        OrderDetailsContentForProvider(
            jobState = state,
            jobRequestState = jobRequestState,
            createJobRequest = jobRequestViewModel::createJobRequest,
            withdrawnJobRequest = jobRequestViewModel::withdrawnJobRequest,
            acceptJob = jobRequestViewModel::acceptJob,
            sendFinishRequest = { jobRequestViewModel.sendFinishRequest(Role.PROVIDER) },
            rejectFinishRequest = { jobRequestViewModel.rejectFinishRequest(Role.PROVIDER) },
            finishJob = { jobRequestViewModel.finishJob(Role.PROVIDER) },
            cancelJob = { jobRequestViewModel.cancelJob(Role.PROVIDER) },
            openChat = openChat,
            showProfile = showCustomerProfile,
            createSchedule = {
                Log.i("SCHEDULE DATA", "ProvidersJobRequest: ${jobRequestState.value.providersJobRequest != null} id: ${jobRequestState.value.providersJobRequest?.id != null}")
                jobRequestViewModel.updateShowDialogEdit(true)
            },
            showSchedule = { jobRequestViewModel.updateShowDialog(true) },
            onCreatedReview = jobRequestViewModel::updateProviderReview,
            onPaymentClick = onPaymentClick
        )

        ScheduleDialog(
            jobRequestState.value.showDialog,
            onDismiss = { jobRequestViewModel.updateShowDialog(false) }
        ) {
            val viewModel: JobScheduleViewModel = hiltViewModel()
            val scheduleState = viewModel.jobScheduleState.collectAsState()

            Log.i("SCHEDULE DATA", "Schedule: ${jobRequestState.value.schedule!!}")

            JobScheduleView(
                schedule = jobRequestState.value.schedule!!,
                role = Role.PROVIDER,
                onSuccess = { },
                onEdit = {
                    jobRequestViewModel.updateShowDialogEdit(true)
                    jobRequestViewModel.updateShowDialog(false)
                },
                viewModel = viewModel,
                state = scheduleState
            )
        }

        ScheduleDialog(
            jobRequestState.value.showDialogEdit,
            onDismiss = { jobRequestViewModel.updateShowDialogEdit(false) }
        ) {
            val viewModel: JobScheduleFormViewModel = hiltViewModel()
            val scheduleState = viewModel.jobScheduleFormState.collectAsState()
            viewModel.setSchedule(
                jobRequestState.value.schedule ?: ScheduleInfo(jobRequestId = jobRequestState.value.providersJobRequest!!.id!!),
                isEdit = jobRequestState.value.schedule != null
            )

            JobScheduleFormView(
                isEdit = jobRequestState.value.schedule != null,
                onSuccess = {
                    Log.i("SCHEDULE", "SUCCESS SCHEDULE")
                    jobRequestViewModel.fetchJobRequestForProvider()
                    jobRequestViewModel.updateShowDialogEdit(false)
                },
                viewModel = viewModel,
                state = scheduleState
            )
        }
    }
}





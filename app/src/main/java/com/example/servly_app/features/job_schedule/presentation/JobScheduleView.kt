package com.example.servly_app.features.job_schedule.presentation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.data.util.ScheduleStatus
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import com.example.servly_app.features.job_schedule.presentation.components.ScheduleCard
import java.time.LocalDate

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
fun Preview() {
    AppTheme {
//        JobScheduleContent()
    }
}

@Composable
fun JobScheduleView(
    schedule: ScheduleInfo,
    role: Role = Role.CUSTOMER,
    onSuccess: () -> Unit,
    onEdit: () -> Unit,
    viewModel: JobScheduleViewModel,
    state: State<JobScheduleState>
) {
    viewModel.setSchedule(schedule)

    if (state.value.schedule == null) {
        LoadingScreen()
        return
    }

    JobScheduleContent(
        state,
        onApprove = { viewModel.approveSchedule(onSuccess) },
        onReject = { viewModel.rejectSchedule(onSuccess) },
        onEdit = onEdit,
        role
    )
}

@Composable
private fun JobScheduleContent(
    state: State<JobScheduleState>,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    onEdit: () -> Unit,
    role: Role
) {
    BasicScreenLayout {
        LazyColumn {
            item {
                Column {
                    if (state.value.schedule!!.scheduleStatus == ScheduleStatus.WAITING_FOR_CUSTOMER_APPROVAL ||
                        state.value.schedule!!.scheduleStatus == ScheduleStatus.UPDATED_WAITING_FOR_CUSTOMER_APPROVAL) {
                        Text(
                            text = stringResource(R.string.schedule_old),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        ScheduleCard(schedule = state.value.schedule!!)

                        Text(
                            text = stringResource(R.string.schedule_new),
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                        )
                        ScheduleCard(schedule = state.value.schedule!!.copy(
                            title = state.value.schedule!!.updatedTitle ?: "",
                            startAt = state.value.schedule!!.updatedStartAt ?: LocalDate.now(),
                            endAt = state.value.schedule!!.updatedEndAt ?: LocalDate.now(),
                            description = state.value.schedule!!.updatedDescription ?: "",
                            price = state.value.schedule!!.updatedPrice ?: 0,
                        ))
                    } else {
                        ScheduleCard(schedule = state.value.schedule!!)
                    }
                }
            }

            item {
                if (role == Role.CUSTOMER &&
                    (state.value.schedule!!.scheduleStatus == ScheduleStatus.WAITING_FOR_CUSTOMER_APPROVAL) ||
                    (state.value.schedule!!.scheduleStatus == ScheduleStatus.UPDATED_WAITING_FOR_CUSTOMER_APPROVAL)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = onReject,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.details_button_reject),
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Button(
                            onClick = onApprove,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = stringResource(R.string.schedule_approve),
                                color = Color.White
                            )
                        }
                    }
                } else if (state.value.schedule!!.scheduleStatus != ScheduleStatus.WAITING_FOR_CUSTOMER_APPROVAL &&
                    state.value.schedule!!.scheduleStatus != ScheduleStatus.UPDATED_WAITING_FOR_CUSTOMER_APPROVAL && role == Role.PROVIDER) {
                    Button(
                        onClick = onEdit,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.edit),
                            color = Color.White
                        )
                    }
                }
            }
        }

    }
}

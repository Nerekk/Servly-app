package com.example.servly_app.features.job_schedule.presentation.edit

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.DatePickerField
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.ui.theme.AppTheme
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
fun PreviewRequestScheduleView() {
    AppTheme {
        val state = remember { mutableStateOf(JobScheduleFormState()) }
        JobScheduleEditContent(
            state,
            {}, {}, {}, {}, {}, {}
        )
    }
}

@Composable
fun JobScheduleFormView(
    isEdit: Boolean = false,
    onSuccess: () -> Unit,
    viewModel: JobScheduleFormViewModel,
    state: State<JobScheduleFormState>
) {
    if (state.value.scheduleInfo == null) {
        LoadingScreen()
        return
    }

    JobScheduleEditContent(
        state,
        updateStartDate = viewModel::updateStartDate,
        updateEndDate = viewModel::updateEndDate,
        updateTitle = viewModel::updateTitle,
        updateDescription = viewModel::updateDescription,
        updatePrice = viewModel::updatePrice,
        updateSchedule = {
            if (isEdit) {
                viewModel.updateSchedule(onSuccess = onSuccess)
            } else {
                viewModel.createSchedule(onSuccess = onSuccess)
            }
        }
    )
}

@Composable
private fun JobScheduleEditContent(
    state: State<JobScheduleFormState>,
    updateStartDate: (LocalDate) -> Unit,
    updateEndDate: (LocalDate) -> Unit,
    updateTitle: (String) -> Unit,
    updateDescription: (String) -> Unit,
    updatePrice: (String) -> Unit,
    updateSchedule: () -> Unit,
) {
    if (state.value.isLoading) {
        LoadingScreen()
        return
    }
    BasicScreenLayout {
        Column {

            OutlinedTextField(
                value = state.value.scheduleInfo!!.updatedTitle ?: "",
                onValueChange = updateTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                placeholder = { Text(stringResource(R.string.schedule_title)) }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DatePickerField(
                    label = stringResource(R.string.schedule_date_start),
                    date = state.value.scheduleInfo?.updatedStartAt,
                    onDateSelected = updateStartDate,
                    modifier = Modifier.weight(1f)
                )
                DatePickerField(
                    label = stringResource(R.string.schedule_date_end),
                    date = state.value.scheduleInfo?.updatedEndAt,
                    onDateSelected = updateEndDate,
                    modifier = Modifier.weight(1f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.schedule_price),
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )

                OutlinedTextField(
                    value = state.value.scheduleInfo!!.updatedPrice?.let {
                        "$it zł"
                    } ?: run { "" },
                    onValueChange = { updatePrice(it.removeSuffix(" zł")) },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    )
                )

            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.schedule_description),
                    modifier = Modifier,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                OutlinedTextField(
                    value = state.value.scheduleInfo!!.updatedDescription ?: "",
                    onValueChange = updateDescription,
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text(stringResource(R.string.schedule_description_placeholder)) }
                )
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))


            Button(
                onClick = updateSchedule,
                modifier = Modifier.fillMaxWidth(),
                enabled = state.value.isButtonEnabled
            ) {
                Text(
                    text = stringResource(R.string.save),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

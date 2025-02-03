package com.example.servly_app.features._customer.job_create.presentation.job_form

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.servly_app.R
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features._customer.CustomerState
import com.example.servly_app.features._customer.job_create.presentation.main.components.JobCategory
import com.example.servly_app.features.role_selection.presentation.components.FormHeader

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
fun PreviewOffersView() {
    val state = remember { mutableStateOf(JobFormState()) }
    AppTheme {
        JobFormContent(
            state,
            updateTitle = {},
            onClick = {},
            updateCity = {},
            updateStreet = {},
            updateHouseNumber = {},
            autofillAddress = {},
            updateAnswer = { it1, it2 ->

            }
        )
    }
}

@Composable
fun JobFormView(jobCategory: JobCategory?, customerState: State<CustomerState>, onSuccess: () -> Unit) {
    jobCategory?.let {
        val viewModel: JobFormViewModel = hiltViewModel<JobFormViewModel, JobFormViewModel.JobFormViewModelFactory> { factory ->
            factory.create(jobCategory)
        }
        val state = viewModel.jobFormState.collectAsState()
        val context = LocalContext.current

        JobFormContent(
            state,
            updateTitle = viewModel::updateTitle,
            updateCity = viewModel::updateCity,
            updateStreet = viewModel::updateStreet,
            updateHouseNumber = viewModel::updateHouseNumber,
            autofillAddress = {
                viewModel.autofillAddress(customerState)
            },
            updateAnswer = viewModel::updateAnswer,
            onClick = {
                viewModel.createJob(
                    onSuccess = {
                        onSuccess()
                    },
                    onFailure = {
                        Toast.makeText(context, "Create job posting failed", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        )
    } ?: run {
        Text("Service category is null")
    }
}

@Composable
private fun JobFormContent(
    state: State<JobFormState>,
    updateTitle: (String) -> Unit,
    updateCity: (String) -> Unit,
    updateStreet: (String) -> Unit,
    updateHouseNumber: (String) -> Unit,
    autofillAddress: () -> Unit,
    updateAnswer: (Long, String) -> Unit,
    onClick: () -> Unit
) {
    Log.d("LazyColumnDebug", "Answers: ${state.value.answers}")
    Log.d("LazyColumnDebug", "Questions: ${state.value.questions}")


    BasicScreenLayout {
        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (state.value.isLoading) {
                LoadingScreen()
                return@Column
            }

            Column {
                if (state.value.errorMessage != null) {
                    Text(state.value.errorMessage!!)
                    return@Column
                }

                LazyColumn {
                    item {
                        Column {
                            Text(
                                text = stringResource(R.string.job_form_header),
                                style = MaterialTheme.typography.titleLarge
                            )
                            OutlinedTextField(
                                value = state.value.title,
                                onValueChange = { updateTitle(it) },
                                label = { Text(stringResource(R.string.job_form_title)) },
                                isError = state.value.titleError != null,
                                maxLines = 1,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                supportingText = {
                                    state.value.titleError?.let { errorMessage ->
                                        Text(
                                            text = errorMessage,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            )
                            Text(
                                text = "${state.value.title.length}/${JobFormState.MAX_TITLE_SIZE}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray,
                                modifier = Modifier.align(Alignment.End)
                            )

                            FormHeader(
                                imageVector = Icons.Default.LocationOn,
                                text = stringResource(R.string.job_form_location),
                                modifier = Modifier.padding(top = 16.dp)
                            )
                            OutlinedTextField(
                                value = state.value.city,
                                onValueChange = { updateCity(it) },
                                label = { Text(stringResource(R.string.customer_field_city)) },
                                placeholder = { Text(stringResource(R.string.customer_field_city)) },
                                isError = state.value.cityError != null,
                                maxLines = 1,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                supportingText = {
                                    state.value.cityError?.let { errorMessage ->
                                        Text(
                                            text = errorMessage,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            )

                            OutlinedTextField(
                                value = state.value.street,
                                onValueChange = { updateStreet(it) },
                                label = { Text(stringResource(R.string.customer_field_street)) },
                                placeholder = { Text(stringResource(R.string.customer_field_street)) },
                                isError = state.value.streetError != null,
                                maxLines = 1,
                                modifier = Modifier
                                    .fillMaxWidth(),
                                supportingText = {
                                    state.value.streetError?.let { errorMessage ->
                                        Text(
                                            text = errorMessage,
                                            color = MaterialTheme.colorScheme.error,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            )

                            OutlinedTextField(
                                value = state.value.houseNumber,
                                onValueChange = { updateHouseNumber(it) },
                                label = { Text(stringResource(R.string.customer_field_house_number)) },
                                placeholder = { Text(stringResource(R.string.customer_field_house_number)) },
                                maxLines = 1,
                                modifier = Modifier
                                    .fillMaxWidth()
                            )
                            Text(
                                text = AnnotatedString(stringResource(R.string.job_form_autofill)),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier
                                    .align(Alignment.End)
                                    .padding(top = 8.dp)
                                    .clickable { autofillAddress() }
                            )
                        }
                    }

                    item {
                        FormHeader(
                            imageVector = Icons.Default.Info,
                            text = stringResource(R.string.job_form_required),
                            modifier = Modifier.padding(top = 20.dp, bottom = 8.dp)
                        )
                    }

                    itemsIndexed(state.value.questions) { index, question ->
                        Column(
                            modifier = Modifier.padding(bottom = 20.dp)
                        ) {
                            Text("${index+1}. ${question.text}")
                            state.value.answers.find { it.id == question.id }?.let { answer ->
                                OutlinedTextField(
                                    value = answer.answer,
                                    onValueChange = { updateAnswer(question.id, it) },
                                    label = { Text(stringResource(R.string.job_form_answer)) },
                                    isError = answer.errorMessage != null,
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    supportingText = {
                                        answer.errorMessage?.let { errorMessage ->
                                            Text(
                                                text = errorMessage,
                                                color = MaterialTheme.colorScheme.error,
                                                style = MaterialTheme.typography.bodySmall
                                            )
                                        }
                                    }
                                )
                                Text(
                                    text = "${answer.answer.length}/${JobFormState.MAX_ANSWER_SIZE}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray,
                                    modifier = Modifier.align(Alignment.End)
                                )
                            }
                        }
                    }

                    item {
                        JobFormButton(onClick, state)
                    }
                }
            }
        }
    }
}

@Composable
private fun JobFormButton(onClick: () -> Unit, state: State<JobFormState>) {
    Column(
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        if (state.value.isLoadingButton) {
            LoadingScreen()
            return@Column
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClick() }
        ) {
            Text(
                text = stringResource(R.string.job_form_button),
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}

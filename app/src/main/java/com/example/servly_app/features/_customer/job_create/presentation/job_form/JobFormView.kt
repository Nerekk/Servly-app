package com.example.servly_app.features._customer.job_create.presentation.job_form

import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.servly_app.R
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.LoadingScreen
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features._customer.CustomerState
import com.example.servly_app.features._customer.job_create.presentation.main.components.JobCategory
import com.example.servly_app.features.role_selection.presentation.components.FormHeader
import com.google.android.gms.maps.model.LatLng

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
            autofillAddress = {},
            updateAddress = {it1, it2 ->},
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

        val toastMessage = stringResource(R.string.toast_create_job_failed)

        JobFormContent(
            state,
            updateTitle = viewModel::updateTitle,
            autofillAddress = {
                viewModel.autofillAddress(customerState)
            },
            updateAnswer = viewModel::updateAnswer,
            updateAddress = viewModel::updateAddress,
            onClick = {
                viewModel.createJob(
                    onSuccess = {
                        onSuccess()
                    },
                    onFailure = {
                        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
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
    updateAddress: (String, LatLng) -> Unit,
    autofillAddress: () -> Unit,
    updateAnswer: (Long, String) -> Unit,
    onClick: () -> Unit
) {
    Log.d("LazyColumnDebug", "Answers: ${state.value.answers}")
    Log.d("LazyColumnDebug", "Questions: ${state.value.questions}")

    var showPlacePicker by remember { mutableStateOf(false) }


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
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground
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

                            state.value.selectedAddress?.let {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(it)
                                    Text(
                                        text = AnnotatedString(stringResource(R.string.change)),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier
                                            .clickable { showPlacePicker = true }
                                    )
                                }
                            } ?: run {
                                OutlinedButton(
                                    onClick = {
                                        showPlacePicker = true
                                    },
                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                                ) {
                                    Text(stringResource(R.string.set_location))
                                }
                            }
                            state.value.addressError?.let { errorMessage ->
                                Text(
                                    text = errorMessage,
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }

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
    if (showPlacePicker) {
        Dialog(onDismissRequest = { showPlacePicker = false }) {
            Column(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(10))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.find_location),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    IconButton(
                        onClick = { showPlacePicker = false }
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = null)
                    }
                }

                PlacePickerScreen(
                    onPlaceSelected = { address, latLng ->
                        updateAddress(address, latLng)
                        Log.d("MainScreen", "Wybrano: $address, LatLng: $latLng")
                    },
                    onDismiss = { showPlacePicker = false }
                )
                Text(
                    text = AnnotatedString(stringResource(R.string.job_form_autofill)),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 16.dp)
                        .clickable {
                            autofillAddress()
                            showPlacePicker = false
                        }
                )
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

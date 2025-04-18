package com.example.servly_app.features.role_selection.presentation.user_data

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.core.components.ScaffoldAuthNavBar
import com.example.servly_app.core.components.ArrangedColumn
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.features._customer.job_create.presentation.job_form.PlacePickerScreen
import com.example.servly_app.features.role_selection.data.ProviderInfo
import com.example.servly_app.features.role_selection.presentation.components.FormHeader
import com.example.servly_app.features.role_selection.presentation.components.HeaderTitle
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
fun PreviewProviderFormView() {
    val navController = rememberNavController()
    val state = remember { mutableStateOf(ProviderState()) }
    AppTheme {
        ProviderFormContent(
            navController = navController,
            state = state,
            updateName = {},
            updatePhoneNumber = {},
            updateAddress = {it1, it2 ->},
            updateRange = {}, {},
            onSaveButton = {}
        )
    }
}



@Composable
fun ProviderFormView(navController: NavHostController, providerInfo: ProviderInfo? = null, onSuccess: () -> Unit) {
    val viewModel: ProviderFormViewModel = hiltViewModel()
    providerInfo?.let { viewModel.setEditData(it) }
    val state = viewModel.providerState.collectAsState()

    val context = LocalContext.current

    val toastMessage = stringResource(R.string.toast_profil_update_failed)

    ProviderFormContent(
        navController,
        state,
        updateName = viewModel::updateName,
        updatePhoneNumber = viewModel::updatePhoneNumber,
        updateAddress = viewModel::updateAddress,
        updateRange = viewModel::updateRange,
        updateAboutMe = viewModel::updateAboutMe,
        onSaveButton = {
            viewModel.handleProvider(
                onSuccess = { onSuccess() },
                onFailure = { Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show() }
            )
        }
    )
}

@Composable
private fun ProviderFormContent(
    navController: NavHostController,
    state: State<ProviderState>,
    updateName: (String) -> Unit,
    updatePhoneNumber: (String) -> Unit,
    updateAddress: (String, LatLng) -> Unit,
    updateRange: (Double) -> Unit,
    updateAboutMe: (String) -> Unit,
    onSaveButton: () -> Unit
) {
    var showPlacePicker by remember { mutableStateOf(false) }


    ScaffoldAuthNavBar(
        navController,
        title = if (state.value.isEditForm) stringResource(R.string.profile_editing) else null
    ) { initialPadding ->
        BasicScreenLayout(initialPadding) {
            LazyColumn {
                item {
                    Column {
                        HeaderTitle(stringResource(R.string.role_form_title))

                        FormHeader(
                            imageVector = Icons.Default.Person,
                            text = stringResource(R.string.role_form_basic_info)
                        )
                        OutlinedTextField(
                            value = state.value.name,
                            onValueChange = { updateName(it) },
                            label = { Text(stringResource(R.string.role_form_field_name)) },
                            placeholder = { Text(stringResource(R.string.role_form_field_name)) },
                            isError = state.value.nameError != null,
                            modifier = Modifier
                                .fillMaxWidth(),
                            supportingText = {
                                state.value.nameError?.let { errorMessage ->
                                    Text(
                                        text = errorMessage,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        )

                        OutlinedTextField(
                            value = state.value.phoneNumber,
                            onValueChange = { updatePhoneNumber(it) },
                            label = { Text(stringResource(R.string.role_form_field_phone_number)) },
                            placeholder = { Text("+48222333444") },
                            isError = state.value.phoneError != null,
                            modifier = Modifier
                                .fillMaxWidth(),
                            supportingText = {
                                state.value.phoneError?.let { errorMessage ->
                                    Text(
                                        text = errorMessage,
                                        color = MaterialTheme.colorScheme.error,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            },
                            keyboardOptions = KeyboardOptions.Default.copy(
                                keyboardType = KeyboardType.Number
                            )
                        )


                        FormHeader(
                            imageVector = Icons.Default.LocationOn,
                            text = stringResource(R.string.provider_field_address),
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


                        FormHeader(
                            text = "${stringResource(R.string.provider_field_area)} ${state.value.rangeInKm.toInt()}km",
                            modifier = Modifier.padding(top = 16.dp)
                        )
                        Slider(
                            valueRange = 20f..100f,
                            steps = 7,
                            value = state.value.rangeInKm.toFloat(),
                            onValueChange = { updateRange(it.toDouble()) }
                        )

                        FormHeader(text = stringResource(R.string.provider_aboutme_section))
                        OutlinedTextField(
                            value = state.value.aboutMe,
                            onValueChange = updateAboutMe,
                            label = { Text(stringResource(R.string.provider_field_aboutme)) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    ProviderButton(onSaveButton, state.value.isLoading, state.value.isButtonEnabled)
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
                }
            }
        }
    }
}


@Composable
private fun ProviderButton(
    onSaveButton: () -> Unit,
    isLoading: Boolean,
    isEnabled: Boolean
) {
    Column(
        modifier = Modifier.padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Button(
                onClick = { onSaveButton() },
                enabled = isEnabled,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.save),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

package com.example.servly_app.features.role_selection.presentation.user_data

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.authentication.presentation.navigation.AuthNavItem
import com.example.servly_app.core.components.ScaffoldAuthNavBar
import com.example.servly_app.core.components.ArrangedColumn
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.features.role_selection.data.ProviderInfo
import com.example.servly_app.features.role_selection.presentation.components.FormHeader
import com.example.servly_app.features.role_selection.presentation.components.HeaderTitle

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
            updateCity = {},
            updateRange = {},
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

    ProviderFormContent(
        navController,
        state,
        updateName = viewModel::updateName,
        updatePhoneNumber = viewModel::updatePhoneNumber,
        updateCity = viewModel::updateCity,
        updateRange = viewModel::updateRange,
        onSaveButton = {
            viewModel.handleProvider(
                onSuccess = { onSuccess() },
                onFailure = { Toast.makeText(context, "Create customer fail", Toast.LENGTH_SHORT).show() }
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
    updateCity: (String) -> Unit,
    updateRange: (Double) -> Unit,
    onSaveButton: () -> Unit
) {

    ScaffoldAuthNavBar(
        navController,
        title = if (state.value.isEditForm) stringResource(R.string.profile_editing) else null
    ) { initialPadding ->
        BasicScreenLayout(initialPadding) {
            ArrangedColumn {
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
                        }
                    )


                    FormHeader(
                        imageVector = Icons.Default.LocationOn,
                        text = stringResource(R.string.provider_field_address),
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    OutlinedTextField(
                        value = state.value.city,
                        onValueChange = { updateCity(it) },
                        label = { Text(stringResource(R.string.customer_field_city)) },
                        placeholder = { Text(stringResource(R.string.customer_field_city)) },
                        isError = state.value.cityError != null,
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
                }

                ProviderButton(onSaveButton, state.value.isLoading, state.value.isButtonEnabled)
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
        modifier = Modifier.padding(bottom = 24.dp),
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
                    text = stringResource(R.string.role_save),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.example.servly_app.core.components.ArrangedColumn
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.components.ScaffoldAuthNavBar
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.role_selection.data.CustomerInfo
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
fun PreviewCustomerFormView() {
    val navController = rememberNavController()
    val state = remember { mutableStateOf(CustomerState()) }
    AppTheme {
        CustomerFormContent(
            navController,
            state,
            updateName = {},
            updatePhoneNumber = {},
            updateCity = {},
            updateStreet = {},
            updateHouseNumber = {},
            onSaveButton = {},
        )
    }
}

@Composable
fun CustomerFormView(navController: NavHostController, customerInfo: CustomerInfo? = null, onSuccess: () -> Unit) {
    val viewModel: CustomerFormViewModel = hiltViewModel()
    customerInfo?.let { viewModel.setEditData(it) }
    val customerState = viewModel.customerState.collectAsState()

    val context = LocalContext.current

    CustomerFormContent(
        navController = navController,
        state = customerState,
        updateName = viewModel::updateName,
        updatePhoneNumber = viewModel::updatePhoneNumber,
        updateCity = viewModel::updateCity,
        updateStreet = viewModel::updateStreet,
        updateHouseNumber = viewModel::updateHouseNumber,
        onSaveButton = {
            viewModel.handleCustomer(
                onSuccess = { onSuccess() },
                onFailure = { Toast.makeText(context, "Customer fail", Toast.LENGTH_SHORT).show() }
            )
        }
    )
}

@Composable
private fun CustomerFormContent(
    navController: NavHostController,
    state: State<CustomerState>,
    updateName: (String) -> Unit,
    updatePhoneNumber: (String) -> Unit,
    updateCity: (String) -> Unit,
    updateStreet: (String) -> Unit,
    updateHouseNumber: (String) -> Unit,
    onSaveButton: () -> Unit
) {
    ScaffoldAuthNavBar(
        navController,
        title = if (state.value.isEditForm) "Profile editing" else null
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
                        text = stringResource(R.string.customer_field_address),
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

                    OutlinedTextField(
                        value = state.value.street,
                        onValueChange = { updateStreet(it) },
                        label = { Text(stringResource(R.string.customer_field_street)) },
                        placeholder = { Text(stringResource(R.string.customer_field_street)) },
                        isError = state.value.streetError != null,
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
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                CustomerButton(onSaveButton, state.value.isLoading, state.value.isButtonEnabled)
            }
        }
    }
}

@Composable
private fun CustomerButton(
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




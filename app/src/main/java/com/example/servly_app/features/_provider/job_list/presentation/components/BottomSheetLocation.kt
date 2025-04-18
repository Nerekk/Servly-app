package com.example.servly_app.features._provider.job_list.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.features._provider.job_list.presentation.ProviderJobListState
import com.google.android.gms.maps.model.LatLng

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetLocation(
    state: State<ProviderJobListState>,
    updateSheetVisibility: (Boolean, Boolean, Boolean, Boolean) -> Unit,
    updateShowPlacePicker: (Boolean) -> Unit,
    updateSheetLocation: (String?, LatLng?) -> Unit,
    updateSheetDistance: (Double) -> Unit,
    updateLocation: () -> Unit,
    autofill: () -> Unit
) {
    if (state.value.isLocationSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { updateSheetVisibility(false, false, false, false) }
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 24.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(R.string.location),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .clickable { updateSheetVisibility(false, false, false, false) }
                    )
                }

                state.value.sheetLocation?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = it,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = AnnotatedString(stringResource(R.string.change)),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .clickable { updateShowPlacePicker(true) }
                        )
                    }
                } ?: run {
                    OutlinedButton(
                        onClick = {
                            updateShowPlacePicker(true)
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    ) {
                        Text(stringResource(R.string.set_location))
                    }
                }

                Text(
                    "${stringResource(R.string.provider_field_area)} ${state.value.sheetDistanceInKm.toInt()}km",
                    modifier = Modifier.padding(top = 16.dp)
                )
                Slider(
                    valueRange = 20f..100f,
                    steps = 7,
                    value = state.value.sheetDistanceInKm.toFloat(),
                    onValueChange = { updateSheetDistance(it.toDouble()) }
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    OutlinedButton(
                        enabled = state.value.sheetLocation != null,
                        onClick = {
                            updateSheetLocation(null, null)
                            updateSheetDistance(30.0)
                            updateSheetVisibility(false, false, false, false)

                            updateLocation()
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.clear))
                    }

                    Spacer(Modifier.width(4.dp))

                    OutlinedButton(
                        enabled = true,
                        onClick = autofill,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.autofill))
                    }
                }


                Button(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    enabled = (state.value.sheetLocation != null) &&
                            (state.value.selectedLocation != state.value.sheetLocation) || (state.value.selectedDistanceInKm != state.value.sheetDistanceInKm),
                    onClick = {
                        updateSheetVisibility(false, false, false, false)
                        updateLocation()
                    }
                ) {
                    Text(stringResource(R.string.job_list_apply))
                }
            }
        }
    }
}
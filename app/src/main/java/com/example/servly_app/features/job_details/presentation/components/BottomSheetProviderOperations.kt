package com.example.servly_app.features.job_details.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.data.util.JobRequestStatus
import com.example.servly_app.features.job_details.data.JobRequestInfo
import com.example.servly_app.features.job_details.presentation.JobRequestDetailsState
import com.example.servly_app.features.role_selection.data.ProviderInfo

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetProviderOperations(
    state: State<JobRequestDetailsState>,
    clearBottomSheetProvider: () -> Unit,
    showProfile: (Long) -> Unit,
    openChat: (Long) -> Unit,
    selectProvider: (JobRequestInfo) -> Unit,
) {
    if (state.value.bottomSheetJobRequest != null) {
        ModalBottomSheet(
            onDismissRequest = { clearBottomSheetProvider() }
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
                        text = state.value.bottomSheetJobRequest!!.provider!!.name,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .clickable { clearBottomSheetProvider() }
                    )
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    onClick = {
                        showProfile(state.value.bottomSheetJobRequest!!.provider!!.providerId!!)
                        clearBottomSheetProvider()
                    }
                ) {
                    Text(stringResource(R.string.details_show_profile))
                }

                OutlinedButton(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    onClick = {
                        openChat(state.value.bottomSheetJobRequest!!.id!!)
                        clearBottomSheetProvider()
                    }
                ) {
                    Text(stringResource(R.string.open_chat))
                }

                Button(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    onClick = {
                        selectProvider(state.value.bottomSheetJobRequest!!)
                        clearBottomSheetProvider()
                    },
                    enabled = (state.value.bottomSheetJobRequest!!.jobRequestStatus != JobRequestStatus.WAITING_FOR_PROVIDER_APPROVE &&
                            state.value.bottomSheetJobRequest!!.jobRequestStatus != JobRequestStatus.WITHDRAWN)
                ) {
                    Text(stringResource(R.string.select_provider))
                }
            }
        }
    }
}
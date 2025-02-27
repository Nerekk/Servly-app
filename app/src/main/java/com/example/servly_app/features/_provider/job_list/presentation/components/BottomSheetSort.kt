package com.example.servly_app.features._provider.job_list.presentation.components

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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.features._provider.job_list.presentation.ProviderJobListState

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun BottomSheetSort(
    state: State<ProviderJobListState>,
    updateSheetVisibility: (Boolean, Boolean, Boolean) -> Unit,
    updateSortType: (SortType) -> Unit
) {
    if (state.value.isSortSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { updateSheetVisibility(false, false, false) }
        ) {
            var selectedOption by remember { mutableStateOf(state.value.sortType) }

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
                        text = stringResource(R.string.job_list_sort),
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        modifier = Modifier
                            .clickable { updateSheetVisibility(false, false, false) }
                    )
                }



                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedOption = SortType.DESCENDING }
                ) {
                    Text(stringResource(R.string.job_list_sort_newest))
                    RadioButton(
                        selected = selectedOption == SortType.DESCENDING,
                        onClick = { selectedOption = SortType.DESCENDING }
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedOption = SortType.ASCENDING }
                ) {
                    Text(stringResource(R.string.job_list_sort_clear))
                    RadioButton(
                        selected = selectedOption == SortType.ASCENDING,
                        onClick = { selectedOption = SortType.ASCENDING }
                    )
                }

                Button(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                    enabled = state.value.sortType != selectedOption,
                    onClick = {
                        updateSheetVisibility(false, false, false)
                        updateSortType(selectedOption)
                    }
                ) {
                    Text(stringResource(R.string.job_list_apply))
                }
            }
        }
    }
}
package com.example.servly_app.features._provider.job_list.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.features._provider.job_list.presentation.ProviderJobListState

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
fun BottomSheetCategories(
    state: State<ProviderJobListState>,
    updateSheetVisibility: (Boolean, Boolean, Boolean, Boolean) -> Unit,
    updateSelectedCategories: (List<Long>) -> Unit,
) {
    if (state.value.isCategorySheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { updateSheetVisibility(false, false, false, false) }
        ) {
            var selectedCategories by remember { mutableStateOf(emptyList<Long>()) }

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
                        text = stringResource(R.string.job_list_services),
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

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(7.dp)
                ) {
                    state.value.categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategories.contains(category.id),
                            onClick = {
                                selectedCategories = if (selectedCategories.contains(category.id)) {
                                    selectedCategories - category.id
                                } else {
                                    selectedCategories + category.id
                                }
                            },
                            label = { Text(category.name) },
                            trailingIcon = {
                                if (selectedCategories.contains(category.id)) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = "Clear"
                                    )
                                }
                            }
                        )
                    }
                }

                Button(
                    modifier = Modifier.fillMaxWidth(),
                    enabled = state.value.selectedCategories != selectedCategories,
                    onClick = {
                        updateSheetVisibility(false, false, false, false)
                        updateSelectedCategories(selectedCategories)
                    }
                ) {
                    Text(stringResource(R.string.job_list_apply))
                }
            }
        }
    }
}
package com.example.servly_app.features._provider.job_list.presentation

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.servly_app.R
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.data.util.SortType
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features._customer.job_create.data.dtos.JobPostingInfo
import com.example.servly_app.features._customer.job_list.presentation.main_view.components.Order
import com.example.servly_app.features._customer.job_list.presentation.main_view.components.OrderList
import com.example.servly_app.features._provider.job_list.presentation.components.BottomSheetCategories
import com.example.servly_app.features._provider.job_list.presentation.components.BottomSheetDate
import com.example.servly_app.features._provider.job_list.presentation.components.BottomSheetSort

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
fun PreviewProviderJobListView() {
    val state = remember { mutableStateOf(ProviderJobListState()) }
    AppTheme {
        ProviderJobListContent(state, {a, b ->}, {}, {}, {}, {}, {}, {}, { a, b, c -> })
    }
}

@Composable
fun ProviderJobListView(onClickShowDetails: (JobPostingInfo) -> Unit) {
    val viewModel: ProviderJobListViewModel = hiltViewModel()
    val state = viewModel.providerJobListState.collectAsState()

    ProviderJobListContent(
        state,
        reapplyFiltersAndReload = viewModel::reapplyFiltersAndReloadJobs,
        updateQuery = viewModel::updateSearchQuery,
        updateSelectedCategories = viewModel::updateSelectedCategories,
        updateSelectedDays = viewModel::updateDays,
        updateSortType = viewModel::updateSortType,
        loadJobs = viewModel::loadJobs,
        onClickShowDetails = onClickShowDetails,
        updateSheetVisibility = viewModel::updateSheetVisibility
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ProviderJobListContent(
    state: State<ProviderJobListState>,
    reapplyFiltersAndReload: (Boolean?, Boolean?) -> Unit,
    updateQuery: (String) -> Unit,
    updateSelectedCategories: (List<Long>) -> Unit,
    updateSelectedDays: (Long) -> Unit,
    updateSortType: (SortType) -> Unit,
    loadJobs: (Boolean) -> Unit,
    onClickShowDetails: (JobPostingInfo) -> Unit,
    updateSheetVisibility: (Boolean, Boolean, Boolean) -> Unit
) {
    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = {
            val keyboardController = LocalSoftwareKeyboardController.current
            DockedSearchBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = tween(durationMillis = 300))
                    .height(height = if (lazyListState.canScrollBackward) 0.dp else 56.dp)
                    .padding(horizontal = 16.dp),
                query = state.value.searchQuery,
                onQueryChange = { updateQuery(it) },
                onSearch = {
                    keyboardController?.hide()
                    reapplyFiltersAndReload(true, null)
                },
                active = false,
                onActiveChange = { },
                placeholder = { Text(stringResource(R.string.job_list_search)) },
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "search", tint = Color.Gray) },
                trailingIcon = {
                    if (state.value.searchQuery.isNotEmpty()) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "clear",
                            modifier = Modifier.clickable {
                                updateQuery("")
                                reapplyFiltersAndReload(null, true)
                            }
                        )
                    }
                },
            ) {}
        }
    ) { initialPadding ->
        BasicScreenLayout(isListInContent = true, padding = initialPadding) {
            Column {
                LazyRow(contentPadding = PaddingValues(4.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        InputChip(
                            selected = state.value.sortType == SortType.ASCENDING,
                            onClick = {
                                if (state.value.sortType == SortType.ASCENDING) {
                                    updateSortType(SortType.DESCENDING)
                                } else {
                                    updateSheetVisibility(true, false, false)
                                }
                            },
                            label = {
                                if (state.value.sortType == SortType.ASCENDING) {
                                    Text(stringResource(R.string.job_list_sort_clear))
                                } else {
                                    Text(stringResource(R.string.job_list_sort))
                                }
                            },
                            trailingIcon = {
                                if (state.value.sortType == SortType.ASCENDING) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "clear")
                                } else {
                                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "down")
                                }
                            }
                        )
                    }
                    item {
                        InputChip(
                            selected = state.value.selectedCategories.isNotEmpty(),
                            onClick = {
                                if (state.value.selectedCategories.isNotEmpty()) {
                                    updateSelectedCategories(emptyList())
                                } else {
                                    updateSheetVisibility(false, true, false)
                                }
                            },
                            label = {
                                if (state.value.selectedCategories.isNotEmpty()) {
                                    Text("${state.value.selectedCategories.size} ${stringResource(R.string.job_list_services)}")
                                } else {
                                    Text(stringResource(R.string.job_list_services))
                                }
                            },
                            trailingIcon = {
                                if (state.value.selectedCategories.isNotEmpty()) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "clear")
                                } else {
                                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "down")
                                }
                            }
                        )
                    }

                    item {
                        InputChip(
                            selected = state.value.selectedDays != ProviderJobListState.DATE_OPTIONS[2].first,
                            onClick = {
                                if (state.value.selectedDays != ProviderJobListState.DATE_OPTIONS[2].first) {
                                    updateSelectedDays(ProviderJobListState.DATE_OPTIONS[2].first)
                                } else {
                                    updateSheetVisibility(false, false, true)
                                }
                            },
                            label = {
                                if (state.value.selectedDays != ProviderJobListState.DATE_OPTIONS[2].first) {
                                    Text(
                                        text = when (state.value.selectedDays) {
                                                ProviderJobListState.DATE_OPTIONS[0].first -> stringResource(ProviderJobListState.DATE_OPTIONS[0].second)
                                                ProviderJobListState.DATE_OPTIONS[1].first -> stringResource(ProviderJobListState.DATE_OPTIONS[1].second)
                                                ProviderJobListState.DATE_OPTIONS[3].first -> stringResource(ProviderJobListState.DATE_OPTIONS[3].second)
                                            else -> {""}
                                        }
                                    )
                                } else {
                                    Text(stringResource(R.string.job_list_date))
                                }
                            },
                            trailingIcon = {
                                if (state.value.selectedDays != ProviderJobListState.DATE_OPTIONS[2].first) {
                                    Icon(imageVector = Icons.Default.Clear, contentDescription = "clear")
                                } else {
                                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "down")
                                }
                            }
                        )
                    }
                }

                OrderList(
                    orders = state.value.jobs.map { job ->
                        Order(
                            id = job.id!!,
                            title = job.title,
                            location = "${job.city}, ${job.street}",
                            category = state.value.getCategoryName(job.categoryId),
                            status = job.status,
                            person = job.customerName,
                        )
                    },
                    isLoading = state.value.isLoading,
                    { loadJobs(false) },
                    isActive = true,
                    onClickCard = { order ->
                        Log.i("OrderListView", "Click active id ${order.id}")
                        val jobForDetails = state.value.jobs.find { order.id == it.id }
                        onClickShowDetails(jobForDetails!!)
                    },
                    header = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.job_list_browse),
                                style = MaterialTheme.typography.titleLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            if (state.value.totalElements > 0) {
                                Text(
                                    text = "${stringResource(R.string.job_list_browse_found)}: ${state.value.totalElements}",
                                    style = MaterialTheme.typography.titleSmall,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    },
                    myListState = lazyListState
                )

                BottomSheetSort(state, updateSheetVisibility, updateSortType)
                BottomSheetCategories(state, updateSheetVisibility, updateSelectedCategories)
                BottomSheetDate(state, updateSheetVisibility, updateSelectedDays)
            }
        }
    }
}

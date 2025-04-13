package com.example.servly_app.features.timetable.presentation

import CalendarView
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.provider.CalendarContract
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.servly_app.R
import com.example.servly_app.core.components.BasicScreenLayout
import com.example.servly_app.core.data.util.Role
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.job_schedule.data.ScheduleInfo
import com.example.servly_app.features.job_schedule.presentation.components.ScheduleCard
import java.time.ZoneId

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
fun PreviewTimetableView() {
    AppTheme {
        TimetableView(Role.CUSTOMER)
    }
}

@Composable
fun TimetableView(role: Role) {
    val context = LocalContext.current

    val viewmodel: TimetableViewModel = hiltViewModel<TimetableViewModel, TimetableViewModel.TimetableViewModelFactory> { factory ->
        factory.create(role)
    }

    val timetableState = viewmodel.timetableState.collectAsState()
    val dayTasksState = viewmodel.dayTasksState.collectAsState()

    BasicScreenLayout(isListInContent = true) {
        LazyColumn {
            item {
                Column(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    CalendarView(
                        currentMonth = timetableState.value.selectedYearMonth,
                        totalTasks = timetableState.value.totalTasks,
                        taskDateRanges = timetableState.value.taskDates,
                        selectedDay = timetableState.value.selectedDay,
                        onClickDay = viewmodel::setSelectedDay,
                        onClickNext = viewmodel::setNextYearMonth,
                        onClickPrevious = viewmodel::setPreviousYearMonth
                    )

                    Column {
                        Row(
                            modifier = Modifier.padding(top = 16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.timetable_planned),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            Text(
                                text = timetableState.value.selectedDay.toString(),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        HorizontalDivider(Modifier.padding(bottom = 12.dp))
                    }
                }
            }

            if (dayTasksState.value.tasks.isNotEmpty()) {
                items(dayTasksState.value.tasks) { task ->
                    ScheduleCard(task)
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(4.dp),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Text(
                            text = stringResource(R.string.timetable_reminder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.clickable {
                                openCalendarWithEvent(context, task)
                            }
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                }
            } else {
                item {
                    Text(
                        text = stringResource(R.string.timetable_noplans),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }

    }
}

fun openCalendarWithEvent(context: Context, scheduleInfo: ScheduleInfo) {
    val zoneId = ZoneId.systemDefault()
    val startMillis = scheduleInfo.startAt.atStartOfDay(zoneId).toInstant().toEpochMilli()
    val endMillis = scheduleInfo.endAt.atStartOfDay(zoneId).toInstant().toEpochMilli()

    val intent = Intent(Intent.ACTION_INSERT).apply {
        data = CalendarContract.Events.CONTENT_URI
        putExtra(CalendarContract.Events.TITLE, scheduleInfo.title)
        putExtra(CalendarContract.Events.DESCRIPTION, scheduleInfo.description)
        putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startMillis)
        putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endMillis)
    }
    context.startActivity(intent)
}

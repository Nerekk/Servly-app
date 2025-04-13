import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features.timetable.data.DateRange
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*

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
fun PreviewCalendar() {
    val dateRanges = listOf(
        DateRange(startDate = LocalDate.of(2025, 3, 5), endDate = LocalDate.of(2025, 3, 5)),
        DateRange(startDate = LocalDate.of(2025, 3, 10), endDate = LocalDate.of(2025, 3, 12)),
        DateRange(startDate = LocalDate.of(2025, 3, 10), endDate = LocalDate.of(2025, 3, 10)),
        DateRange(startDate = LocalDate.of(2025, 3, 11), endDate = LocalDate.of(2025, 3, 11)),
        DateRange(startDate = LocalDate.of(2025, 3, 20), endDate = LocalDate.of(2025, 3, 22)),
        DateRange(startDate = LocalDate.of(2025, 3, 20), endDate = LocalDate.of(2025, 3, 21))
    )

    AppTheme {
        CalendarView(taskDateRanges = dateRanges, onClickDay = {}, onClickPrevious = {}, onClickNext = {})
    }
}

@Composable
fun CalendarView(
    currentMonth: YearMonth = YearMonth.now(),
    totalTasks: Int = 0,
    taskDateRanges: List<DateRange> = emptyList(),
    selectedDay: LocalDate = LocalDate.now(),
    onClickDay: (LocalDate) -> Unit,
    onClickPrevious: () -> Unit,
    onClickNext: () -> Unit
) {
    val today = LocalDate.now()

    val lineMap = createLineMap(taskDateRanges)
    val daysOfWeek = DayOfWeek.values()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding()
            .background(color = MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(12.dp))
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    onClickPrevious()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = {
                    onClickNext()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        HorizontalDivider(modifier = Modifier.padding(bottom = 4.dp), thickness = 4.dp)

        Row(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            daysOfWeek.forEach { day ->
                Text(
                    text = day.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 14.sp
                )
            }
        }

        val daysInMonth = currentMonth.lengthOfMonth()
        val firstDayOfMonth = LocalDate.of(currentMonth.year, currentMonth.month, 1).dayOfWeek
        val adjustedStart = (firstDayOfMonth.value + 6) % 7

        val dayPadding = 0.dp
        Column(
            verticalArrangement = Arrangement.spacedBy(dayPadding)
        ) {
            var dayCounter = 1
            for (week in 0..5) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(dayPadding)
                ) {
                    for (day in 0..6) {
                        if ((week == 0 && day < adjustedStart) || dayCounter > daysInMonth) {
                            Spacer(modifier = Modifier.weight(1f))
                        } else {
                            val currentDay = LocalDate.of(currentMonth.year, currentMonth.month, dayCounter)
                            val isToday = currentDay == today
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .border(
                                        width = 1.dp,
                                        color = if (selectedDay == currentDay) MaterialTheme.colorScheme.onSurface else Color.Transparent,
                                        shape = RoundedCornerShape(15)
                                    )
                                    .clickable { onClickDay(currentDay) },
                                contentAlignment = Alignment.TopCenter
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    HorizontalDivider()
                                    Text(
                                        text = "$dayCounter",
                                        color = if (isToday) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSurface,
                                        fontWeight = if (isToday) FontWeight.Bold else null
                                    )
                                    lineMap[currentDay]?.take(4)?.forEach { line ->
                                        line()
                                    }
                                }
                            }
                            dayCounter++
                        }
                    }
                }
            }
        }

        Text(
            text = "${totalTasks} ${stringResource(R.string.timetable_plannedmonth)}",
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
fun SingleLine(color: Color = MaterialTheme.colorScheme.tertiary) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 1.dp)
            .height(4.dp)
            .background(color, shape = RoundedCornerShape(50))
    )
}

@Composable
fun StartLine(color: Color = MaterialTheme.colorScheme.tertiary) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 4.dp, top = 1.dp, bottom = 1.dp)
            .height(4.dp)
            .background(color, shape = RoundedCornerShape(topStartPercent = 50, bottomStartPercent = 50))
    )
}

@Composable
fun MiddleLine(color: Color = MaterialTheme.colorScheme.tertiary) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp)
            .height(4.dp)
            .background(color)
    )
}

@Composable
fun EndLine(color: Color = MaterialTheme.colorScheme.tertiary) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 4.dp, top = 1.dp, bottom = 1.dp)
            .height(4.dp)
            .background(color, shape = RoundedCornerShape(topEndPercent = 50, bottomEndPercent = 50))
    )
}

fun createLineMap(dateRanges: List<DateRange>): Map<LocalDate, List<@Composable () -> Unit>> {
    val lineMap = mutableMapOf<LocalDate, MutableList<@Composable () -> Unit>>()

    dateRanges.forEach { range ->
        when {
            range.startDate == range.endDate -> {
                lineMap.getOrPut(range.startDate) { mutableListOf() }.add { SingleLine() }
            }
            else -> {
                lineMap.getOrPut(range.startDate) { mutableListOf() }.add { StartLine() }
                lineMap.getOrPut(range.endDate) { mutableListOf() }.add { EndLine() }

                var middleDate = range.startDate.plusDays(1)
                while (middleDate.isBefore(range.endDate)) {
                    lineMap.getOrPut(middleDate) { mutableListOf() }.add { MiddleLine() }
                    middleDate = middleDate.plusDays(1)
                }
            }
        }
    }

    return lineMap
}

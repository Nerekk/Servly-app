package com.example.servly_app.features._customer.requests.request_schedule

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.features._customer.requests.details_view.HeaderSection
import java.time.LocalDate

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
fun PreviewScheduleCard() {
    val exampleSchedule = Schedule(
        workDays = listOf(
            LocalDate.of(2025, 1, 8),
            LocalDate.of(2025, 1, 9)
        ),
        services = listOf(
            Service(
                title = "Hydraulika",
                subtasks = listOf(
                    "Naprawa kranu",
                    "Czyszczenie rur",
                    "Uszczelnianie rur",
                    "Instalacja zlewu"
                ),
                price = 200.0
            ),
            Service(
                title = "Sprzątanie",
                subtasks = listOf("Mycie okien", "Odkurzanie"),
                price = 80.0
            )
        )
    )

    AppTheme {
        ScheduleCard(exampleSchedule)
    }
}

@Composable
fun ScheduleCard(schedule: Schedule) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(24.dp)
            ),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            HeaderSection(
                title = "Budowa domu",
                location = "Łódź, Polesie",
                person = "Jan"
            )

            ScheduleList(schedule = schedule, isExpandedList = true)

        }
    }
}

@Composable
private fun SubtaskInfo(
    subtask: String
) {
    Text(
        text = "\t- $subtask",
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
private fun ScheduleList(
    schedule: Schedule,
    isExpandedList: Boolean?
) {
    var isExpanded by remember { mutableStateOf(isExpandedList ?: false) }

    Text(
        text = "Usługi",
        style = MaterialTheme.typography.headlineSmall,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .padding(top = 16.dp, bottom = 8.dp)
    )

    schedule.services.forEach { service ->
        ServiceInfo(
            service = service,
            isExpanded = isExpanded,
            modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.tertiaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
            .clickable { isExpanded = !isExpanded }
        )

        if (isExpanded) {
            service.subtasks.forEach { subtask ->
                SubtaskInfo(subtask)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
    
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Suma",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Text(
            text = "${schedule.services.sumOf { it.price }} zł",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }

}

@Composable
private fun ServiceInfo(
    service: Service,
    isExpanded: Boolean,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = service.title,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
            style = MaterialTheme.typography.titleMedium
        )

        Row {
            Text(
                text = "${service.price}zł",
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.align(Alignment.CenterVertically)
            )

            Icon(
                imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                contentDescription = "arrow",
                tint = if (service.subtasks.isNotEmpty()) MaterialTheme.colorScheme.onTertiaryContainer else Color.Transparent
            )

        }
    }
}

package com.example.servly_app.features.job_schedule.presentation.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.R
import com.example.servly_app.core.ui.theme.AppTheme
import com.example.servly_app.core.util.EnumUtils
import com.example.servly_app.core.util.LanguageUtils
import com.example.servly_app.features.job_schedule.data.ScheduleInfo
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
    AppTheme {
        ScheduleCard(
            schedule = ScheduleInfo(
                title = "Cleaning whole officedsadasd  dsa",
                price = 30,
                endAt = LocalDate.of(2025, 4, 20),
                description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Cras nunc odio, cursus quis suscipit vitae, eleifend quis velit. Sed vitae fringilla lorem. Nam consectetur libero ac odio fringilla, luctus molestie velit elementum. Vivamus semper pellentesque lacus vel placerat. "
            )
        )
    }
}

@Composable
fun ScheduleCard(schedule: ScheduleInfo) {
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
            Text(
                text = stringResource(R.string.schedule),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "${stringResource(R.string.schedule_updatedat)}: ${schedule.updatedAt}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Status: ${EnumUtils.getStatusString(schedule.scheduleStatus)}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )

            ScheduleList(schedule = schedule)
        }
    }
}

@Composable
private fun ScheduleList(
    schedule: ScheduleInfo
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = schedule.title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.weight(1f)
        )
    }

    HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 4.dp)
    ) {
        Text(
            text = schedule.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier
        )
    }

    Row(
        modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "date"
            )
            Text(
                text = if (!schedule.startAt.isEqual(schedule.endAt)) {
                    "${LanguageUtils.formatDate(schedule.startAt)} - ${LanguageUtils.formatDate(schedule.endAt)}"
                } else { LanguageUtils.formatDate(schedule.startAt) },
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.payments_24px),
                contentDescription = "payments",
                modifier = Modifier.padding(end = 4.dp)
            )

            Text(
                text = "${schedule.price} zł",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }

    Spacer(modifier = Modifier.height(12.dp))

}


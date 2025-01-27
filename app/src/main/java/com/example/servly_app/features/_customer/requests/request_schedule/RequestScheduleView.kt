package com.example.servly_app.features._customer.requests.request_schedule

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.servly_app.core.ui.theme.AppTheme
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
fun PreviewRequestScheduleView() {
    AppTheme {
        RequestScheduleView()
    }
}

@Composable
fun RequestScheduleView() {
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Harmonogram #123",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            LazyColumn {
                item {
                    ScheduleCard(schedule = exampleSchedule)
                }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = {},
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Odrzuć",
                                color = MaterialTheme.colorScheme.error
                            )
                        }

                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "Akceptuj",
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

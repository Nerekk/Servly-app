package com.example.servly_app.features._customer.job_list.presentation.details_view.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.servly_app.core.data.util.JobStatus

@Composable
fun HeaderSection(
    title: String,
    location: String,
    status: JobStatus,
    person: String? = null
) {
    Row {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = status.let {
                when (it) {
                    JobStatus.ACTIVE -> Icons.Rounded.Info
                    JobStatus.DONE -> Icons.Rounded.CheckCircle
                    JobStatus.CANCELED -> Icons.Rounded.Close
                }
            },
            contentDescription = "icon",
            tint = status.let {
                when (it) {
                    JobStatus.ACTIVE -> MaterialTheme.colorScheme.primary
                    JobStatus.DONE -> Color.Green
                    JobStatus.CANCELED -> Color.Red
                }
            }
        )
    }


    Spacer(modifier = Modifier.height(8.dp))

    Row {
        Icon(
            imageVector = Icons.Outlined.LocationOn,
            contentDescription = "location",
            tint = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = location,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
    person?.let {
        Row {
            Icon(
                imageVector = Icons.Outlined.Person,
                contentDescription = "person",
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = person,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
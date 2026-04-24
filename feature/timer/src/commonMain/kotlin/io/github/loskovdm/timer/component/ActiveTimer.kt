package io.github.loskovdm.timer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.loskovdm.designsystem.util.formatTimeToHmsString
import io.github.loskovdm.timer.model.Project
import io.github.loskovdm.timer.model.Task
import io.github.loskovdm.timer.model.TimeEntry
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.add_description
import timetracker.designsystem.generated.resources.ic_stop_filled
import timetracker.designsystem.generated.resources.stop
import timetracker.designsystem.generated.resources.stop_timer
import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun ActiveTimer(
    modifier: Modifier = Modifier,
    timeEntry: TimeEntry,
    duration: Duration,
    onClick: () -> Unit,
    onStop: () -> Unit,
) {
    val taskName = timeEntry.task?.name ?: stringResource(Res.string.add_description)
    val project = timeEntry.project

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(112.dp)
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = formatTimeToHmsString(duration.inWholeSeconds),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = taskName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                if (project != null) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(project.color))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = project.name,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            TextButton(
                onClick = onStop,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_stop_filled),
                    contentDescription = stringResource(Res.string.stop_timer)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = stringResource(Res.string.stop),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
fun PreviewActiveTimer(){
    val project1 = Project(
        id = Uuid.generateV7(),
        name = "Project 1",
        color = 0xff3ff2d1,
        isSynced = true,
    )
    val task1 = Task(
        id = Uuid.generateV7(),
        name = "Task 1",
        project = project1,
        isSynced = true,
    )
    val entry = TimeEntry(
        id = Uuid.generateV7(),
        startDateTime = Instant.parse("2026-04-06T10:00:00Z"),
        endDateTime = null,
        project = project1,
        task = task1,
        isSynced = true,
    )
    MaterialTheme {
        ActiveTimer(
            timeEntry = entry,
            duration = Instant.parse("2026-04-15T10:29:00Z") - Instant.parse("2026-04-15T10:00:00Z"),
            onClick = {},
            onStop = {}
        )
    }
}
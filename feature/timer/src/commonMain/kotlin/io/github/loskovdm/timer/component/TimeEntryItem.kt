package io.github.loskovdm.timer.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import timetracker.designsystem.generated.resources.ic_cached
import timetracker.designsystem.generated.resources.ic_start_filled
import timetracker.designsystem.generated.resources.not_synced
import timetracker.designsystem.generated.resources.synced
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Composable
fun TimeEntryItem(
    modifier: Modifier = Modifier,
    timeEntry: TimeEntry,
    onClick: () -> Unit,
    onStartTimerClick: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(4.dp),
    ) {
        Row(
            modifier = Modifier.height(76.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f).padding(16.dp)
            ) {
                val taskName = timeEntry.task?.name
                val project = timeEntry.project

                val isEmptyTask = taskName == null

                Text(
                    text = taskName ?: stringResource(Res.string.add_description),
                    style = if (isEmptyTask) {
                        MaterialTheme.typography.bodyMedium
                    } else {
                        MaterialTheme.typography.titleMedium
                    },
                    fontWeight = if (isEmptyTask) FontWeight.Normal else FontWeight.SemiBold,
                    color = if (isEmptyTask) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )

                if (project != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(Color(project.color))
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = project.name,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
            Button(
                modifier = Modifier,
                onClick = onStartTimerClick,
                colors = ButtonColors(
                    containerColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                ),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    val durationInSeconds = (timeEntry.endDateTime!! - timeEntry.startDateTime).inWholeSeconds
                    Text(
                        text = formatTimeToHmsString(durationInSeconds),
                        style = MaterialTheme.typography.titleSmall
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (!timeEntry.isSynced) {
                            Icon(
                                modifier = Modifier.size(16.dp),
                                imageVector = vectorResource(Res.drawable.ic_cached),
                                contentDescription = stringResource(Res.string.not_synced),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Icon(
                            modifier = Modifier.size(20.dp),
                            imageVector = vectorResource(Res.drawable.ic_start_filled),
                            contentDescription = stringResource(Res.string.synced),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
fun FilledTimeEntryItemPreview() {
    val testProject = Project(
        id = Uuid.generateV7(),
        name = "Test project",
        color = 0xff4ab0cf,
        isSynced = true,
    )
    val testTask = Task(
        id = Uuid.generateV7(),
        name = "Test task",
        project = testProject,
        isSynced = true,
    )
    val testTimeEntry = TimeEntry(
        id = Uuid.generateV7(),
        startDateTime = Instant.parse("2026-04-06T12:00:00Z"),
        endDateTime = Instant.parse("2026-04-06T13:00:00Z"),
        project = testProject,
        task = testTask,
        isSynced = true,
    )
    MaterialTheme {
        TimeEntryItem(
            timeEntry = testTimeEntry,
            onClick = {},
            onStartTimerClick = {},
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
fun NullTaskTimeEntryItemPreview() {
    val currentTime = Clock.System.now()
    val testProject = Project(
        id = Uuid.generateV7(),
        name = "Test project",
        color = 0xff4ab0cf,
        isSynced = true,
    )
    val testTask = null
    val testTimeEntry = TimeEntry(
        id = Uuid.generateV7(),
        startDateTime = Instant.parse("2026-04-06T12:00:00Z"),
        endDateTime = Instant.parse("2026-04-06T13:00:00Z"),
        project = testProject,
        task = testTask,
        isSynced = false,
    )
    MaterialTheme {
        TimeEntryItem(
            timeEntry = testTimeEntry,
            onClick = {},
            onStartTimerClick = {},
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
fun NullTimeEntryItemPreview() {
    val currentTime = Clock.System.now()
    val testProject = null
    val testTask = null
    val testTimeEntry = TimeEntry(
        id = Uuid.generateV7(),
        startDateTime = Instant.parse("2026-04-06T12:00:00Z"),
        endDateTime = Instant.parse("2026-04-06T13:00:00Z"),
        project = testProject,
        task = testTask,
        isSynced = true,
    )
    MaterialTheme {
        TimeEntryItem(
            timeEntry = testTimeEntry,
            onClick = {},
            onStartTimerClick = {},
        )
    }
}
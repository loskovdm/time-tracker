package io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.designsystem.local.LocalFabPadding
import io.github.loskovdm.designsystem.util.formatDateToString
import io.github.loskovdm.designsystem.util.formatTimeToHmsString
import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import io.github.loskovdm.timetracker.feature.tasks.api.model.Task
import io.github.loskovdm.timetracker.feature.timeentry.api.destination.TimeEntryEditorDestination
import io.github.loskovdm.timetracker.feature.timeentry.api.model.TimeEntry
import io.github.loskovdm.timetracker.feature.timeentry.api.model.TimeEntryWithRelations
import io.github.loskovdm.timetracker.feature.timeentry.api.presentation.TimerViewModel
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.add_description
import timetracker.designsystem.generated.resources.ic_start_filled
import timetracker.designsystem.generated.resources.ic_timer_outlined
import timetracker.designsystem.generated.resources.no_time_entries
import timetracker.designsystem.generated.resources.start_tracking_time_hint
import timetracker.designsystem.generated.resources.synced
import timetracker.designsystem.generated.resources.today
import timetracker.designsystem.generated.resources.yesterday
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
@Composable
internal fun TimeEntriesList(
    onTimeEntryClicked: (TimeEntryEditorDestination) -> Unit,
    listViewModel: TimeEntriesListViewModel = koinViewModel(),
    timerViewModel: TimerViewModel = koinInject(),
) {
    val listState by listViewModel.state.collectAsStateWithLifecycle()

    when (val currentState = listState) {
        TimeEntriesListState.Empty -> EmptyTimeEntriesList()
        is TimeEntriesListState.Loaded -> TimeEntriesList(
            timeEntriesList = currentState.completedTimeEntries,
            onTimeEntryClick = onTimeEntryClicked,
            onStartTimeEntry = { projectId, taskId ->
                timerViewModel.startTimer(projectId, taskId)
            },
        )
        TimeEntriesListState.Loading -> LoadingTimeEntriesList()
    }
}

@Composable
private fun EmptyTimeEntriesList(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(64.dp),
            imageVector = vectorResource(Res.drawable.ic_timer_outlined),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(Res.string.no_time_entries),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(Res.string.start_tracking_time_hint),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun LoadingTimeEntriesList(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize().
            background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center,
    ) {
        LoadingIndicator()
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun TimeEntriesList(
    modifier: Modifier = Modifier,
    timeEntriesList: List<TimeEntryWithRelations>,
    onTimeEntryClick: (TimeEntryEditorDestination) -> Unit,
    onStartTimeEntry: (projectId: Uuid?, taskId: Uuid?) -> Unit,
) {
    val timeZone = remember { TimeZone.currentSystemDefault() }

    val groupedEntries = remember(timeEntriesList, timeZone) {
        timeEntriesList.groupBy {
            it.timeEntry.startDateTime
                .toLocalDateTime(timeZone)
                .date
        }
    }

    val today = remember(timeZone) {
        Clock.System.now()
            .toLocalDateTime(timeZone)
            .date
    }

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 16.dp,
            end = 16.dp,
            bottom = LocalFabPadding.current.calculateBottomPadding()
        ),
    ) {
        groupedEntries.forEach { (date, timeEntriesWithRelations) ->
            val totalDurationInSeconds = timeEntriesWithRelations.sumOf { timeEntryWithRelations ->
                (timeEntryWithRelations.timeEntry.endDateTime!! -
                        timeEntryWithRelations.timeEntry.startDateTime).inWholeSeconds
            }

            item(
                key = "group-$date",
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    TimeEntriesGroupHeader(
                        date = date,
                        today = today,
                        totalDurationInSeconds = totalDurationInSeconds,
                    )
                    TimeEntriesGroup(
                        timeEntriesWithRelations = timeEntriesWithRelations,
                        onTimeEntryClick = onTimeEntryClick,
                        onStartTimeEntry = onStartTimeEntry,
                    )
                }
            }
        }
    }
}

@Composable
private fun TimeEntriesGroupHeader(
    modifier: Modifier = Modifier,
    date: LocalDate,
    today: LocalDate,
    totalDurationInSeconds: Long,
) {
    val yesterday = today.minus(DatePeriod(days = 1))
    val title = when (date) {
        today -> stringResource(Res.string.today)
        yesterday -> stringResource(Res.string.yesterday)
        else -> formatDateToString(date)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start,
        )
        Text(
            text = formatTimeToHmsString(totalDurationInSeconds),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End,
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
@Composable
private fun TimeEntriesGroup(
    modifier: Modifier = Modifier,
    timeEntriesWithRelations: List<TimeEntryWithRelations>,
    onTimeEntryClick: (TimeEntryEditorDestination) -> Unit,
    onStartTimeEntry: (projectId: Uuid?, taskId: Uuid?) -> Unit,
) {
    Card(
        modifier = modifier.padding(bottom = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(1.dp),
        ) {
            timeEntriesWithRelations.forEach { timeEntryWithRelations ->
                TimeEntryItem(
                    timeEntryWithRelations = timeEntryWithRelations,
                    onClick = {
                        val timeEntryEditorDestination = TimeEntryEditorDestination(
                            timeEntryId = timeEntryWithRelations.timeEntry.id,
                        )
                        onTimeEntryClick(timeEntryEditorDestination)
                    },
                    onStart = {
                        onStartTimeEntry(
                            timeEntryWithRelations.project?.id,
                            timeEntryWithRelations.task?.id,
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun TimeEntryItem(
    modifier: Modifier = Modifier,
    timeEntryWithRelations: TimeEntryWithRelations,
    onClick: () -> Unit,
    onStart: () -> Unit,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        shape = RoundedCornerShape(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
    ) {
        Row(
            modifier = Modifier.height(76.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TimeEntryInfo(
                modifier = Modifier.weight(1f),
                timeEntryWithRelations = timeEntryWithRelations,
            )
            TimeEntryAction(
                timeEntry = timeEntryWithRelations.timeEntry,
                onStart = onStart,
            )
        }
    }
}

@Composable
private fun TimeEntryInfo(
    modifier: Modifier = Modifier,
    timeEntryWithRelations: TimeEntryWithRelations,
) {
    Column(
        modifier = modifier.padding(16.dp)
    ) {
        TimeEntryTask(task = timeEntryWithRelations.task)
        TimeEntryProject(project = timeEntryWithRelations.project)
    }
}

@Composable
private fun TimeEntryTask(
    modifier: Modifier = Modifier,
    task: Task?,
) {
    val taskName = task?.name
    val isEmptyTask = taskName == null

    Text(
        modifier = modifier,
        text = taskName ?: stringResource(Res.string.add_description),
        style = if (isEmptyTask) {
            MaterialTheme.typography.bodyMedium
        } else {
            MaterialTheme.typography.titleMedium
        },
        fontWeight = if (isEmptyTask) {
            FontWeight.Normal
        } else {
            FontWeight.SemiBold
        },
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun TimeEntryProject(
    modifier: Modifier = Modifier,
    project: Project?,
) {
    if (project == null) return

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(Color(project.color))
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = project.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun TimeEntryAction(
    modifier: Modifier = Modifier,
    timeEntry: TimeEntry,
    onStart: () -> Unit,
) {
    Button(
        modifier = modifier.fillMaxHeight(),
        onClick = onStart,
        shape = RoundedCornerShape(4.dp),
        contentPadding = PaddingValues(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurface,
        ),
    ) {
        TimeEntryButtonContent(
            timeEntry = timeEntry,
        )
    }
}

@Composable
fun TimeEntryButtonContent(
    modifier: Modifier = Modifier,
    timeEntry: TimeEntry,
) {
    Column(
        horizontalAlignment = Alignment.End
    ) {
        val durationSeconds = (timeEntry.endDateTime!! - timeEntry.startDateTime).inWholeSeconds

        Text(
            modifier = modifier,
            text = formatTimeToHmsString(durationSeconds)
        )
        Icon(
            modifier = Modifier.size(20.dp),
            imageVector = vectorResource(Res.drawable.ic_start_filled),
            contentDescription = stringResource(Res.string.synced),
            tint = MaterialTheme.colorScheme.onSurface,
        )
    }
}
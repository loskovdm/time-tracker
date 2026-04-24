package io.github.loskovdm.timer

//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyListState
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.material3.TopAppBar
//import androidx.compose.material3.TopAppBarColors
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.TopAppBarScrollBehavior
//import androidx.compose.material3.WideNavigationRailState
//import androidx.compose.material3.rememberTopAppBarState
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.input.nestedscroll.nestedScroll
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.navigation3.runtime.NavKey
//import io.github.loskovdm.designsystem.component.HomeScreen
//import io.github.loskovdm.designsystem.navigation.NavigationItem
//import io.github.loskovdm.designsystem.util.DeviceConfiguration
//import io.github.loskovdm.designsystem.util.formatDateToString
//import io.github.loskovdm.designsystem.util.formatTimeToHmsString
//import io.github.loskovdm.timer.component.TimeEntryItem
//import kotlinx.datetime.DatePeriod
//import kotlinx.datetime.LocalDate
//import kotlinx.datetime.TimeZone
//import kotlinx.datetime.minus
//import kotlinx.datetime.toLocalDateTime
//import kotlinx.datetime.todayIn
//import org.jetbrains.compose.resources.painterResource
//import org.jetbrains.compose.resources.stringResource
//import org.koin.compose.viewmodel.koinViewModel
//import timetracker.designsystem.generated.resources.Res
//import timetracker.designsystem.generated.resources.add_entry
//import timetracker.designsystem.generated.resources.ic_add_entry
//import timetracker.designsystem.generated.resources.ic_settings_outlined
//import timetracker.designsystem.generated.resources.settings
//import timetracker.designsystem.generated.resources.timer
//import timetracker.designsystem.generated.resources.today
//import timetracker.designsystem.generated.resources.yesterday
//import kotlin.time.Clock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.NavKey
import io.github.loskovdm.designsystem.component.ErrorDialog
import io.github.loskovdm.designsystem.component.HomeScreen
import io.github.loskovdm.designsystem.navigation.NavigationItem
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.designsystem.util.formatDateToString
import io.github.loskovdm.designsystem.util.formatTimeToHmsString
import io.github.loskovdm.timer.component.ActiveTimer
import io.github.loskovdm.timer.component.TimeEntryEditor
import io.github.loskovdm.timer.component.TimeEntryItem
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.add_entry
import timetracker.designsystem.generated.resources.ic_add_entry
import timetracker.designsystem.generated.resources.ic_settings_outlined
import timetracker.designsystem.generated.resources.ic_start_filled
import timetracker.designsystem.generated.resources.ic_stop_filled
import timetracker.designsystem.generated.resources.settings
import timetracker.designsystem.generated.resources.start_timer
import timetracker.designsystem.generated.resources.stop_timer
import timetracker.designsystem.generated.resources.timer
import timetracker.designsystem.generated.resources.today
import timetracker.designsystem.generated.resources.yesterday
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun TimerScreen(
    modifier: Modifier = Modifier,
    viewModel: TimerViewModel = koinViewModel(),
    railState: WideNavigationRailState,
    deviceConfiguration: DeviceConfiguration,
    navigationItems: Map<NavKey, NavigationItem>,
    selectedNavigationItem: NavKey,
    onSelectedNavigationItem: (NavKey) -> Unit,
    onSettings: () -> Unit,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val lazyListState = remember { LazyListState() }
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val zone = TimeZone.currentSystemDefault()

    state.errorMessage?.let { message ->
        ErrorDialog(
            message = message,
            onDismiss = viewModel::dismissError,
        )
    }

    HomeScreen(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        deviceConfiguration = deviceConfiguration,
        topBar = {
            TimerTopBar(
                deviceConfiguration = deviceConfiguration,
                scrollBehavior = scrollBehavior,
                onAddEntry = {
                    viewModel.openNewTimeEntryEditor()
                },
                onSettings = onSettings,
            )
        },
        railState = railState,
        lazyListState = lazyListState,
        navigationItems = navigationItems,
        selectedNavigationItem = selectedNavigationItem,
        onSelectedNavigationItem = onSelectedNavigationItem,
        iconFloutingActionButton = if (state.activeTimeEntry == null) {
            Res.drawable.ic_start_filled
        } else {
            Res.drawable.ic_stop_filled
        },
        labelFloutingActionButton = if (state.activeTimeEntry == null) {
            Res.string.start_timer
        } else {
            Res.string.stop_timer
        },
        onClickFloutingActionButton = {
            if (state.activeTimeEntry == null) {
                viewModel.startNewTimerAndOpenEditor()
            } else {
                viewModel.stopActiveTimer()
            }
        },
    ) {
        val today = remember(zone) { Clock.System.todayIn(zone) }
        val yesterday = remember(today) { today.minus(DatePeriod(days = 1)) }

        val groupedEntries = remember(state.completedTimeEntries, zone) {
            state.completedTimeEntries
                .sortedByDescending { it.startDateTime }
                .groupBy { it.startDateTime.toLocalDateTime(zone).date }
        }

        state.timeEntryEditor?.let { timeEntryEditorUiState ->
            TimeEntryEditor(
                deviceConfiguration = deviceConfiguration,
                timeEntryEditorUiState = timeEntryEditorUiState,
                timerValue = if (timeEntryEditorUiState.timeEntryId == state.activeTimeEntry?.entry?.id) {
                    state.activeTimeEntry?.duration ?: Duration.ZERO
                } else {
                    val startDateTime = LocalDateTime(
                        date = timeEntryEditorUiState.startDate,
                        time = timeEntryEditorUiState.startTime
                    )

                    timeEntryEditorUiState.endDate?.let { endDate ->
                        timeEntryEditorUiState.endTime?.let { endTime ->

                            val endDateTime = LocalDateTime(endDate, endTime)

                            val startInstant = startDateTime.toInstant(zone)
                            val endInstant = endDateTime.toInstant(zone)

                            endInstant - startInstant
                        }
                    } ?: Duration.ZERO
                },
                projects = timeEntryEditorUiState.projects,
                tasksForSelectedProject = timeEntryEditorUiState.tasks.filter { task ->
                    task.project.id == timeEntryEditorUiState.project?.id
                },
                onDismissRequest = {
                    viewModel.closeTimeEntryEditor()
                },
                onSaveClick = {
                    if (timeEntryEditorUiState.timeEntryId == null) {
                        viewModel.saveNewTimeEntry(
                            startTime = timeEntryEditorUiState.startTime,
                            startDate = timeEntryEditorUiState.startDate,
                            endTime = timeEntryEditorUiState.endTime!!,
                            endDate = timeEntryEditorUiState.endDate!!,
                            project = timeEntryEditorUiState.project,
                            task = timeEntryEditorUiState.task
                        )
                    } else {
                        viewModel.saveEditedTimeEntry(
                            id = timeEntryEditorUiState.timeEntryId,
                            startTime = timeEntryEditorUiState.startTime,
                            startDate = timeEntryEditorUiState.startDate,
                            endTime = timeEntryEditorUiState.endTime,
                            endDate = timeEntryEditorUiState.endDate,
                            project = timeEntryEditorUiState.project,
                            task = timeEntryEditorUiState.task
                        )
                    }
                    viewModel.closeTimeEntryEditor()
                },
                onDeleteClick = {
                    timeEntryEditorUiState.timeEntryId?.let {
                        viewModel.deleteTimeEntry(
                            id = timeEntryEditorUiState.timeEntryId,
                            startTime = timeEntryEditorUiState.startTime,
                            startDate = timeEntryEditorUiState.startDate,
                            endTime = timeEntryEditorUiState.endTime,
                            endDate = timeEntryEditorUiState.endDate,
                            project = timeEntryEditorUiState.project,
                            task = timeEntryEditorUiState.task
                        )
                    }
                    viewModel.closeTimeEntryEditor()
                },
                onStartDateChange = viewModel::updateStartDate,
                onStartTimeChange = viewModel::updateStartTime,
                onEndDateChange = { value ->
                    value?.let(viewModel::updateFinishDate)
                },
                onEndTimeChange = { value ->
                    value?.let(viewModel::updateFinishTime)
                },
                onProjectChange = viewModel::updateProject,
                onTaskChange = viewModel::updateTask,
            )
        }

        LazyColumn(
            modifier = modifier,
            verticalArrangement = Arrangement.spacedBy(4.dp),
            state = lazyListState,
            contentPadding = PaddingValues(
                top = 16.dp,
                start = 16.dp,
                end = 16.dp,
                bottom = when (deviceConfiguration) {
                    DeviceConfiguration.DESKTOP -> 8.dp
                    else -> 76.dp
                }
            )
        ) {
            state.activeTimeEntry?.let { activeTimeEntryUiState ->
                item(key = "running-timer") {
                    ActiveTimer(
                        timeEntry = activeTimeEntryUiState.entry,
                        duration = activeTimeEntryUiState.duration,
                        onClick = {
                            viewModel.openEditorFromEntry(activeTimeEntryUiState.entry)
                        },
                        onStop = {
                            viewModel.stopActiveTimer()
                        },
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
            }

            groupedEntries.forEach { (date, entries) ->
                val totalSeconds = entries.sumOf { entry ->
                    (entry.endDateTime!! - entry.startDateTime).inWholeSeconds
                }
                item(key = "group-$date") {
                    Column(
                        modifier = Modifier.padding(bottom = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        TimeEntriesGroupHeader(
                            date = date,
                            today = today,
                            yesterday = yesterday,
                            totalSeconds = totalSeconds,
                        )
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color.Transparent,
                            ),
                        ) {
                            entries.forEach { timeEntry ->
                                Column(
                                    modifier = Modifier.padding(vertical = 1.dp)
                                ) {
                                    TimeEntryItem(
                                        timeEntry = timeEntry,
                                        onClick = {
                                            viewModel.openEditorFromEntry(timeEntry)
                                        },
                                        onStartTimerClick = {
                                            viewModel.startTimerFromEntry(timeEntry)
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimeEntriesGroupHeader(
    date: LocalDate,
    today: LocalDate,
    yesterday: LocalDate,
    totalSeconds: Long,
    modifier: Modifier = Modifier,
) {
    val title = when (date) {
        today -> stringResource(Res.string.today)
        yesterday -> stringResource(Res.string.yesterday)
        else -> formatDateToString(date)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start,
        )
        Text(
            text = formatTimeToHmsString(totalSeconds),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.End,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerTopBar(
    modifier: Modifier = Modifier,
    deviceConfiguration: DeviceConfiguration,
    scrollBehavior: TopAppBarScrollBehavior,
    onAddEntry: () -> Unit,
    onSettings: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(stringResource(Res.string.timer))
        },
        actions = {
            IconButton(
                onClick = onAddEntry
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add_entry),
                    contentDescription = stringResource(Res.string.add_entry)
                )
            }
            IconButton(
                onClick = onSettings,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_settings_outlined),
                    contentDescription = stringResource(Res.string.settings)
                )
            }
        },
        colors = TopAppBarColors(
            containerColor = if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            },
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            subtitleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        scrollBehavior = scrollBehavior,
    )
}

@Preview
@Composable
fun PreviewTimerScreen() {
    val railState = rememberWideNavigationRailState()
    val deviceConfiguration = DeviceConfiguration.DESKTOP

    MaterialTheme {
        MaterialTheme {
            TimerScreen(
                railState = railState,
                deviceConfiguration = deviceConfiguration,
                navigationItems = emptyMap(),
                selectedNavigationItem = object : NavKey {},
                onSelectedNavigationItem = {},
                onSettings = {},
            )
        }
    }
}




//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TimerScreen(
//    modifier: Modifier = Modifier,
//    viewModel: TimerViewModel = koinViewModel(),
//    railState: WideNavigationRailState,
//    deviceConfiguration: DeviceConfiguration,
//    navigationItems: Map<NavKey, NavigationItem>,
//    selectedNavigationItem: NavKey,
//    onSelectedNavigationItem: (NavKey) -> Unit,
//    onSettings: () -> Unit,
//) {
//    val state by viewModel.uiState.collectAsStateWithLifecycle()
//
//    val lazyListState = remember { LazyListState() }
//    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
//
//    val zone = TimeZone.currentSystemDefault()
//    val today = remember(zone) { Clock.System.todayIn(zone) }
//    val yesterday = remember(today) { today.minus(DatePeriod(days = 1)) }
//    val groupedEntries = remember(state.completedTimeEntries, zone) {
//        state.completedTimeEntries
//            .sortedByDescending { it.startDateTime }
//            .groupBy { it.startDateTime.toLocalDateTime(zone).date }
//    }
//
//    HomeScreen(
//        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
//        deviceConfiguration = deviceConfiguration,
//        topBar = {
//            TimerTopBar(
//                deviceConfiguration = deviceConfiguration,
//                scrollBehavior = scrollBehavior,
//                onAddEntry = {},
//                onSettings = onSettings,
//            )
//        },
//        railState = railState,
//        lazyListState = lazyListState,
//        navigationItems = navigationItems,
//        selectedNavigationItem = selectedNavigationItem,
//        onSelectedNavigationItem = onSelectedNavigationItem,
//        iconFloutingActionButton = null,
//        labelFloutingActionButton = null,
//        onClickFloutingActionButton = {},
//    ) {
//        LazyColumn(
//            modifier = modifier,
//            verticalArrangement = Arrangement.spacedBy(4.dp),
//            state = lazyListState,
//            contentPadding = PaddingValues(
//                top = 16.dp,
//                start = 16.dp,
//                end = 16.dp,
//                bottom = when (deviceConfiguration) {
//                    DeviceConfiguration.DESKTOP -> 8.dp
//                    else -> 76.dp
//                }
//            )
//        ) {
////            state.activeTimeEntry?.let { activeTimeEntryUiState ->
////                item(key = "running-timer") {
////                    ActiveTimer(
////                        timeEntry = activeTimeEntryUiState.entry,
////                        elapsed = activeTimeEntryUiState.elapsed,
////                        onClick = {
////                            viewModel.openEditorFromEntry(activeTimeEntryUiState.entry)
////                        },
////                        onStop = {
////                            viewModel.stopActiveTimer()
////                        },
////                        modifier = Modifier.padding(bottom = 8.dp)
////                    )
////                }
////            }
//
//            groupedEntries.forEach { (date, entries) ->
//                val totalSeconds = entries.sumOf { entry ->
//                    (entry.endDateTime!! - entry.startDateTime).inWholeSeconds
//                }
//                item(key = "group-$date") {
//                    Column(
//                        modifier = Modifier.padding(bottom = 8.dp),
//                        verticalArrangement = Arrangement.spacedBy(4.dp)
//                    ) {
//                        TimeEntriesGroupHeader(
//                            date = date,
//                            today = today,
//                            yesterday = yesterday,
//                            totalSeconds = totalSeconds,
//                        )
//                        Card(
//                            shape = RoundedCornerShape(16.dp),
//                            colors = CardDefaults.cardColors(
//                                containerColor = Color.Transparent,
//                            ),
//                        ) {
//                            entries.forEach { timeEntry ->
//                                Column(
//                                    modifier = Modifier.padding(vertical = 1.dp)
//                                ) {
//                                    TimeEntryItem(
//                                        timeEntry = timeEntry,
//                                        onClick = {
//
//                                        },
//                                        onStartTimerClick = {
//
//                                        },
//                                    )
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TimerTopBar(
//    modifier: Modifier = Modifier,
//    deviceConfiguration: DeviceConfiguration,
//    scrollBehavior: TopAppBarScrollBehavior,
//    onAddEntry: () -> Unit,
//    onSettings: () -> Unit,
//) {
//    TopAppBar(
//        modifier = modifier,
//        title = {
//            Text(stringResource(Res.string.timer))
//        },
//        actions = {
//            IconButton(
//                onClick = onAddEntry
//            ) {
//                Icon(
//                    painter = painterResource(Res.drawable.ic_add_entry),
//                    contentDescription = stringResource(Res.string.add_entry)
//                )
//            }
//            IconButton(
//                onClick = onSettings,
//            ) {
//                Icon(
//                    painter = painterResource(Res.drawable.ic_settings_outlined),
//                    contentDescription = stringResource(Res.string.settings)
//                )
//            }
//        },
//        colors = TopAppBarColors(
//            containerColor = if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT) {
//                MaterialTheme.colorScheme.surface
//            } else {
//                MaterialTheme.colorScheme.surfaceContainer
//            },
//            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
//            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
//            titleContentColor = MaterialTheme.colorScheme.onSurface,
//            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
//            subtitleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
//        ),
//        scrollBehavior = scrollBehavior,
//    )
//}
//
//@Composable
//fun TimeEntriesGroupHeader(
//    date: LocalDate,
//    today: LocalDate,
//    yesterday: LocalDate,
//    totalSeconds: Long,
//    modifier: Modifier = Modifier,
//) {
//    val title = when (date) {
//        today -> stringResource(Res.string.today)
//        yesterday -> stringResource(Res.string.yesterday)
//        else -> formatDateToString(date)
//    }
//
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(start = 8.dp, end = 8.dp),
//        horizontalArrangement = Arrangement.SpaceBetween
//    ) {
//        Text(
//            text = title,
//            style = MaterialTheme.typography.titleSmall,
//            color = MaterialTheme.colorScheme.onSurfaceVariant,
//            textAlign = TextAlign.Start,
//        )
//        Text(
//            text = formatTimeToHmsString(totalSeconds),
//            style = MaterialTheme.typography.titleSmall,
//            color = MaterialTheme.colorScheme.onSurfaceVariant,
//            textAlign = TextAlign.End,
//        )
//    }
//}
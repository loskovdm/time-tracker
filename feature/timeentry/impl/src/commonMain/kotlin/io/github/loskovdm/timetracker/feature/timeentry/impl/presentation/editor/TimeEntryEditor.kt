package io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.editor

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CalendarLocale
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerDialogDefaults
import androidx.compose.material3.TimePickerDisplayMode
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.loskovdm.designsystem.component.EditorHeader
import io.github.loskovdm.designsystem.component.TimeEntryProject
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.designsystem.util.formatDateToString
import io.github.loskovdm.designsystem.util.formatLocalTimeToHmString
import io.github.loskovdm.designsystem.util.formatTimeToHmsString
import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import io.github.loskovdm.timetracker.feature.projects.api.presentation.ActiveProjectsListViewModel
import io.github.loskovdm.timetracker.feature.projects.api.presentation.ProjectsListState
import io.github.loskovdm.timetracker.feature.tasks.api.model.Task
import io.github.loskovdm.timetracker.feature.tasks.api.presentation.ActiveTasksListViewModel
import io.github.loskovdm.timetracker.feature.tasks.api.presentation.TasksListState
import io.github.loskovdm.timetracker.feature.timeentry.api.presentation.TimerViewModel
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.cancel
import timetracker.designsystem.generated.resources.delete
import timetracker.designsystem.generated.resources.end
import timetracker.designsystem.generated.resources.ic_arrow_drop_down
import timetracker.designsystem.generated.resources.ic_arrow_drop_up
import timetracker.designsystem.generated.resources.ic_calendar_outlined
import timetracker.designsystem.generated.resources.ic_projects_outlined
import timetracker.designsystem.generated.resources.ic_task_outlined
import timetracker.designsystem.generated.resources.no_projects
import timetracker.designsystem.generated.resources.no_tasks
import timetracker.designsystem.generated.resources.ok
import timetracker.designsystem.generated.resources.project
import timetracker.designsystem.generated.resources.select_project
import timetracker.designsystem.generated.resources.select_task
import timetracker.designsystem.generated.resources.start
import timetracker.designsystem.generated.resources.stop_timer
import timetracker.designsystem.generated.resources.task
import timetracker.designsystem.generated.resources.without_project
import timetracker.designsystem.generated.resources.without_task
import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalUuidApi::class)
@Composable
internal fun TimeEntryEditor(
    onClose: () -> Unit,
    editorViewModel: TimeEntryEditorViewModel,
    timerViewModel: TimerViewModel = koinInject(),
    projectsListViewModel: ActiveProjectsListViewModel = koinViewModel(),
) {
    val editorState by editorViewModel.state.collectAsStateWithLifecycle()
    val projectsListState by projectsListViewModel.state.collectAsStateWithLifecycle()

    val zone = TimeZone.currentSystemDefault()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        EditorHeader(
            onClose = onClose,
            onSave = {
                editorViewModel.saveTimeEntry()
                onClose()
            },
            isAvailableSave = editorState.isDateTimeValid,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            DurationSection(
                duration = editorState.duration,
                isDateTimeValid = editorState.isDateTimeValid,
            )
            DateTimeSection(
                modifier = Modifier
                    .height(
                        if (editorState.endDateTime != null) {
                            75.dp
                        } else {
                            60.dp
                        }
                    ),
                startLocalDateTime = editorState.startDateTime.toLocalDateTime(zone),
                endLocalDateTime = editorState.endDateTime?.toLocalDateTime(zone),
                onStartLocalDateTimeChange = { changedLocalDateTime ->
                    editorViewModel.changeStartDateTime(
                        changedLocalDateTime.toInstant(zone)
                    )
                },
                onEndLocalDateTimeChange = { changedLocalDateTime ->
                    editorViewModel.changeEndDateTime(
                        changedLocalDateTime.toInstant(zone)
                    )
                }
            )
            ProjectSection(
                modifier = Modifier
                    .height(60.dp)
                    .padding(top = if (editorState.endDateTime != null) {
                        2.dp
                    } else {
                        0.dp
                    }),
                projectsListState = projectsListState,
                currentProject = editorState.project,
                onSelect = editorViewModel::selectProject,
                removeProject = {
                    editorViewModel.selectProject(project = null)
                }
            )
            TaskSection(
                modifier = Modifier.height(60.dp),
                currentProject = editorState.project,
                currentTask = editorState.task,
                onSelectTask = editorViewModel::selectTask,
                onRemoveTask = {
                    editorViewModel.selectTask(task = null)
                },
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
            ActionSection(
                title = if (editorState.isNewEntry) {
                    stringResource(Res.string.cancel)
                } else {
                    if (editorState.endDateTime == null) {
                        stringResource(Res.string.stop_timer)
                    } else {
                        stringResource(Res.string.delete)
                    }
                },
                onBack = onClose,
                onClick = {
                    if (editorState.isNewEntry) {
                        onClose()
                    } else {
                        if (editorState.endDateTime == null) {
                            timerViewModel.stopTimer()
                        } else {
                            editorViewModel.deleteTimeEntry()
                        }
                    }
                },
            )
        }
    }
}

@Composable
private fun DurationSection(
    modifier: Modifier = Modifier,
    duration: Duration,
    isDateTimeValid: Boolean,
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = formatTimeToHmsString(duration.inWholeSeconds),
        color = if (isDateTimeValid) {
            MaterialTheme.colorScheme.onSurface
        } else {
            MaterialTheme.colorScheme.error
        },
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.SemiBold,
    )
}

@Composable
private fun DateTimeSection(
    modifier: Modifier = Modifier,
    startLocalDateTime: LocalDateTime,
    endLocalDateTime: LocalDateTime?,
    onStartLocalDateTimeChange: (LocalDateTime) -> Unit,
    onEndLocalDateTimeChange: (LocalDateTime) -> Unit,
) {
    val isVisibleEndDateTimeSelector = endLocalDateTime != null

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = vectorResource(Res.drawable.ic_calendar_outlined),
            contentDescription = null,
        )
        Column {
            val titleWidth = 50.dp

            DateTimeSelector(
                title = stringResource(Res.string.start),
                titleWidth = titleWidth,
                currentTime = startLocalDateTime.time,
                currentDate = startLocalDateTime.date,
                onTimeChange = { changedLocalTime ->
                    onStartLocalDateTimeChange(
                        LocalDateTime(
                            date = startLocalDateTime.date,
                            time = changedLocalTime,
                        )
                    )
                },
                onDateChange = { changedLocalDate ->
                    onStartLocalDateTimeChange(
                        LocalDateTime(
                            date = changedLocalDate,
                            time = startLocalDateTime.time,
                        )
                    )
                },
            )
            if (isVisibleEndDateTimeSelector) {
                DateTimeSelector(
                    title = stringResource(Res.string.end),
                    titleWidth = titleWidth,
                    currentTime = endLocalDateTime.time,
                    currentDate = endLocalDateTime.date,
                    onTimeChange = { changedLocalTime ->
                        onEndLocalDateTimeChange(
                            LocalDateTime(
                                date = endLocalDateTime.date,
                                time = changedLocalTime,
                            )
                        )
                    },
                    onDateChange = { changedLocalDate ->
                        onEndLocalDateTimeChange(
                            LocalDateTime(
                                date = changedLocalDate,
                                time = endLocalDateTime.time,
                            )
                        )
                    },
                )
            }
        }
    }
}

@Composable
private fun DateTimeSelector(
    modifier: Modifier = Modifier,
    title: String,
    titleWidth: Dp,
    currentTime: LocalTime,
    currentDate: LocalDate,
    onTimeChange: (LocalTime) -> Unit,
    onDateChange: (LocalDate) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.width(titleWidth),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            text = title,
        )
        TimeSelector(
            currentTime = currentTime,
            onChange = onTimeChange,
        )
        Spacer(modifier = Modifier.weight(1f))
        DateSelector(
            currentDate = currentDate,
            onChange = onDateChange,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeSelector(
    modifier: Modifier = Modifier,
    currentTime: LocalTime,
    onChange: (LocalTime) -> Unit,
) {
    val isTimePickerVisible = rememberSaveable { mutableStateOf(false) }

    TextButton(
        modifier = modifier,
        onClick = {
            isTimePickerVisible.value = true
        },
        contentPadding = PaddingValues(horizontal = 4.dp),
    ) {
        Text(
            text = formatLocalTimeToHmString(currentTime),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    if (isTimePickerVisible.value) {
        var displayMode by remember { mutableStateOf(TimePickerDisplayMode.Picker) }
        val deviceConfiguration = LocalDeviceConfiguration.current

        val timePickerState = remember(currentTime) {
            TimePickerState(
                initialHour = currentTime.hour,
                initialMinute = currentTime.minute,
                is24Hour = true,
            )
        }

        TimePickerDialog(
            modifier = if (deviceConfiguration == DeviceConfiguration.TABLET_LANDSCAPE) {
                Modifier.heightIn(max = 400.dp)
            } else {
                Modifier
            },
            title = {
                TimePickerDialogDefaults.Title(displayMode = TimePickerDisplayMode.Picker)
            },
            onDismissRequest = {
                isTimePickerVisible.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onChange(
                            LocalTime(
                                hour = timePickerState.hour,
                                minute = timePickerState.minute,
                                second = 0,
                                nanosecond = 0,
                            )
                        )
                        isTimePickerVisible.value = false
                    }
                ) {
                    Text(
                        stringResource(Res.string.ok)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isTimePickerVisible.value = false
                    }
                ) {
                    Text(
                        stringResource(Res.string.cancel)
                    )
                }
            },
            modeToggleButton = {
                if (deviceConfiguration != DeviceConfiguration.DESKTOP) {
                    TimePickerDialogDefaults.DisplayModeToggle(
                        onDisplayModeChange = {
                            displayMode =
                                if (displayMode == TimePickerDisplayMode.Picker) {
                                    TimePickerDisplayMode.Input
                                } else {
                                    TimePickerDisplayMode.Picker
                                }
                        },
                        displayMode = displayMode,
                    )
                }
            },
        ) {
            if (displayMode == TimePickerDisplayMode.Input
                || deviceConfiguration == DeviceConfiguration.DESKTOP) {
                TimeInput(state = timePickerState)
            } else {
                TimePicker(state = timePickerState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateSelector(
    modifier: Modifier = Modifier,
    currentDate: LocalDate,
    onChange: (LocalDate) -> Unit,
) {
    val zone = TimeZone.UTC
    val isDatePickerVisible = rememberSaveable { mutableStateOf(false) }

    TextButton(
        modifier = modifier,
        onClick = {
            isDatePickerVisible.value = true
        },
        contentPadding = PaddingValues(horizontal = 4.dp),
    ) {
        Text(
            text = formatDateToString(currentDate),
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    if (isDatePickerVisible.value) {
        val deviceConfiguration = LocalDeviceConfiguration.current
        val datePickerState = remember(currentDate) {
            DatePickerState(
                locale = CalendarLocale.getDefault(),
                initialSelectedDateMillis = currentDate
                    .atStartOfDayIn(zone)
                    .toEpochMilliseconds(),
                initialDisplayMode = if (deviceConfiguration == DeviceConfiguration.MOBILE_LANDSCAPE) {
                    DisplayMode.Input
                } else {
                    DisplayMode.Picker
                }
            )
        }

        DatePickerDialog(
            onDismissRequest = { isDatePickerVisible.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            onChange(
                                Instant
                                    .fromEpochMilliseconds(millis)
                                    .toLocalDateTime(zone)
                                    .date
                            )
                        }
                        isDatePickerVisible.value = false
                    }
                ) {
                    Text(
                        stringResource(Res.string.ok)
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        isDatePickerVisible.value = false
                    }
                ) {
                    Text(
                        stringResource(Res.string.cancel)
                    )
                }
            },
        ) {
            Column(modifier.verticalScroll(rememberScrollState())) {
                DatePicker(state = datePickerState)
            }
        }
    }
}

@Composable
private fun ProjectSection(
    modifier: Modifier = Modifier,
    projectsListState: ProjectsListState,
    currentProject: Project?,
    onSelect: (Project) -> Unit,
    removeProject: () -> Unit,
) {
    val isVisibleMenu = rememberSaveable { mutableStateOf(false) }

    Box {
        ProjectSelector(
            modifier = modifier,
            currentProject = currentProject,
            onClick = {
                isVisibleMenu.value = !isVisibleMenu.value
            },
            isVisibleMenu = isVisibleMenu.value,
        )

        val maxVisibleItems = 5
        DropdownMenu(
            modifier = Modifier
                .widthIn(max = 600.dp)
                .heightIn(max = (48 * maxVisibleItems).dp),
            expanded = isVisibleMenu.value,
            onDismissRequest = { isVisibleMenu.value = false },
            shape = RoundedCornerShape(16.dp)
        ) {
            ProjectsMenuContent(
                projectsListState = projectsListState,
                currentProject = currentProject,
                onClick = onSelect,
                removeProject = removeProject,
                onClose = { isVisibleMenu.value = false }
            )
        }
    }
}

@Composable
private fun ProjectSelector(
    modifier: Modifier = Modifier,
    currentProject: Project?,
    onClick: () -> Unit,
    isVisibleMenu: Boolean,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            pressedElevation = 0.dp
        ),
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_projects_outlined),
                contentDescription = null,
            )
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(Res.string.project),
                    style = MaterialTheme.typography.bodyLarge
                )
                if (currentProject == null) {
                    Text(
                        text = stringResource(Res.string.select_project),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    TimeEntryProject(
                        name = currentProject.name,
                        color = currentProject.color,
                    )
                }
            }
            Icon(
                imageVector = if (isVisibleMenu) {
                    vectorResource(Res.drawable.ic_arrow_drop_up)
                } else {
                    vectorResource(Res.drawable.ic_arrow_drop_down)
                },
                contentDescription = null,
            )
        }
    }
}

@Composable
private fun ProjectsMenuContent(
    projectsListState: ProjectsListState,
    currentProject: Project?,
    onClick: (Project) -> Unit,
    removeProject: () -> Unit,
    onClose: () -> Unit,
) {
    when (projectsListState) {
        ProjectsListState.Loading -> {}
        is ProjectsListState.Empty -> {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(Res.string.no_projects),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                onClick = {},
                enabled = false,
            )
        }
        is ProjectsListState.Loaded -> {
            if (currentProject != null) {
                DropdownMenuItem(
                    text = {
                        TimeEntryProject(
                            name = stringResource(Res.string.without_project),
                            color = 0xFF808080,
                        )
                    },
                    onClick = {
                        removeProject()
                        onClose()
                    },
                )
            }
            projectsListState.projects
                .filter { project -> project != currentProject }
                .forEach { project ->
                    DropdownMenuItem(
                        text = {
                            TimeEntryProject(
                                name = project.name,
                                color = project.color,
                            )
                        },
                        onClick = {
                            onClick(project)
                            onClose()
                        }
                    )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
private fun TaskSection(
    modifier: Modifier = Modifier,
    currentProject: Project?,
    currentTask: Task?,
    onSelectTask: (Task) -> Unit,
    onRemoveTask: () -> Unit,
) {
    val isMenuExpanded = rememberSaveable { mutableStateOf(false) }
    val isEnabled = currentProject != null

    val activeTasksViewModel = if (isEnabled) {
        val projectId = currentProject.id
        koinViewModel<ActiveTasksListViewModel>(
            key = projectId.toString(),
            parameters = { parametersOf(projectId) }
        )
    } else null

    val tasksState by activeTasksViewModel?.state?.collectAsStateWithLifecycle()
        ?: remember { mutableStateOf(null) }

    Box {
        TaskSelector(
            modifier = modifier,
            currentTask = currentTask,
            isEnabled = isEnabled,
            onClick = {
                if (isEnabled) isMenuExpanded.value = !isMenuExpanded.value
            },
            isMenuExpanded = isMenuExpanded.value,
        )

        if (isEnabled && tasksState != null) {
            val maxVisibleItems = 5
            DropdownMenu(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .heightIn(max = (48 * maxVisibleItems).dp),
                expanded = isMenuExpanded.value,
                onDismissRequest = { isMenuExpanded.value = false },
                shape = RoundedCornerShape(16.dp)
            ) {
                TaskMenuContent(
                    tasksState = tasksState!!,
                    currentTask = currentTask,
                    onSelectTask = onSelectTask,
                    onClearTask = onRemoveTask,
                    onClose = { isMenuExpanded.value = false }
                )
            }
        }
    }
}

@Composable
private fun TaskSelector(
    modifier: Modifier = Modifier,
    currentTask: Task?,
    isEnabled: Boolean,
    onClick: () -> Unit,
    isMenuExpanded: Boolean,
) {
    Card(
        modifier = modifier,
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp,
            focusedElevation = 0.dp,
            hoveredElevation = 0.dp,
            pressedElevation = 0.dp
        ),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_task_outlined),
                contentDescription = null,
                tint = if (isEnabled) MaterialTheme.colorScheme.onSurface
                    else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = stringResource(Res.string.task),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (currentTask == null) {
                    Text(
                        text = stringResource(Res.string.select_task),
                        color = if (isEnabled) MaterialTheme.colorScheme.onSurfaceVariant
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    TaskName(name = currentTask.name)
                }
            }
            Icon(
                imageVector = if (isMenuExpanded) {
                    vectorResource(Res.drawable.ic_arrow_drop_up)
                } else {
                    vectorResource(Res.drawable.ic_arrow_drop_down)
                },
                contentDescription = null,
                tint = if (isEnabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TaskMenuContent(
    tasksState: TasksListState,
    currentTask: Task?,
    onSelectTask: (Task) -> Unit,
    onClearTask: () -> Unit,
    onClose: () -> Unit,
) {
    when (tasksState) {
        TasksListState.Loading -> {}
        is TasksListState.Empty -> {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(Res.string.no_tasks),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                onClick = {},
                enabled = false,
            )
        }
        is TasksListState.Loaded -> {
            if (currentTask != null) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(Res.string.without_task),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = {
                        onClearTask()
                        onClose()
                    },
                )
            }
            tasksState.tasks
                .filter { task -> task != currentTask }
                .forEach { task ->
                    DropdownMenuItem(
                        text = {
                            TaskName(name = task.name)
                        },
                        onClick = {
                            onSelectTask(task)
                            onClose()
                        }
                    )
                }
        }
    }
}

@Composable
private fun TaskName(
    modifier: Modifier = Modifier,
    name: String,
) {
    Text(
        modifier = modifier,
        text = name,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun ActionSection(
    modifier: Modifier = Modifier,
    title: String,
    onClick: () -> Unit,
    onBack: () -> Unit,
) {
    TextButton(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            onClick()
            onBack()
        },
    ) {
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.Bold,
        )
    }
}
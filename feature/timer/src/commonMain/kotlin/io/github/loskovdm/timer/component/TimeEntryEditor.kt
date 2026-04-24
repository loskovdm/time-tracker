package io.github.loskovdm.timer.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDisplayMode
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.designsystem.util.formatDateToString
import io.github.loskovdm.designsystem.util.formatLocalTimeToHmString
import io.github.loskovdm.designsystem.util.formatTimeToHmsString
import io.github.loskovdm.timer.TimeEntryEditorUiState
import io.github.loskovdm.timer.model.Project
import io.github.loskovdm.timer.model.Task
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.cancel
import timetracker.designsystem.generated.resources.close
import timetracker.designsystem.generated.resources.delete
import timetracker.designsystem.generated.resources.ic_calendar_outlined
import timetracker.designsystem.generated.resources.ic_close
import timetracker.designsystem.generated.resources.ic_projects_outlined
import timetracker.designsystem.generated.resources.ic_task_outlined
import timetracker.designsystem.generated.resources.ok
import timetracker.designsystem.generated.resources.save
import timetracker.designsystem.generated.resources.select_time
import timetracker.designsystem.generated.resources.start
import timetracker.designsystem.generated.resources.stop
import kotlin.time.Duration
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun TimeEntryEditor(
    modifier: Modifier = Modifier,
    deviceConfiguration: DeviceConfiguration,
    timeEntryEditorUiState: TimeEntryEditorUiState,
    timerValue: Duration,
    projects: List<Project>,
    tasksForSelectedProject: List<Task>,
    onDismissRequest: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onStartTimeChange: (LocalTime) -> Unit,
    onEndDateChange: (LocalDate?) -> Unit,
    onEndTimeChange: (LocalTime?) -> Unit,
    onProjectChange: (Project?) -> Unit,
    onTaskChange: (Task?) -> Unit,
) {
    if (deviceConfiguration == DeviceConfiguration.DESKTOP) {
        TimeEntryEditorDialog(
            modifier = modifier,
            editorState = timeEntryEditorUiState,
            timerValue = timerValue,
            onDismissRequest = onDismissRequest,
            onSaveClick = onSaveClick,
            onDeleteClick = onDeleteClick,
            onStartDateChange = onStartDateChange,
            onStartTimeChange = onStartTimeChange,
            onEndDateChange = onEndDateChange,
            onEndTimeChange = onEndTimeChange,
            onProjectChange = onProjectChange,
            onTaskChange = onTaskChange,
        )
    } else {
        TimeEntryEditorBottomSheet(
            modifier = modifier,
            timerValue = timerValue,
            editorState = timeEntryEditorUiState,
            onDismissRequest = onDismissRequest,
            onSaveClick = onSaveClick,
            onDeleteClick = onDeleteClick,
            onStartDateChange = onStartDateChange,
            onStartTimeChange = onStartTimeChange,
            onEndDateChange = onEndDateChange,
            onEndTimeChange = onEndTimeChange,
            onProjectChange = onProjectChange,
            onTaskChange = onTaskChange,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeEntryEditorBottomSheet(
    modifier: Modifier = Modifier,
    timerValue: Duration,
    editorState: TimeEntryEditorUiState,
    onDismissRequest: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onStartTimeChange: (LocalTime) -> Unit,
    onEndDateChange: (LocalDate?) -> Unit,
    onEndTimeChange: (LocalTime?) -> Unit,
    onProjectChange: (Project?) -> Unit,
    onTaskChange: (Task?) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            onDismissRequest()
        },
        sheetState = sheetState,
        shape = RoundedCornerShape(
            topStart = 16.dp,
            topEnd = 16.dp,
            bottomStart = 0.dp,
            bottomEnd = 0.dp
        ),
        dragHandle = {
            TimerDragHandle(
                onClose = onDismissRequest,
                onSave = onSaveClick,
            )
        },
    ) {
        TimeEntryEditorContent(
            timerValue = timerValue,
            editorState = editorState,
            onStartDateChange = onStartDateChange,
            onStartTimeChange = onStartTimeChange,
            onEndDateChange = onEndDateChange,
            onEndTimeChange = onEndTimeChange,
            onProjectChange = onProjectChange,
            onTaskChange = onTaskChange,
            onDeleteClick = onDeleteClick,
        )
    }
}

@Composable
fun TimeEntryEditorDialog(
    modifier: Modifier = Modifier,
    timerValue: Duration,
    editorState: TimeEntryEditorUiState,
    onDismissRequest: () -> Unit,
    onSaveClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onStartDateChange: (LocalDate) -> Unit,
    onStartTimeChange: (LocalTime) -> Unit,
    onEndDateChange: (LocalDate?) -> Unit,
    onEndTimeChange: (LocalTime?) -> Unit,
    onProjectChange: (Project?) -> Unit,
    onTaskChange: (Task?) -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
        ) {
            Column {
                TimerDragHandle(
                    onClose = onDismissRequest,
                    onSave = onSaveClick,
                )
                TimeEntryEditorContent(
                    timerValue = timerValue,
                    editorState = editorState,
                    onStartDateChange = onStartDateChange,
                    onStartTimeChange = onStartTimeChange,
                    onEndDateChange = onEndDateChange,
                    onEndTimeChange = onEndTimeChange,
                    onProjectChange = onProjectChange,
                    onTaskChange = onTaskChange,
                    onDeleteClick = onDeleteClick,
                )
            }
        }
    }

}

@OptIn(ExperimentalUuidApi::class)
@Composable
fun TimeEntryEditorContent(
    modifier: Modifier = Modifier,
    timerValue: Duration,
    editorState: TimeEntryEditorUiState,
    onStartDateChange: (LocalDate) -> Unit,
    onStartTimeChange: (LocalTime) -> Unit,
    onEndDateChange: (LocalDate?) -> Unit,
    onEndTimeChange: (LocalTime?) -> Unit,
    onProjectChange: (Project?) -> Unit,
    onTaskChange: (Task?) -> Unit,
    onDeleteClick: () -> Unit,
) {
    val timePickerTarget = remember { mutableStateOf(TimePickerTarget.START) }
    val isTimePickerVisible = remember { mutableStateOf(false) }

    if (isTimePickerVisible.value) {
        val initialTime = when (timePickerTarget.value) {
            TimePickerTarget.START -> editorState.startTime
            TimePickerTarget.END -> editorState.endTime ?: editorState.startTime
        }

        TimeEntryTimePickerDialog(
            initialTime = initialTime,
            onDismissRequest = { dismissTimePicker(isTimePickerVisible) },
            onConfirm = { selectedTime ->
                when (timePickerTarget.value) {
                    TimePickerTarget.START -> onStartTimeChange(selectedTime)
                    TimePickerTarget.END -> onEndTimeChange(selectedTime)
                }
                dismissTimePicker(isTimePickerVisible)
            },
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp, start = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = formatTimeToHmsString(timerValue.inWholeSeconds),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_calendar_outlined),
                    contentDescription = null
                )
                Column() {
                    val labelWidth = 50.dp
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.width(labelWidth),
                            text = stringResource(Res.string.start),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        EditorDateTimeButtonText(
                            onClick = {
                                openTimePicker(
                                    targetState = timePickerTarget,
                                    visibleState = isTimePickerVisible,
                                    target = TimePickerTarget.START,
                                )
                            },
                            text = formatLocalTimeToHmString(editorState.startTime),
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        EditorDateTimeButtonText(
                            onClick = {
                                // TODO: date picker
                            },
                            text = formatDateToString(editorState.startDate)
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier.width(labelWidth),
                            text = stringResource(Res.string.stop),
                            style = MaterialTheme.typography.bodyLarge
                        )

                        EditorDateTimeButtonText(
                            onClick = {
                                openTimePicker(
                                    targetState = timePickerTarget,
                                    visibleState = isTimePickerVisible,
                                    target = TimePickerTarget.END,
                                )
                            },
                            text = if (editorState.endTime != null) {
                                formatLocalTimeToHmString(editorState.endTime)
                            } else {
                                ""
                            },
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        EditorDateTimeButtonText(
                            onClick = {
                                // TODO: date picker
                            },
                            text = if (editorState.endDate != null) {
                                formatDateToString(editorState.endDate)
                            } else {
                                ""
                            },
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_projects_outlined),
                    contentDescription = null
                )
                // TODO: project selector
            }

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_task_outlined),
                    contentDescription = null
                )
                // TODO: task selector
            }

            if (editorState.timeEntryId != null) {
                HorizontalDivider()

                TextButton(
                    onClick = {
                        onDeleteClick()
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.delete).uppercase(),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimerDragHandle(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    onSave: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(
            onClick = onClose,
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_close),
                contentDescription = stringResource(Res.string.close)
            )
        }

        BottomSheetDefaults.DragHandle()

        TextButton(
            onClick = onSave,
        ) {
            Text(
                text = stringResource(Res.string.save).uppercase(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun EditorDateTimeButtonText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
) {
    TextButton(
        modifier = modifier,
        onClick = onClick,
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = LocalContentColor.current,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = LocalContentColor.current
        ),
        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimeEntryTimePickerDialog(
    initialTime: LocalTime,
    onDismissRequest: () -> Unit,
    onConfirm: (LocalTime) -> Unit,
) {
    val timePickerState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true,
    )

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .widthIn(max = 600.dp)
                .fillMaxWidth(0.85f)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                TimePicker(state = timePickerState)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text(stringResource(Res.string.cancel))
                    }

                    TextButton(
                        onClick = {
                            onConfirm(
                                LocalTime(
                                    hour = timePickerState.hour,
                                    minute = timePickerState.minute,
                                    second = 0,
                                    nanosecond = 0,
                                )
                            )
                        }
                    ) {
                        Text(stringResource(Res.string.ok))
                    }
                }
            }
        }
    }
}
private enum class TimePickerTarget {
    START,
    END,
}

private fun openTimePicker(
    targetState: MutableState<TimePickerTarget>,
    visibleState: MutableState<Boolean>,
    target: TimePickerTarget,
) {
    targetState.value = target
    visibleState.value = true
}

private fun dismissTimePicker(visibleState: MutableState<Boolean>) {
    visibleState.value = false
}

@OptIn(ExperimentalUuidApi::class)
@Preview
@Composable
fun TimeEntryEditorBottomSheetPreview() {
    val timerValue = Instant.parse("2026-04-15T10:29:00Z") - Instant.parse("2026-04-15T10:00:00Z")
    MaterialTheme {
        TimeEntryEditorContent(
            timerValue = timerValue,
            editorState = TimeEntryEditorUiState(
            ),
            onStartDateChange = {},
            onStartTimeChange = {},
            onEndDateChange = {},
            onEndTimeChange = {},
            onProjectChange = {},
            onTaskChange = {},
            onDeleteClick = {},
        )
    }
}


//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun TimeEntryEditorBottomSheet(
//    modifier: Modifier = Modifier,
//    uiState: TimeEntryEditorUiState,
//    projects: List<Project>,
//    tasksForSelectedProject: List<Task>,
//    onDismissRequest: () -> Unit,
//    onSaveClick: () -> Unit,
//    onDeleteClick: () -> Unit,
//    onStartDateChange: (LocalDate) -> Unit,
//    onStartTimeChange: (LocalTime) -> Unit,
//    onEndDateChange: (LocalDate?) -> Unit,
//    onEndTimeChange: (LocalTime?) -> Unit,
//    onProjectChange: (Project?) -> Unit,
//    onTaskChange: (Task?) -> Unit,
//) {
//    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
//
//    ModalBottomSheet(
//        modifier = modifier,
//        onDismissRequest = onDismissRequest,
//        sheetState = sheetState,
//    ) {
//        Column(
//            modifier = Modifier
//                .fillMaxHeight(0.95f)
//                .fillMaxWidth()
//                .navigationBarsPadding()
//                .imePadding()
//                .padding(horizontal = 16.dp, vertical = 8.dp),
//        ) {
//            EditorHeader(onDismissRequest = onDismissRequest, onSaveClick = onSaveClick)
//            HorizontalDivider(modifier = Modifier.padding(top = 4.dp, bottom = 12.dp))
//
//            TimeEntryEditorContent(
//                modifier = Modifier.weight(1f).verticalScroll(rememberScrollState()),
//                uiState = uiState,
//                projects = projects,
//                tasksForSelectedProject = tasksForSelectedProject,
//                onDeleteClick = onDeleteClick,
//                onStartDateChange = onStartDateChange,
//                onStartTimeChange = onStartTimeChange,
//                onEndDateChange = onEndDateChange,
//                onEndTimeChange = onEndTimeChange,
//                onProjectChange = onProjectChange,
//                onTaskChange = onTaskChange,
//            )
//        }
//    }
//}
//
//@Composable
//private fun TimeEntryEditorDialog(
//    modifier: Modifier = Modifier,
//    uiState: TimeEntryEditorUiState,
//    projects: List<Project>,
//    tasksForSelectedProject: List<Task>,
//    onDismissRequest: () -> Unit,
//    onSaveClick: () -> Unit,
//    onDeleteClick: () -> Unit,
//    onStartDateChange: (LocalDate) -> Unit,
//    onStartTimeChange: (LocalTime) -> Unit,
//    onEndDateChange: (LocalDate?) -> Unit,
//    onEndTimeChange: (LocalTime?) -> Unit,
//    onProjectChange: (Project?) -> Unit,
//    onTaskChange: (Task?) -> Unit,
//) {
//    Dialog(onDismissRequest = onDismissRequest) {
//        Surface(
//            modifier = modifier.widthIn(max = 560.dp),
//        ) {
//            Column(modifier = Modifier.padding(16.dp)) {
//                EditorHeader(onDismissRequest = onDismissRequest, onSaveClick = onSaveClick)
//                HorizontalDivider(modifier = Modifier.padding(top = 4.dp, bottom = 12.dp))
//                TimeEntryEditorContent(
//                    modifier = Modifier.verticalScroll(rememberScrollState()),
//                    uiState = uiState,
//                    projects = projects,
//                    tasksForSelectedProject = tasksForSelectedProject,
//                    onDeleteClick = onDeleteClick,
//                    onStartDateChange = onStartDateChange,
//                    onStartTimeChange = onStartTimeChange,
//                    onEndDateChange = onEndDateChange,
//                    onEndTimeChange = onEndTimeChange,
//                    onProjectChange = onProjectChange,
//                    onTaskChange = onTaskChange,
//                )
//            }
//        }
//    }
//}
//
//@Composable
//private fun EditorHeader(
//    onDismissRequest: () -> Unit,
//    onSaveClick: () -> Unit,
//) {
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//    ) {
//        IconButton(onClick = onDismissRequest) {
//            Icon(
//                imageVector = vectorResource(Res.drawable.ic_close),
//                contentDescription = stringResource(Res.string.close),
//            )
//        }
//        TextButton(onClick = onSaveClick, modifier = Modifier.align(Alignment.CenterVertically)) {
//            Text(stringResource(Res.string.save))
//        }
//    }
//}
//
//@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
//@Composable
//private fun TimeEntryEditorContent(
//    modifier: Modifier = Modifier,
//    uiState: TimeEntryEditorUiState,
//    projects: List<Project>,
//    tasksForSelectedProject: List<Task>,
//    onDeleteClick: () -> Unit,
//    onStartDateChange: (LocalDate) -> Unit,
//    onStartTimeChange: (LocalTime) -> Unit,
//    onEndDateChange: (LocalDate?) -> Unit,
//    onEndTimeChange: (LocalTime?) -> Unit,
//    onProjectChange: (Project?) -> Unit,
//    onTaskChange: (Task?) -> Unit,
//) {
//    var projectExpanded by remember { mutableStateOf(false) }
//    var taskExpanded by remember { mutableStateOf(false) }
//
//    val isTaskEnabled = uiState.project != null
//
//    Column(
//        modifier = modifier,
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//    ) {
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            OutlinedTextField(
//                modifier = Modifier.weight(1f),
//                value = uiState.startDate.toString(),
//                onValueChange = { value -> parseLocalDateOrNull(value)?.let(onStartDateChange) },
//                label = { Text("Дата старта") },
//                singleLine = true,
//            )
//            OutlinedTextField(
//                modifier = Modifier.weight(1f),
//                value = uiState.startTime.toString(),
//                onValueChange = { value -> parseLocalTimeOrNull(value)?.let(onStartTimeChange) },
//                label = { Text("Время старта") },
//                singleLine = true,
//            )
//        }
//
//        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
//            OutlinedTextField(
//                modifier = Modifier.weight(1f),
//                value = uiState.endDate?.toString().orEmpty(),
//                onValueChange = { value -> onEndDateChange(parseLocalDateOrNull(value)) },
//                label = { Text("Дата завершения") },
//                placeholder = { Text("Не выбрано") },
//                singleLine = true,
//            )
//            OutlinedTextField(
//                modifier = Modifier.weight(1f),
//                value = uiState.endTime?.toString().orEmpty(),
//                onValueChange = { value -> onEndTimeChange(parseLocalTimeOrNull(value)) },
//                label = { Text("Время завершения") },
//                placeholder = { Text("Не выбрано") },
//                singleLine = true,
//            )
//        }
//
//        ExposedDropdownMenuBox(
//            expanded = projectExpanded,
//            onExpandedChange = { projectExpanded = !projectExpanded },
//        ) {
//            OutlinedTextField(
//                modifier = Modifier
//                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = true)
//                    .fillMaxWidth(),
//                readOnly = true,
//                value = uiState.project?.name.orEmpty(),
//                onValueChange = {},
//                label = { Text("Проект") },
//                placeholder = { Text("Выберите проект") },
//                trailingIcon = {
//                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = projectExpanded)
//                },
//            )
//
//            DropdownMenu(
//                expanded = projectExpanded,
//                onDismissRequest = { projectExpanded = false },
//            ) {
//                projects.forEach { project ->
//                    DropdownMenuItem(
//                        text = { Text(project.name) },
//                        onClick = {
//                            onProjectChange(project)
//                            onTaskChange(null)
//                            projectExpanded = false
//                        },
//                    )
//                }
//            }
//        }
//
//        ExposedDropdownMenuBox(
//            expanded = taskExpanded,
//            onExpandedChange = {
//                if (isTaskEnabled) taskExpanded = !taskExpanded
//            },
//        ) {
//            OutlinedTextField(
//                modifier = Modifier
//                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable, enabled = isTaskEnabled)
//                    .fillMaxWidth(),
//                enabled = isTaskEnabled,
//                readOnly = true,
//                value = uiState.task?.name.orEmpty(),
//                onValueChange = {},
//                label = { Text("Задача") },
//                placeholder = {
//                    Text(if (isTaskEnabled) "Выберите задачу" else "Сначала выберите проект")
//                },
//                trailingIcon = {
//                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = taskExpanded && isTaskEnabled)
//                },
//            )
//
//            DropdownMenu(
//                expanded = taskExpanded && isTaskEnabled,
//                onDismissRequest = { taskExpanded = false },
//            ) {
//                tasksForSelectedProject.forEach { task ->
//                    DropdownMenuItem(
//                        text = { Text(task.name) },
//                        onClick = {
//                            onTaskChange(task)
//                            taskExpanded = false
//                        },
//                    )
//                }
//            }
//        }
//
//        if (uiState.timeEntryId != null) {
//            OutlinedButton(
//                modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 4.dp),
//                onClick = onDeleteClick,
//            ) {
//                Text("Удалить")
//            }
//        }
//    }
//}
//
//private fun parseLocalDateOrNull(value: String): LocalDate? {
//    if (value.isBlank()) return null
//    return runCatching { LocalDate.parse(value) }.getOrNull()
//}
//
//private fun parseLocalTimeOrNull(value: String): LocalTime? {
//    if (value.isBlank()) return null
//    return runCatching { LocalTime.parse(value) }.getOrNull()
//}

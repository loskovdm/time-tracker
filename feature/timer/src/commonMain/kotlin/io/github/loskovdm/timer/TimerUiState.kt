package io.github.loskovdm.timer

//import io.github.loskovdm.timer.model.TimeEntry

import io.github.loskovdm.timer.model.Project
import io.github.loskovdm.timer.model.Task
import io.github.loskovdm.timer.model.TimeEntry
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class TimerUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val activeTimeEntry: ActiveTimeEntryUiState? = null,
    val completedTimeEntries: List<TimeEntry> = emptyList(),
    val timeEntryEditor: TimeEntryEditorUiState? = null,
)

data class ActiveTimeEntryUiState(
    val entry: TimeEntry,
    val duration: Duration,
)

data class TimeEntryEditorUiState @OptIn(ExperimentalUuidApi::class) constructor(
    val timeEntryId: Uuid? = null,
    val startTime: LocalTime = nowLocalDateTime().time,
    val startDate: LocalDate = nowLocalDateTime().date,
    val endTime: LocalTime? = nowLocalDateTime().time,
    val endDate: LocalDate? = nowLocalDateTime().date,
    val project: Project? = null,
    val task: Task? = null,
    val projects: List<Project> = emptyList(),
    val tasks: List<Task> = emptyList(),
)

private fun nowLocalDateTime() =
    Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())




//data class TimerUiState(
//    val isLoading: Boolean = false,
//    val errorMessage: String? = null,
//    val completedTimeEntries: List<TimeEntry> = emptyList(),
//)
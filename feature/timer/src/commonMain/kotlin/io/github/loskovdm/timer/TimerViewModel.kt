package io.github.loskovdm.timer

//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import io.github.loskovdm.domain.usecase.project.ObserveProjectsUseCase
//import io.github.loskovdm.domain.usecase.task.ObserveTasksUseCase
//import io.github.loskovdm.domain.usecase.timetracker.ObserveCompletedTimeEntriesUseCase
//import io.github.loskovdm.timer.model.toView
//import kotlinx.coroutines.flow.SharingStarted
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.flow.stateIn
//import kotlin.uuid.ExperimentalUuidApi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.error.DomainError
import io.github.loskovdm.domain.error.Result
import io.github.loskovdm.domain.usecase.project.ObserveProjectsUseCase
import io.github.loskovdm.domain.usecase.task.ObserveTasksUseCase
import io.github.loskovdm.domain.usecase.timetracker.AddTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timetracker.DeleteTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timetracker.ObserveActiveTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timetracker.ObserveCompletedTimeEntriesUseCase
import io.github.loskovdm.domain.usecase.timetracker.StartTrackTimeUseCase
import io.github.loskovdm.domain.usecase.timetracker.UpdateTimeEntryUseCase
import io.github.loskovdm.timer.model.Project
import io.github.loskovdm.timer.model.Task
import io.github.loskovdm.timer.model.TimeEntry
import io.github.loskovdm.timer.model.toView
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class TimerViewModel(
    observeProjectsUseCase: ObserveProjectsUseCase,
    observeTasksUseCase: ObserveTasksUseCase,
    observeCompletedTimeEntriesUseCase: ObserveCompletedTimeEntriesUseCase,
    observeActiveTimeEntryUseCase: ObserveActiveTimeEntryUseCase,
    private val startTrackTimeUseCase: StartTrackTimeUseCase,
    private val addTimeEntryUseCase: AddTimeEntryUseCase,
    private val updateTimeEntryUseCase: UpdateTimeEntryUseCase,
    private val deleteTimeEntryUseCase: DeleteTimeEntryUseCase,
) : ViewModel() {
    private data class DataBundle(
        val projects: List<Project>,
        val tasks: List<Task>,
        val completedTimeEntries: List<TimeEntry>,
        val activeTimeEntry: TimeEntry?
    )
    private val projectsFlow = observeProjectsUseCase()
    private val tasksFlow = observeTasksUseCase()
    private val completedTimeEntriesFlow = observeCompletedTimeEntriesUseCase()
    private val activeTimeEntryFlow = observeActiveTimeEntryUseCase()
    private val timeEntryEditorUiState = MutableStateFlow<TimeEntryEditorUiState?>(null)
    private val errorMessageFlow = MutableStateFlow<String?>(null)
    private val timerActionMutex = Mutex()
    private val timerTickerFlow: Flow<Unit> = flow {
        while (true) {
            emit(Unit)
            delay(1_000)
        }
    }

    private val activeTimeEntryWithTickerFlow = combine(
        activeTimeEntryFlow,
        timerTickerFlow,
    ) { activeTimeEntry, _ -> activeTimeEntry }

    val uiState: StateFlow<TimerUiState> = combine(
        projectsFlow,
        tasksFlow,
        completedTimeEntriesFlow,
        activeTimeEntryWithTickerFlow,
        timeEntryEditorUiState,
    ) { projects, tasks, completedTimeEntries, activeTimeEntry, timeEntryEditor ->
        val projectsUi = projects.associate { it.id to it.toView() }
        val tasksUi = tasks.associate { it.id to it.toView(projectsUi[it.projectId]!!) }
        val projectsListUi = projectsUi.values.toList()
        val tasksListUi = tasksUi.values.toList()
        val completedTimeEntriesUi = completedTimeEntries.map { entry ->
            val projectView = entry.projectId?.let { projectsUi[it] }
            val taskView = entry.taskId?.let { tasksUi[it] }
            entry.toView(projectView, taskView)
        }
        val activeEntryUi = activeTimeEntry?.let { entry ->
            val projectView = entry.projectId?.let { projectsUi[it] }
            val taskView = entry.taskId?.let { tasksUi[it] }
            entry.toView(projectView, taskView)
        }
        val editorUiState = timeEntryEditor?.copy(
            projects = projectsListUi,
            tasks = tasksListUi,
        )
        TimerUiState(
            isLoading = false,
            activeTimeEntry = activeEntryUi?.let {
                ActiveTimeEntryUiState(
                    entry = it,
                    duration = Clock.System.now() - it.startDateTime,
                )
            },
            completedTimeEntries = completedTimeEntriesUi,
            timeEntryEditor = editorUiState,
        )
    }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = TimerUiState(isLoading = true)
            )

    private val zone = TimeZone.currentSystemDefault()

    fun openNewTimeEntryEditor() {
        timeEntryEditorUiState.value = TimeEntryEditorUiState()
    }

    fun closeTimeEntryEditor() {
        timeEntryEditorUiState.value = null
    }

    fun saveNewTimeEntry(
        startTime: LocalTime,
        startDate: LocalDate,
        endTime: LocalTime,
        endDate: LocalDate,
        project: Project?,
        task: Task?,
    ) {
        viewModelScope.launch {
            val startDateTime = LocalDateTime(startDate, startTime).toInstant(zone)
            val endDateTime = LocalDateTime(endDate, endTime).toInstant(zone)
            addTimeEntryUseCase(
                startDateTime = startDateTime,
                endDateTime = endDateTime,
                projectId = project?.id,
                taskId = task?.id,
            )
        }
    }

    fun startNewTimerAndOpenEditor() {
        viewModelScope.launch {
            timerActionMutex.withLock {
                stopActiveTimerInternal()

                startTrackTimeUseCase(
                    startDateTime = Clock.System.now(),
                    projectId = null,
                    taskId = null,
                )
                    .also { result ->
                        if (result is Result.Failure) {
                            errorMessageFlow.value = result.error.toUserMessage()
                            return@withLock
                        }
                        errorMessageFlow.value = null
                    }

                val activeEntry = activeTimeEntryFlow.first { it != null } ?: return@withLock
                val projectsUi = projectsFlow.first().associate { it.id to it.toView() }
                val tasksUi = tasksFlow.first().associate { it.id to it.toView(projectsUi[it.projectId]!!) }

                timeEntryEditorUiState.value = TimeEntryEditorUiState(
                    timeEntryId = activeEntry.id,
                    startTime = activeEntry.startDateTime.toLocalDateTime(zone).time,
                    startDate = activeEntry.startDateTime.toLocalDateTime(zone).date,
                    endTime = null,
                    endDate = null,
                    project = activeEntry.projectId?.let { projectsUi[it] },
                    task = activeEntry.taskId?.let { tasksUi[it] },
                )
            }
        }
    }

    fun startTimerFromEntry(timeEntry: TimeEntry) {
        viewModelScope.launch {
            timerActionMutex.withLock {
                stopActiveTimerInternal()
                val result = startTrackTimeUseCase(
                    startDateTime = Clock.System.now(),
                    projectId = timeEntry.project?.id,
                    taskId = timeEntry.task?.id,
                )

                if (result is Result.Failure) {
                    errorMessageFlow.value = result.error.toUserMessage()
                    return@withLock
                }

                errorMessageFlow.value = null
            }
        }
    }

    fun dismissError() {
        errorMessageFlow.value = null
    }

    fun stopActiveTimer() {
        viewModelScope.launch {
            timerActionMutex.withLock {
                stopActiveTimerInternal()
            }
        }
    }

    private suspend fun stopActiveTimerInternal() {
        val activeEntry = activeTimeEntryFlow.first() ?: return
        updateTimeEntryUseCase(
            id = activeEntry.id,
            startDateTime = activeEntry.startDateTime,
            endDateTime = Clock.System.now(),
            projectId = activeEntry.projectId,
            taskId = activeEntry.taskId,
        )
        closeTimeEntryEditor()
    }

    fun openEditorFromEntry(timeEntry: TimeEntry) {
        val zone = TimeZone.currentSystemDefault()
        timeEntryEditorUiState.value = TimeEntryEditorUiState(
            timeEntryId = timeEntry.id,
            startTime = timeEntry.startDateTime.toLocalDateTime(zone).time,
            startDate = timeEntry.startDateTime.toLocalDateTime(zone).date,
            endTime = timeEntry.endDateTime?.toLocalDateTime(zone)?.time,
            endDate = timeEntry.endDateTime?.toLocalDateTime(zone)?.date,
            project = timeEntry.project,
            task = timeEntry.task,
        )
    }

    fun saveEditedTimeEntry(
        id: Uuid,
        startTime: LocalTime,
        startDate: LocalDate,
        endTime: LocalTime?,
        endDate: LocalDate?,
        project: Project?,
        task: Task?,
    ) {
        viewModelScope.launch {
            val startDateTime = LocalDateTime(startDate, startTime).toInstant(zone)
            val endDateTime = if (endTime != null && endDate != null) {
                LocalDateTime(endDate, endTime).toInstant(zone)
            } else {
                null
            }
            updateTimeEntryUseCase(
                id = id,
                startDateTime = startDateTime,
                endDateTime = endDateTime,
                projectId = project?.id,
                taskId = task?.id
            )
        }
    }

    fun deleteTimeEntry(
        id: Uuid,
        startTime: LocalTime,
        startDate: LocalDate,
        endTime: LocalTime?,
        endDate: LocalDate?,
        project: Project?,
        task: Task?,
    ) {
        viewModelScope.launch {
            val startDateTime = LocalDateTime(startDate, startTime).toInstant(zone)
            var endDateTime = if (endTime != null && endDate != null) {
                LocalDateTime(endDate, endTime).toInstant(zone)
            } else {
                null
            }
            if (endDateTime == null) {
                endDateTime = Clock.System.now()
            }
            deleteTimeEntryUseCase(
                id = id,
                startDateTime = startDateTime,
                endDateTime = endDateTime,
                projectId = project?.id,
                taskId = task?.id,
            )
        }
    }

    fun updateStartTime(startTime: LocalTime) {
        timeEntryEditorUiState.update {
            it?.copy(startTime = startTime)
        }
    }

    fun updateStartDate(startDate: LocalDate) {
        timeEntryEditorUiState.update {
            it?.copy(startDate = startDate)
        }
    }

    fun updateFinishTime(endTime: LocalTime) {
        timeEntryEditorUiState.update {
            it?.copy(endTime = endTime)
        }
    }

    fun updateFinishDate(endDate: LocalDate) {
        timeEntryEditorUiState.update {
            it?.copy(endDate = endDate)
        }
    }

    fun updateProject(project: Project?) {
        timeEntryEditorUiState.update {
            it?.copy(
                project = project,
                task = if (project == null || it.task?.project?.id != project.id) null else it.task,
            )
        }
    }

    fun updateTask(task: Task?) {
        timeEntryEditorUiState.update {
            it?.copy(task = task)
        }
    }

    private fun DomainError.toUserMessage(): String {
        return when (this) {
            DomainError.SecondActiveTimeEntry -> "An active timer already exists."
        }
    }
}




//@OptIn(ExperimentalUuidApi::class)
//class TimerViewModel(
//    observeProjectsUseCase: ObserveProjectsUseCase,
//    observeTasksUseCase: ObserveTasksUseCase,
//    observeCompletedTimeEntriesUseCase: ObserveCompletedTimeEntriesUseCase,
//): ViewModel() {
//    private val projectsFlow = observeProjectsUseCase()
//    private val tasksFlow = observeTasksUseCase()
//    private val completedTimeEntriesFlow = observeCompletedTimeEntriesUseCase()
//    val uiState: StateFlow<TimerUiState> = combine(
//        projectsFlow,
//        tasksFlow,
//        completedTimeEntriesFlow
//    ) { projects, tasks, completedTimeEntries ->
//        val projectsUi = projects.associate { projectDomain ->
//            projectDomain.id to projectDomain.toView()
//        }
//        val tasksUi = tasks.associate { taskDomain ->
//            taskDomain.id to taskDomain.toView(projectsUi[taskDomain.id]!!)
//        }
//        val completedTimeEntriesUi = completedTimeEntries.map { completedTimeEntryDomain ->
//            completedTimeEntryDomain.toView(
//                project = projectsUi[completedTimeEntryDomain.id],
//                task = tasksUi[completedTimeEntryDomain.id]
//            )
//        }
//
//        TimerUiState(
//            completedTimeEntries = completedTimeEntriesUi,
//        )
//    }
//        .stateIn(
//            scope = viewModelScope,
//            started = SharingStarted.WhileSubscribed(5_000),
//            initialValue = TimerUiState(isLoading = false)
//        )
//}
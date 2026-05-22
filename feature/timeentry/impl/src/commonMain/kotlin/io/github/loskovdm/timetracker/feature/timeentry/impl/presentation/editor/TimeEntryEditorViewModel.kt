package io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.editor

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.designsystem.util.withoutSeconds
import io.github.loskovdm.domain.usecase.timeentry.AddTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timeentry.DeleteTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timeentry.GetTimeEntryByIdUseCase
import io.github.loskovdm.domain.usecase.timeentry.UpdateTimeEntryUseCase
import io.github.loskovdm.timetracker.feature.projects.api.model.Project
import io.github.loskovdm.timetracker.feature.tasks.api.model.Task
import io.github.loskovdm.timetracker.feature.timeentry.impl.mapper.ProjectMapper
import io.github.loskovdm.timetracker.feature.timeentry.impl.mapper.TaskMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class TimeEntryEditorViewModel(
    private val timeEntryId: Uuid?,
    private val getTimeEntryByIdUseCase: GetTimeEntryByIdUseCase,
    private val addTimeEntryUseCase: AddTimeEntryUseCase,
    private val updateTimeEntryUseCase: UpdateTimeEntryUseCase,
    private val deleteTimeEntryUseCase: DeleteTimeEntryUseCase,
    private val projectMapper: ProjectMapper,
    private val taskMapper: TaskMapper,
) : ViewModel() {
    private val _editableState = MutableStateFlow(TimeEntryEditorState())

    private val editableState = _editableState
        .onStart { loadEditableData() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimeEntryEditorState(),
        )

    private val ticker = flow {
        while (true) {
            emit(Unit)
            delay(1_000)
        }
    }
    val state: StateFlow<TimeEntryEditorState> = combine(
        editableState,
        ticker,
    ) { editableData, _ ->
        TimeEntryEditorState(
            startDateTime = editableData.startDateTime,
            endDateTime = editableData.endDateTime,
            project = editableData.project,
            task = editableData.task,
            isNewEntry = editableData.isNewEntry,
            isDateTimeValid = editableData.isDateTimeValid,
            duration = if (editableData.endDateTime != null) {
                editableData.endDateTime - editableData.startDateTime
            } else {
                Clock.System.now() - editableData.startDateTime
            }
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimeEntryEditorState(),
        )

    private fun loadEditableData() {
        if (timeEntryId == null) {
            _editableState.update { TimeEntryEditorState() }
        } else {
            viewModelScope.launch {
                val timeEntryWithRelations = getTimeEntryByIdUseCase(timeEntryId)
                if (timeEntryWithRelations == null) {
                    _editableState.update {
                        TimeEntryEditorState(isNewEntry = false)
                    }
                } else {
                    val startDateTime = timeEntryWithRelations.timeEntry.startDateTime
                    val endDateTime = timeEntryWithRelations.timeEntry.endDateTime

                    _editableState.update {
                        TimeEntryEditorState(
                            startDateTime = timeEntryWithRelations.timeEntry.startDateTime,
                            endDateTime = timeEntryWithRelations.timeEntry.endDateTime,
                            project = timeEntryWithRelations.project?.let {
                                projectMapper.toView(it)
                            },
                            task = timeEntryWithRelations.task?.let {
                                taskMapper.toView(it)
                            },
                            isNewEntry = false,
                            isDateTimeValid = endDateTime == null || startDateTime <= endDateTime
                        )
                    }
                }
            }
        }
    }

    fun saveTimeEntry() {
        viewModelScope.launch {
            if (timeEntryId == null) {
                val endDateTime = state.value.endDateTime ?: return@launch
                addTimeEntryUseCase(
                    startDateTime = state.value.startDateTime,
                    endDateTime = endDateTime,
                    projectId = state.value.project?.id,
                    taskId = state.value.task?.id,
                )
            } else {
                updateTimeEntryUseCase(
                    id = timeEntryId,
                    startDateTime = state.value.startDateTime,
                    endDateTime = state.value.endDateTime,
                    projectId = state.value.project?.id,
                    taskId = state.value.task?.id,
                )
            }
        }
    }

    fun deleteTimeEntry() {
        viewModelScope.launch {
            val endDateTime = state.value.endDateTime
            if (timeEntryId != null && endDateTime != null) {
                deleteTimeEntryUseCase(
                    id = timeEntryId,
                    startDateTime = state.value.startDateTime,
                    endDateTime = endDateTime,
                    projectId = state.value.project?.id,
                    taskId = state.value.task?.id
                )
            }
        }
    }

    fun changeStartDateTime(changedStartDateTime: Instant) {
        val endDateTime = _editableState.value.endDateTime

        _editableState.update {
            it.copy(
                startDateTime = changedStartDateTime,
                isDateTimeValid = if (endDateTime == null) {
                    changedStartDateTime < Clock.System.now()
                } else {
                    changedStartDateTime <= endDateTime
                }
            )
        }
    }

    fun changeEndDateTime(changedEndDateTime: Instant) {
        val startDateTime = _editableState.value.startDateTime

        _editableState.update {
            it.copy(
                endDateTime = changedEndDateTime,
                isDateTimeValid = startDateTime <= changedEndDateTime
            )
        }
    }

    fun selectProject(project: Project?) {
        _editableState.update { it.copy(project = project, task = null) }
    }

    fun selectTask(task: Task?) {
        _editableState.update { it.copy(task = task) }
    }
}
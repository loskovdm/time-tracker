package io.github.loskovdm.timetracker.feature.timeentry.impl.presentation.timer

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.viewModelScope
import io.github.loskovdm.domain.error.Result
import io.github.loskovdm.domain.error.TrackTimeError
import io.github.loskovdm.domain.usecase.timeentry.GetActiveTimeEntryUseCase
import io.github.loskovdm.domain.usecase.timeentry.StartTrackTimeUseCase
import io.github.loskovdm.domain.usecase.timeentry.StopTrackTimeUseCase
import io.github.loskovdm.timetracker.feature.timeentry.api.presentation.TimerState
import io.github.loskovdm.timetracker.feature.timeentry.api.presentation.TimerViewModel
import io.github.loskovdm.timetracker.feature.timeentry.impl.mapper.TimeEntryWithRelationsMapper
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.second_active_timer_error
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal class TimerViewModelImpl(
    private val mapper: TimeEntryWithRelationsMapper,
    getActiveTimeEntryUseCase: GetActiveTimeEntryUseCase,
    private val startTrackTimeUseCase: StartTrackTimeUseCase,
    private val stopTrackTimeUseCase: StopTrackTimeUseCase,
) : TimerViewModel() {
    private val _error = MutableStateFlow<StringResource?>(null)
    private val _ticker = flow {
        while (true) {
            emit(Unit)
            delay(1_000)
        }
    }

    override val state: StateFlow<TimerState> = combine(
        getActiveTimeEntryUseCase(),
        _error,
        _ticker,
    ) { activeTimeEntry, error, _ ->
        when {
            error != null -> {
                TimerState.Error(error)
            }
            activeTimeEntry == null -> {
                TimerState.Empty
            }
            else -> {
                TimerState.Loaded(
                    timeEntryWithRelations = mapper.toView(activeTimeEntry),
                    duration = Clock.System.now() - activeTimeEntry.timeEntry.startDateTime,
                )
            }
        }
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TimerState.Empty
        )

    override fun startTimer(
        projectId: Uuid?,
        taskId: Uuid?,
    ) {
        viewModelScope.launch {
            val result = startTrackTimeUseCase(
                projectId = projectId,
                taskId = taskId,
            )

            when (result) {
                is Result.Success -> {}
                is Result.Failure -> {
                    _error.value = getErrorMessage(result.error)
                }
            }
        }
    }

    override fun stopTimer() {
        when (val currentState = state.value) {
            TimerState.Empty -> return
            is TimerState.Error -> return
            is TimerState.Loaded -> {
                viewModelScope.launch {
                    stopTrackTimeUseCase(
                        id = currentState.timeEntryWithRelations.timeEntry.id,
                        startDateTime = currentState.timeEntryWithRelations.timeEntry.startDateTime,
                        projectId = currentState.timeEntryWithRelations.project?.id,
                        taskId = currentState.timeEntryWithRelations.task?.id
                    )
                }
            }
        }
    }

    override fun clearError() {
        _error.value = null
    }

    private fun getErrorMessage(error: TrackTimeError): StringResource =
        when (error) {
            TrackTimeError.SecondActiveTimeEntry -> Res.string.second_active_timer_error
        }
}
package io.github.loskovdm.timetracker.feature.timeentry.api.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
abstract class TimerViewModel : ViewModel() {
    abstract val state: StateFlow<TimerState>
    abstract fun startTimer(
        projectId: Uuid? = null,
        taskId: Uuid? = null,
    )
    abstract fun stopTimer()
    abstract fun clearError()
}
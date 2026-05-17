package io.github.loskovdm.timetracker.feature.tasks.api.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class TasksListViewModel : ViewModel() {
    abstract val state: StateFlow<TasksListState>
}
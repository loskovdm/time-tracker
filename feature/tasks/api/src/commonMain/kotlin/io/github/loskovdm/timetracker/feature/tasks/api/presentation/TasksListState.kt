package io.github.loskovdm.timetracker.feature.tasks.api.presentation

import io.github.loskovdm.timetracker.feature.tasks.api.model.Task

sealed interface TasksListState {
    data object Loading : TasksListState
    data class Loaded(val tasks: List<Task>) : TasksListState
    data object Empty : TasksListState
}
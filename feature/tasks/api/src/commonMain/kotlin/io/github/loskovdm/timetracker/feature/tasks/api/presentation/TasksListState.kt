package io.github.loskovdm.timetracker.feature.tasks.api.presentation

import io.github.loskovdm.timetracker.feature.tasks.api.model.Task
import kotlin.uuid.ExperimentalUuidApi

sealed interface TasksListState {
    data object Loading : TasksListState

    @OptIn(ExperimentalUuidApi::class)
    data class Loaded(val tasks: List<Task>) : TasksListState

    @OptIn(ExperimentalUuidApi::class)
    data object Empty : TasksListState
}
package io.github.loskovdm.timetracker.feature.projects.api.presentation

import io.github.loskovdm.timetracker.feature.projects.api.model.Project

sealed interface ProjectsListState {
    data object Loading : ProjectsListState
    data class Loaded(val projects: List<Project>) : ProjectsListState
    data object Empty : ProjectsListState
}
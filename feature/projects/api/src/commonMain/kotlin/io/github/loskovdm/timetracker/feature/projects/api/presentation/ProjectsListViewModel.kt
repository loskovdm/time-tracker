package io.github.loskovdm.timetracker.feature.projects.api.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class ProjectsListViewModel : ViewModel() {
    abstract val state: StateFlow<ProjectsListState>
}
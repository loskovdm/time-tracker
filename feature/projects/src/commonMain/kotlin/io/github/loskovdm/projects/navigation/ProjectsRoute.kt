package io.github.loskovdm.projects.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface ProjectsRoute: NavKey {

    @Serializable
    data object Projects: ProjectsRoute

    @Serializable
    data class Tasks(val projectName: String): ProjectsRoute

}
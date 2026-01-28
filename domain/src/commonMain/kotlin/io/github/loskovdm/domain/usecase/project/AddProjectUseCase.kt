package io.github.loskovdm.domain.usecase.project

import io.github.loskovdm.domain.model.Project
import io.github.loskovdm.domain.repository.ProjectRepository

class AddProjectUseCase(
    private val repository: ProjectRepository,
) {
    suspend operator fun invoke(
        name: String,
        color: String,
    ) {
        val project = Project(
            name = name,
            color = color,
        )
        repository.addProject(project)
    }
}
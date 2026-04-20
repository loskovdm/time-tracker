package io.github.loskovdm.domain.usecase.project

import io.github.loskovdm.domain.model.Project
import io.github.loskovdm.domain.repository.ProjectRepository
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddProjectUseCase(
    private val repository: ProjectRepository,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        name: String,
        color: Long,
    ) {
        val project = Project(
            id = Uuid.generateV7(),
            name = name,
            color = color,
            isSynced = false,
        )
        repository.addProject(
            project = project,
            addedAt = Clock.System.now(),
        )
    }
}
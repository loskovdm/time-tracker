package io.github.loskovdm.domain.usecase.project

import io.github.loskovdm.domain.model.Project
import io.github.loskovdm.domain.repository.ProjectRepository
import kotlin.time.Clock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DeleteProjectUseCase(
    private val repository: ProjectRepository,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        id: Uuid,
        name: String,
        color: Long,
    ) {
        val project = Project(
            id = id,
            name = name,
            color = color,
            isSynced = false,
        )
        repository.deleteProject(
            project = project,
            deletedAt = Clock.System.now(),
        )
    }
}
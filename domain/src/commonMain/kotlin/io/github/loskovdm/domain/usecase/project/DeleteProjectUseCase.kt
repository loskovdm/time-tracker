package io.github.loskovdm.domain.usecase.project

import io.github.loskovdm.domain.model.Project
import io.github.loskovdm.domain.repository.ProjectRepository
import io.github.loskovdm.domain.repository.TaskRepository
import io.github.loskovdm.domain.repository.TimeEntryRepository
import io.github.loskovdm.domain.util.DeleteStrategy
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val taskRepository: TaskRepository,
    private val timeEntryRepository: TimeEntryRepository,
) {
    suspend operator fun invoke(
        id: Uuid,
        name: String,
        color: Long,
        isArchived: Boolean,
        deleteStrategy: DeleteStrategy,
    ) {
        when (deleteStrategy) {
            DeleteStrategy.CASCADE -> {
                timeEntryRepository.deleteTimeEntryByProjectId(id)
            }
            DeleteStrategy.SET_NULL -> {
                timeEntryRepository.unlinkTimeEntriesFromProject(id)
            }
        }

        taskRepository.deleteTasksByProjectId(id)

        val project = Project(
            id = id,
            name = name,
            color = color,
            isArchived = isArchived,
        )
        projectRepository.deleteProject(project)
    }
}

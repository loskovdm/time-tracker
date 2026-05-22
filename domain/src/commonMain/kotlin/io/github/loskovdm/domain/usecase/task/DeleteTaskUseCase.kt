package io.github.loskovdm.domain.usecase.task

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository
import io.github.loskovdm.domain.repository.TimeEntryRepository
import io.github.loskovdm.domain.util.DeleteStrategy
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class DeleteTaskUseCase(
    private val taskRepository: TaskRepository,
    private val timeEntryRepository: TimeEntryRepository,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        id: Uuid,
        name: String,
        projectId: Uuid,
        isCompleted: Boolean,
        deleteStrategy: DeleteStrategy,
    ) {
        when (deleteStrategy) {
            DeleteStrategy.CASCADE -> {
                timeEntryRepository.deleteTimeEntryByTaskId(id)
            }
            DeleteStrategy.SET_NULL -> {
                timeEntryRepository.setTaskIdToNull(id)
            }
        }

        val deleteTask = Task(
            id = id,
            name = name,
            projectId = projectId,
            isCompleted = isCompleted,
        )
        taskRepository.deleteTask(deleteTask)
    }
}
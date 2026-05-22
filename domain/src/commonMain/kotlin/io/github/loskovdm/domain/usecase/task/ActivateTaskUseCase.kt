package io.github.loskovdm.domain.usecase.task

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ActivateTaskUseCase(
    private val repository: TaskRepository,
) {
    suspend operator fun invoke(
        id: Uuid,
        name: String,
        projectId: Uuid,
    ) {
        val task = Task(
            id = id,
            name = name,
            projectId = projectId,
            isCompleted = false,
        )
        repository.updateTask(task = task)
    }
}
package io.github.loskovdm.domain.usecase.task

import io.github.loskovdm.domain.model.Task
import io.github.loskovdm.domain.repository.TaskRepository
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class AddTaskUseCase(
    private val repository: TaskRepository,
) {
    @OptIn(ExperimentalUuidApi::class)
    suspend operator fun invoke(
        name: String,
        projectId: Uuid,
    ) {
        val task = Task(
            id = Uuid.generateV7(),
            name = name,
            projectId = projectId,
        )
        repository.addTask(task = task)
    }
}
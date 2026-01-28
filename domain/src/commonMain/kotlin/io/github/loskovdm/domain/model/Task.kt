package io.github.loskovdm.domain.model

data class Task(
    val id: Long = 0,
    val name: String,
    val projectId: Long,
)

package io.github.loskovdm.domain.error

sealed class Result<out T, out E> {
    data class Success<T, E>(
        val data: T
    ) : Result<T, E>()

    data class Failure<T, E>(
        val error: E
    ) : Result<T, E>()
}
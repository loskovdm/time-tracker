package io.github.loskovdm.domain.error

sealed class DomainError {
    object SecondActiveTimeEntry : DomainError()
}
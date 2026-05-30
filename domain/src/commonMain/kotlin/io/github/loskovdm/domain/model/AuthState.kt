package io.github.loskovdm.domain.model

sealed interface AuthState {
    data object Loading : AuthState

    data object Guest : AuthState

    data class Authenticated(
        val email: String,
        val userId: String,
    ) : AuthState
}

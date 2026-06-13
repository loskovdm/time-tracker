package io.github.loskovdm.domain.usecase.auth

import io.github.loskovdm.domain.repository.AuthRepository

class RequestPasswordResetUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String) {
        authRepository.requestPasswordReset(email.trim())
    }
}

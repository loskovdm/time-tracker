package io.github.loskovdm.domain.usecase.auth

import io.github.loskovdm.domain.repository.AuthRepository

class UpdatePasswordUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(newPassword: String) {
        authRepository.updatePassword(newPassword)
    }
}

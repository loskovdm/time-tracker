package io.github.loskovdm.domain.usecase.auth

import io.github.loskovdm.domain.repository.AuthRepository

class SignUpUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String) {
        authRepository.signUp(email.trim(), password)
    }
}

package io.github.loskovdm.domain.usecase.auth

import io.github.loskovdm.domain.repository.AuthRepository

class SignInUseCase(
    private val authRepository: AuthRepository,
) {
    suspend operator fun invoke(email: String, password: String) {
        authRepository.signIn(email.trim(), password)
    }
}

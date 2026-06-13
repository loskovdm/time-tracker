package io.github.loskovdm.domain.model

sealed class AuthError {
    data object InvalidCredentials : AuthError()

    data object EmailNotConfirmed : AuthError()

    data object UserAlreadyRegistered : AuthError()

    data object WeakPassword : AuthError()

    data object InvalidOtp : AuthError()

    data object PasswordMismatch : AuthError()

    data object RateLimited : AuthError()

    data object Network : AuthError()

    data object Generic : AuthError()
}

class AuthException(val error: AuthError) : Exception()

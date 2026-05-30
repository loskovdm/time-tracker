package io.github.loskovdm.timetracker.supabaseclient

import io.github.loskovdm.domain.model.AuthError
import io.github.loskovdm.domain.model.AuthException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException

internal object SupabaseAuthErrorMapper {
    fun map(throwable: Throwable): AuthException {
        val message = throwable.message?.lowercase().orEmpty()
        val error = when {
            throwable is HttpRequestTimeoutException ||
                throwable is SocketTimeoutException ||
                message.contains("network") ||
                message.contains("connection") ||
                message.contains("timeout") -> AuthError.Network

            message.contains("invalid login credentials") ||
                message.contains("invalid email or password") ||
                message.contains("invalid_credentials") -> AuthError.InvalidCredentials

            message.contains("email not confirmed") ||
                message.contains("email_not_confirmed") -> AuthError.EmailNotConfirmed

            message.contains("user already registered") ||
                message.contains("already registered") ||
                message.contains("already exists") -> AuthError.UserAlreadyRegistered

            message.contains("password") && (
                message.contains("weak") ||
                    message.contains("short") ||
                    message.contains("at least")
                ) -> AuthError.WeakPassword

            else -> AuthError.Generic
        }
        return AuthException(error)
    }
}

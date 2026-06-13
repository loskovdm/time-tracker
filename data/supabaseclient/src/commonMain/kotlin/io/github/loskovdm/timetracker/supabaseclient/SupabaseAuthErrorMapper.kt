package io.github.loskovdm.timetracker.supabaseclient

import io.github.loskovdm.domain.model.AuthError
import io.github.loskovdm.domain.model.AuthException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException

internal object SupabaseAuthErrorMapper {
    fun map(throwable: Throwable): AuthException {
        val message = collectMessages(throwable).lowercase()
        val error = when {
            throwable is HttpRequestTimeoutException ||
                throwable is SocketTimeoutException ||
                message.contains("network") ||
                message.contains("connection") ||
                message.contains("timeout") -> AuthError.Network

            message.contains("over_email_send_rate_limit") ||
                message.contains("rate limit") ||
                message.contains("rate_limit") ||
                message.contains("\"code\":429") -> AuthError.RateLimited

            message.contains("same_password") ||
                message.contains("different from the old password") -> AuthError.SamePassword

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

            message.contains("invalid otp") ||
                message.contains("otp_expired") ||
                message.contains("token has expired") ||
                message.contains("token is invalid") ||
                message.contains("invalid token") -> AuthError.InvalidOtp

            else -> AuthError.Generic
        }
        return AuthException(error)
    }

    private fun collectMessages(throwable: Throwable): String = buildString {
        var current: Throwable? = throwable
        while (current != null) {
            current.message?.let { appendLine(it) }
            current = current.cause
        }
    }
}

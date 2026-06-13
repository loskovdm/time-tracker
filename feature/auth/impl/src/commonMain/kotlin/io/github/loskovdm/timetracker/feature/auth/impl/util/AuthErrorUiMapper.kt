package io.github.loskovdm.timetracker.feature.auth.impl.util

import io.github.loskovdm.domain.model.AuthError
import org.jetbrains.compose.resources.StringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.auth_error_email_not_confirmed
import timetracker.designsystem.generated.resources.auth_error_generic
import timetracker.designsystem.generated.resources.auth_error_invalid_credentials
import timetracker.designsystem.generated.resources.auth_error_invalid_otp
import timetracker.designsystem.generated.resources.auth_error_password_mismatch
import timetracker.designsystem.generated.resources.auth_error_same_password
import timetracker.designsystem.generated.resources.auth_error_rate_limit
import timetracker.designsystem.generated.resources.auth_error_network
import timetracker.designsystem.generated.resources.auth_error_user_already_registered
import timetracker.designsystem.generated.resources.auth_error_weak_password

internal fun AuthError.toMessageRes(): StringResource =
    when (this) {
        AuthError.InvalidCredentials -> Res.string.auth_error_invalid_credentials
        AuthError.EmailNotConfirmed -> Res.string.auth_error_email_not_confirmed
        AuthError.UserAlreadyRegistered -> Res.string.auth_error_user_already_registered
        AuthError.WeakPassword -> Res.string.auth_error_weak_password
        AuthError.InvalidOtp -> Res.string.auth_error_invalid_otp
        AuthError.PasswordMismatch -> Res.string.auth_error_password_mismatch
        AuthError.RateLimited -> Res.string.auth_error_rate_limit
        AuthError.SamePassword -> Res.string.auth_error_same_password
        AuthError.Network -> Res.string.auth_error_network
        AuthError.Generic -> Res.string.auth_error_generic
    }

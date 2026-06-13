package io.github.loskovdm.timetracker.feature.auth.impl.presentation.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.github.loskovdm.timetracker.feature.auth.api.ChangePasswordDestination
import io.github.loskovdm.timetracker.feature.auth.impl.util.toMessageRes
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.auth_confirm_code
import timetracker.designsystem.generated.resources.auth_confirmation_code_sent_message
import timetracker.designsystem.generated.resources.auth_confirmation_code_sent_title
import timetracker.designsystem.generated.resources.auth_email_invalid
import timetracker.designsystem.generated.resources.auth_otp_invalid
import timetracker.designsystem.generated.resources.auth_resend_code
import timetracker.designsystem.generated.resources.auth_forgot_password
import timetracker.designsystem.generated.resources.auth_sign_in
import timetracker.designsystem.generated.resources.auth_sign_up
import timetracker.designsystem.generated.resources.auth_subtitle
import timetracker.designsystem.generated.resources.auth_switch_to_sign_in
import timetracker.designsystem.generated.resources.auth_switch_to_sign_up
import timetracker.designsystem.generated.resources.auth_verify_email_message
import timetracker.designsystem.generated.resources.auth_verification_code
import timetracker.designsystem.generated.resources.error
import timetracker.designsystem.generated.resources.ok
import timetracker.designsystem.generated.resources.settings_email
import timetracker.designsystem.generated.resources.settings_password

@Composable
internal fun AuthScreen(
    contentPadding: PaddingValues = PaddingValues(),
    navigator: Navigator,
    viewModel: AuthViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.finishAuth.collect {
            navigator.goBack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(contentPadding)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        when (uiState.step) {
            AuthStep.Credentials -> CredentialsStep(
                uiState = uiState,
                onEmailChange = viewModel::onEmailChange,
                onPasswordChange = viewModel::onPasswordChange,
                onSubmit = viewModel::onSubmit,
                onToggleMode = viewModel::onToggleMode,
                onForgotPassword = {
                    navigator.goTo(ChangePasswordDestination(closeAuthOnSuccess = true))
                },
            )

            AuthStep.VerifySignupOtp -> VerifySignupOtpStep(
                uiState = uiState,
                onOtpCodeChange = viewModel::onOtpCodeChange,
                onSubmit = viewModel::onSubmit,
                onResendCode = viewModel::onResendConfirmationCode,
            )
        }
    }

    when (val dialog = uiState.dialog) {
        is AuthDialogState.Error -> {
            AlertDialog(
                onDismissRequest = viewModel::onDismissDialog,
                title = {
                    Text(
                        text = stringResource(Res.string.error),
                        style = MaterialTheme.typography.titleLarge,
                    )
                },
                text = {
                    Text(
                        text = stringResource(dialog.error.toMessageRes()),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                confirmButton = {
                    TextButton(onClick = viewModel::onDismissDialog) {
                        Text(stringResource(Res.string.ok))
                    }
                },
            )
        }

        AuthDialogState.ConfirmationCodeSent -> {
            AlertDialog(
                onDismissRequest = viewModel::onDismissDialog,
                title = { Text(stringResource(Res.string.auth_confirmation_code_sent_title)) },
                text = { Text(stringResource(Res.string.auth_confirmation_code_sent_message)) },
                confirmButton = {
                    TextButton(onClick = viewModel::onDismissDialog) {
                        Text(stringResource(Res.string.ok))
                    }
                },
            )
        }

        null -> Unit
    }
}

@Composable
private fun ColumnScope.CredentialsStep(
    uiState: AuthUiState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onToggleMode: () -> Unit,
    onForgotPassword: () -> Unit,
) {
    Text(
        text = stringResource(Res.string.auth_subtitle),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    OutlinedTextField(
        value = uiState.email,
        onValueChange = onEmailChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(Res.string.settings_email)) },
        isError = uiState.emailInvalid,
        supportingText = if (uiState.emailInvalid) {
            { Text(stringResource(Res.string.auth_email_invalid)) }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
        enabled = !uiState.isLoading,
    )

    OutlinedTextField(
        value = uiState.password,
        onValueChange = onPasswordChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(Res.string.settings_password)) },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        enabled = !uiState.isLoading,
    )

    Button(
        onClick = onSubmit,
        modifier = Modifier.fillMaxWidth(),
        enabled = !uiState.isLoading,
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(4.dp),
                strokeWidth = 2.dp,
            )
        } else {
            Text(
                when (uiState.mode) {
                    AuthMode.SignIn -> stringResource(Res.string.auth_sign_in)
                    AuthMode.SignUp -> stringResource(Res.string.auth_sign_up)
                },
            )
        }
    }

    TextButton(
        onClick = onToggleMode,
        modifier = Modifier.align(Alignment.CenterHorizontally),
        enabled = !uiState.isLoading,
    ) {
        Text(
            when (uiState.mode) {
                AuthMode.SignIn -> stringResource(Res.string.auth_switch_to_sign_up)
                AuthMode.SignUp -> stringResource(Res.string.auth_switch_to_sign_in)
            },
        )
    }

    if (uiState.mode == AuthMode.SignIn) {
        TextButton(
            onClick = onForgotPassword,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            enabled = !uiState.isLoading,
        ) {
            Text(stringResource(Res.string.auth_forgot_password))
        }
    }
}

@Composable
private fun ColumnScope.VerifySignupOtpStep(
    uiState: AuthUiState,
    onOtpCodeChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onResendCode: () -> Unit,
) {
    Text(
        text = stringResource(Res.string.auth_verify_email_message, uiState.email),
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )

    OutlinedTextField(
        value = uiState.otpCode,
        onValueChange = onOtpCodeChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(Res.string.auth_verification_code)) },
        isError = uiState.otpInvalid,
        supportingText = if (uiState.otpInvalid) {
            { Text(stringResource(Res.string.auth_otp_invalid)) }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        enabled = !uiState.isLoading,
    )

    Button(
        onClick = onSubmit,
        modifier = Modifier.fillMaxWidth(),
        enabled = !uiState.isLoading,
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(4.dp),
                strokeWidth = 2.dp,
            )
        } else {
            Text(stringResource(Res.string.auth_confirm_code))
        }
    }

    TextButton(
        onClick = onResendCode,
        modifier = Modifier.align(Alignment.CenterHorizontally),
        enabled = !uiState.isLoading,
    ) {
        Text(stringResource(Res.string.auth_resend_code))
    }
}

package io.github.loskovdm.timetracker.feature.auth.impl.presentation.changepassword

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
import org.koin.core.parameter.parametersOf
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.auth_change_password_message
import timetracker.designsystem.generated.resources.auth_confirm_code
import timetracker.designsystem.generated.resources.auth_email_invalid
import timetracker.designsystem.generated.resources.auth_error_password_mismatch
import timetracker.designsystem.generated.resources.auth_error_weak_password
import timetracker.designsystem.generated.resources.auth_new_password
import timetracker.designsystem.generated.resources.auth_otp_invalid
import timetracker.designsystem.generated.resources.auth_password_updated_message
import timetracker.designsystem.generated.resources.auth_password_updated_title
import timetracker.designsystem.generated.resources.auth_reset_code_sent_message
import timetracker.designsystem.generated.resources.auth_reset_code_sent_title
import timetracker.designsystem.generated.resources.auth_reset_password_message
import timetracker.designsystem.generated.resources.auth_resend_code
import timetracker.designsystem.generated.resources.auth_save_password
import timetracker.designsystem.generated.resources.auth_send_reset_code
import timetracker.designsystem.generated.resources.auth_verify_reset_message
import timetracker.designsystem.generated.resources.auth_verification_code
import timetracker.designsystem.generated.resources.error
import timetracker.designsystem.generated.resources.ok
import timetracker.designsystem.generated.resources.settings_confirm_password
import timetracker.designsystem.generated.resources.settings_email

@Composable
internal fun ChangePasswordScreen(
    destination: ChangePasswordDestination,
    contentPadding: PaddingValues = PaddingValues(),
    navigator: Navigator,
    viewModel: ChangePasswordViewModel = koinViewModel { parametersOf(destination) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, destination.closeAuthOnSuccess) {
        viewModel.finish.collect {
            navigator.goBack()
            if (destination.closeAuthOnSuccess) {
                navigator.goBack()
            }
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
            ChangePasswordStep.RequestEmail -> RequestResetStep(
                uiState = uiState,
                onEmailChange = viewModel::onEmailChange,
                onSubmit = viewModel::onSubmit,
            )

            ChangePasswordStep.VerifyOtp -> VerifyResetOtpStep(
                uiState = uiState,
                onOtpCodeChange = viewModel::onOtpCodeChange,
                onSubmit = viewModel::onSubmit,
                onResendCode = viewModel::onResendCode,
            )

            ChangePasswordStep.SetNewPassword -> SetNewPasswordStep(
                uiState = uiState,
                onNewPasswordChange = viewModel::onNewPasswordChange,
                onConfirmPasswordChange = viewModel::onConfirmPasswordChange,
                onSubmit = viewModel::onSubmit,
            )
        }
    }

    when (val dialog = uiState.dialog) {
        is ChangePasswordDialogState.Error -> {
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

        ChangePasswordDialogState.ResetCodeSent -> {
            AlertDialog(
                onDismissRequest = viewModel::onDismissDialog,
                title = { Text(stringResource(Res.string.auth_reset_code_sent_title)) },
                text = { Text(stringResource(Res.string.auth_reset_code_sent_message)) },
                confirmButton = {
                    TextButton(onClick = viewModel::onDismissDialog) {
                        Text(stringResource(Res.string.ok))
                    }
                },
            )
        }

        ChangePasswordDialogState.PasswordUpdated -> {
            AlertDialog(
                onDismissRequest = viewModel::onDismissDialog,
                title = { Text(stringResource(Res.string.auth_password_updated_title)) },
                text = { Text(stringResource(Res.string.auth_password_updated_message)) },
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
private fun ColumnScope.RequestResetStep(
    uiState: ChangePasswordUiState,
    onEmailChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    Text(
        text = if (uiState.isChangePasswordFlow) {
            stringResource(Res.string.auth_change_password_message, uiState.email)
        } else {
            stringResource(Res.string.auth_reset_password_message)
        },
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
        enabled = !uiState.isLoading && !uiState.isChangePasswordFlow,
        readOnly = uiState.isChangePasswordFlow,
    )

    SubmitButton(
        isLoading = uiState.isLoading,
        label = stringResource(Res.string.auth_send_reset_code),
        onClick = onSubmit,
    )
}

@Composable
private fun ColumnScope.VerifyResetOtpStep(
    uiState: ChangePasswordUiState,
    onOtpCodeChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onResendCode: () -> Unit,
) {
    Text(
        text = stringResource(Res.string.auth_verify_reset_message, uiState.email),
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

    SubmitButton(
        isLoading = uiState.isLoading,
        label = stringResource(Res.string.auth_confirm_code),
        onClick = onSubmit,
    )

    TextButton(
        onClick = onResendCode,
        modifier = Modifier.align(Alignment.CenterHorizontally),
        enabled = !uiState.isLoading,
    ) {
        Text(stringResource(Res.string.auth_resend_code))
    }
}

@Composable
private fun ColumnScope.SetNewPasswordStep(
    uiState: ChangePasswordUiState,
    onNewPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    onSubmit: () -> Unit,
) {
    OutlinedTextField(
        value = uiState.newPassword,
        onValueChange = onNewPasswordChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(Res.string.auth_new_password)) },
        isError = uiState.passwordInvalid,
        supportingText = if (uiState.passwordInvalid) {
            { Text(stringResource(Res.string.auth_error_weak_password)) }
        } else {
            null
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        enabled = !uiState.isLoading,
    )

    OutlinedTextField(
        value = uiState.confirmPassword,
        onValueChange = onConfirmPasswordChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(stringResource(Res.string.settings_confirm_password)) },
        isError = uiState.passwordMismatch,
        supportingText = if (uiState.passwordMismatch) {
            { Text(stringResource(Res.string.auth_error_password_mismatch)) }
        } else {
            null
        },
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        enabled = !uiState.isLoading,
    )

    SubmitButton(
        isLoading = uiState.isLoading,
        label = stringResource(Res.string.auth_save_password),
        onClick = onSubmit,
    )
}

@Composable
private fun ColumnScope.SubmitButton(
    isLoading: Boolean,
    label: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = !isLoading,
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.padding(4.dp),
                strokeWidth = 2.dp,
            )
        } else {
            Text(label)
        }
    }
}

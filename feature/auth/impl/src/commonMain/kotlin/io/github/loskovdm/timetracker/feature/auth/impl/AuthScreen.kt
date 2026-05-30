package io.github.loskovdm.timetracker.feature.auth.impl

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import io.github.loskovdm.timetracker.feature.auth.impl.presentation.AuthDialogState
import io.github.loskovdm.timetracker.feature.auth.impl.presentation.AuthMode
import io.github.loskovdm.timetracker.feature.auth.impl.presentation.AuthViewModel
import io.github.loskovdm.timetracker.feature.auth.impl.presentation.toMessageRes
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.auth_email_invalid
import timetracker.designsystem.generated.resources.auth_registration_email_sent_message
import timetracker.designsystem.generated.resources.auth_registration_email_sent_title
import timetracker.designsystem.generated.resources.auth_sign_in
import timetracker.designsystem.generated.resources.auth_sign_up
import timetracker.designsystem.generated.resources.auth_subtitle
import timetracker.designsystem.generated.resources.auth_switch_to_sign_in
import timetracker.designsystem.generated.resources.auth_switch_to_sign_up
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
        Text(
            text = stringResource(Res.string.auth_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        OutlinedTextField(
            value = uiState.email,
            onValueChange = viewModel::onEmailChange,
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
            onValueChange = viewModel::onPasswordChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(Res.string.settings_password)) },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            enabled = !uiState.isLoading,
        )

        Button(
            onClick = viewModel::onSubmit,
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
            onClick = viewModel::onToggleMode,
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

        is AuthDialogState.RegistrationEmailSent -> {
            AlertDialog(
                onDismissRequest = viewModel::onDismissDialog,
                title = { Text(stringResource(Res.string.auth_registration_email_sent_title)) },
                text = {
                    Text(
                        stringResource(
                            Res.string.auth_registration_email_sent_message,
                            dialog.email,
                        ),
                    )
                },
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

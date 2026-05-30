package io.github.loskovdm.timetracker.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.auth_guest_migration_discard
import timetracker.designsystem.generated.resources.auth_guest_migration_message
import timetracker.designsystem.generated.resources.auth_guest_migration_migrate
import timetracker.designsystem.generated.resources.auth_guest_migration_title

@Composable
fun GuestMigrationDialog(
    isProcessing: Boolean,
    onMigrate: () -> Unit,
    onDiscard: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = {},
        title = { Text(stringResource(Res.string.auth_guest_migration_title)) },
        text = { Text(stringResource(Res.string.auth_guest_migration_message)) },
        confirmButton = {
            TextButton(
                onClick = onMigrate,
                enabled = !isProcessing,
            ) {
                if (isProcessing) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(4.dp),
                        strokeWidth = 2.dp,
                    )
                } else {
                    Text(stringResource(Res.string.auth_guest_migration_migrate))
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDiscard,
                enabled = !isProcessing,
            ) {
                Text(stringResource(Res.string.auth_guest_migration_discard))
            }
        },
    )
}

package io.github.loskovdm.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.cancel
import timetracker.designsystem.generated.resources.delete_time_entries_action
import timetracker.designsystem.generated.resources.keep_time_entries_action

@Composable
fun DeleteWithTimeEntriesDialog(
    title: String,
    message: String,
    onDeleteTimeEntries: () -> Unit,
    onKeepTimeEntries: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onDeleteTimeEntries,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                    ),
                ) {
                    Text(
                        text = stringResource(Res.string.delete_time_entries_action),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
                OutlinedButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = onKeepTimeEntries,
                ) {
                    Text(
                        text = stringResource(Res.string.keep_time_entries_action),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.cancel))
            }
        },
    )
}

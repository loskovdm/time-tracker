package io.github.loskovdm.designsystem.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.cancel
import timetracker.designsystem.generated.resources.delete_strategy_title
import timetracker.designsystem.generated.resources.delete_time_entries_action
import timetracker.designsystem.generated.resources.keep_time_entries_action

@Composable
fun DeleteWithTimeEntriesDialog(
    message: String,
    onDeleteTimeEntries: () -> Unit,
    onKeepTimeEntries: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(Res.string.delete_strategy_title),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.End,
                ) {
                    TextButton(onClick = onDeleteTimeEntries) {
                        Text(
                            text = stringResource(Res.string.delete_time_entries_action),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                    TextButton(onClick = onKeepTimeEntries) {
                        Text(
                            text = stringResource(Res.string.keep_time_entries_action),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
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

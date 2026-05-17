package io.github.loskovdm.designsystem.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.stringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.error
import timetracker.designsystem.generated.resources.ok

@Composable
fun ErrorDialog(
	message: String,
	onDismiss: () -> Unit,
) {
	AlertDialog(
		onDismissRequest = onDismiss,
		title = {
			Text(
				text = stringResource(Res.string.error),
				style = MaterialTheme.typography.titleLarge,
			)
		},
		text = {
			Text(
				text = message,
				style = MaterialTheme.typography.bodyMedium,
			)
		},
		confirmButton = {
			TextButton(onClick = onDismiss) {
				Text(stringResource(Res.string.ok))
			}
		},
	)
}

@Preview
@Composable
fun ErrorDialogPreview() {
	MaterialTheme {
		ErrorDialog(
			message = "An error occurred while fetching data.",
			onDismiss = {},
		)
	}
}
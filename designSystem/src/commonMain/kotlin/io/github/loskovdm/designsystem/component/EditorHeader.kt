package io.github.loskovdm.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.close
import timetracker.designsystem.generated.resources.ic_close
import timetracker.designsystem.generated.resources.save

@Composable
fun EditorHeader(
    modifier: Modifier = Modifier,
    onClose: () -> Unit,
    onSave: () -> Unit,
    isAvailableSave: Boolean,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(4.dp)
        ,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        IconButton(
            onClick = onClose,
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_close),
                contentDescription = stringResource(Res.string.close),
            )
        }
        TextButton(
            onClick = onSave,
            enabled = isAvailableSave,
        ) {
            Text(
                text = stringResource(Res.string.save).uppercase(),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}
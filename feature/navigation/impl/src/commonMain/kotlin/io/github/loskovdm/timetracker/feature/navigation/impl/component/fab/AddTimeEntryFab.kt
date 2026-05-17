package io.github.loskovdm.timetracker.feature.navigation.impl.component.fab

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.add_entry
import timetracker.designsystem.generated.resources.ic_add_entry

@Composable
fun AddTimeEntryFab(
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        text = { Text(stringResource(Res.string.add_entry)) },
        icon = {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_add_entry),
                contentDescription = stringResource(Res.string.add_entry)
            )
        },
        expanded = isExpanded,
    )
}
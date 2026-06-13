package io.github.loskovdm.timetracker.feature.navigation.impl.component.fab

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import io.github.loskovdm.designsystem.component.PlainTooltipBox
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.add_entry
import timetracker.designsystem.generated.resources.ic_add_entry

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTimeEntryFab(
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val tooltip = stringResource(Res.string.add_entry)
    PlainTooltipBox(
        tooltip = tooltip,
        enabled = !isExpanded,
    ) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            text = { Text(tooltip) },
            icon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_add_entry),
                    contentDescription = tooltip,
                )
            },
            expanded = isExpanded,
        )
    }
}
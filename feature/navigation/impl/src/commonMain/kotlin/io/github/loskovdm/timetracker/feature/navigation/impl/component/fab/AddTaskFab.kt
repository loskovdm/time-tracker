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
import timetracker.designsystem.generated.resources.add_task
import timetracker.designsystem.generated.resources.ic_add_task_filled

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskFab(
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val tooltip = stringResource(Res.string.add_task)
    PlainTooltipBox(
        tooltip = tooltip,
        enabled = !isExpanded,
    ) {
        ExtendedFloatingActionButton(
            expanded = isExpanded,
            onClick = onClick,
            text = { Text(tooltip) },
            icon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_add_task_filled),
                    contentDescription = tooltip,
                )
            }
        )
    }
}
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
import timetracker.designsystem.generated.resources.ic_start_filled
import timetracker.designsystem.generated.resources.start
import timetracker.designsystem.generated.resources.start_timer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartTimerFab(
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    val tooltip = stringResource(Res.string.start_timer)
    PlainTooltipBox(
        tooltip = tooltip,
        enabled = !isExpanded,
    ) {
        ExtendedFloatingActionButton(
            onClick = onClick,
            expanded = isExpanded,
            text = { Text(stringResource(Res.string.start)) },
            icon = {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_start_filled),
                    contentDescription = tooltip,
                )
            }
        )
    }
}
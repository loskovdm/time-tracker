package io.github.loskovdm.timetracker.feature.navigation.impl.component.fab

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.ic_stop_filled
import timetracker.designsystem.generated.resources.stop
import timetracker.designsystem.generated.resources.stop_timer

@Composable
fun StopTimerFab(
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        expanded = isExpanded,
        onClick = onClick,
        text = { Text(stringResource(Res.string.stop)) },
        icon = {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_stop_filled),
                contentDescription = stringResource(Res.string.stop_timer)
            )
        }
    )
}
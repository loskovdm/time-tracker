package io.github.loskovdm.timetracker.feature.navigation.impl.component.fab

import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.ic_share_filled
import timetracker.designsystem.generated.resources.share

@Composable
fun ShareFab(
    isExpanded: Boolean,
    onClick: () -> Unit
) {
    ExtendedFloatingActionButton(
        expanded = isExpanded,
        onClick = onClick,
        text = { Text(stringResource(Res.string.share)) },
        icon = {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_share_filled),
                contentDescription = stringResource(Res.string.share)
            )
        }
    )
}
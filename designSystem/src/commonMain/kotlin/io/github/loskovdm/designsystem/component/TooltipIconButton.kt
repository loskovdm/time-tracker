package io.github.loskovdm.designsystem.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TooltipIconButton(
    onClick: () -> Unit,
    tooltip: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    tooltipEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    PlainTooltipBox(
        tooltip = tooltip,
        enabled = tooltipEnabled,
        modifier = modifier,
    ) {
        IconButton(onClick = onClick, enabled = enabled) {
            content()
        }
    }
}

package io.github.loskovdm.timer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Composable
fun TimerNavigation(
    modifier: Modifier = Modifier,
    appBottomBar: @Composable () -> Unit,
    settingsIconResource: DrawableResource,
    titleResource: StringResource,
    onSettings: () -> Unit,
) {
    TimerScreen(
        modifier = modifier,
        appBottomBar = appBottomBar,
        settingsIconResource = settingsIconResource,
        titleResource = titleResource,
        onSettings = {
            onSettings()
        },
    )
}
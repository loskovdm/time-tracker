package io.github.loskovdm.timetracker.feature.navigation.impl.component.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import io.github.loskovdm.designsystem.component.TooltipIconButton
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.archive
import timetracker.designsystem.generated.resources.ic_archive_filled
import timetracker.designsystem.generated.resources.ic_archive_outlined
import timetracker.designsystem.generated.resources.ic_settings_filled
import timetracker.designsystem.generated.resources.projects
import timetracker.designsystem.generated.resources.settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveProjectsTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    onArchivedProjects: () -> Unit,
    onSettings: () -> Unit,
) {
    val deviceConfiguration = LocalDeviceConfiguration.current

    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(Res.string.projects),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        scrollBehavior = scrollBehavior,
        actions = {
            TooltipIconButton(
                onClick = onArchivedProjects,
                tooltip = stringResource(Res.string.archive),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_archive_filled),
                    contentDescription = stringResource(Res.string.archive)
                )
            }
            TooltipIconButton(
                onClick = onSettings,
                tooltip = stringResource(Res.string.settings),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_settings_filled),
                    contentDescription = stringResource(Res.string.settings)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            },
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
        ),
    )
}
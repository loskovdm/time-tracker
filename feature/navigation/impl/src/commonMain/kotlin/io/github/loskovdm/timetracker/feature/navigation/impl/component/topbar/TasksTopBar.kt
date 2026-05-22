package io.github.loskovdm.timetracker.feature.navigation.impl.component.topbar

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.back
import timetracker.designsystem.generated.resources.ic_arrow_back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksTopBar(
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior,
    projectName: String?,
    onBack: () -> Unit,
) {
    val deviceConfiguration = LocalDeviceConfiguration.current

    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        title = {
            Text(
                text = projectName ?: "",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = vectorResource(Res.drawable.ic_arrow_back),
                    contentDescription = stringResource(Res.string.back)
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
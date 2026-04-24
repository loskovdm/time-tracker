package io.github.loskovdm.projects.projects

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavKey
import io.github.loskovdm.designsystem.component.HomeScreen
import io.github.loskovdm.designsystem.navigation.NavigationItem
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.add_project
import timetracker.designsystem.generated.resources.ic_add_project_filled
import timetracker.designsystem.generated.resources.ic_settings_outlined
import timetracker.designsystem.generated.resources.projects
import timetracker.designsystem.generated.resources.settings
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun ProjectsScreen(
    modifier: Modifier = Modifier,
    viewModel: ProjectsViewModel = koinViewModel(),
    railState: WideNavigationRailState,
    deviceConfiguration: DeviceConfiguration,
    navigationItems: Map<NavKey, NavigationItem>,
    selectedNavigationItem: NavKey,
    onSelectedNavigationItem: (NavKey) -> Unit,
    onSettings: () -> Unit,
    onTasks: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    HomeScreen(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        deviceConfiguration = deviceConfiguration,
        topBar = {
            ProjectsTopBar(
                deviceConfiguration = deviceConfiguration,
                scrollBehavior = scrollBehavior,
                onSettings = onSettings,
            )
        },
        railState = railState,
        navigationItems = navigationItems,
        selectedNavigationItem = selectedNavigationItem,
        onSelectedNavigationItem = onSelectedNavigationItem,
        iconFloutingActionButton = Res.drawable.ic_add_project_filled,
        labelFloutingActionButton = Res.string.add_project,
        onClickFloutingActionButton = {
            // TODO:
        },
    ) {
//        Box(
//            modifier = modifier.fillMaxSize(),
//            contentAlignment = Alignment.Center,
//        ) {
//            Button(
//                onClick = onTasks,
//            ) {
//                Text("Button")
//            }
//        }

        val uiState = viewModel.uiState.collectAsState()

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            OutlinedTextField(
                value = uiState.value.projectName,
                onValueChange = viewModel::onProjectNameChange,
                label = { Text("Название проекта") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = viewModel::onAddClick,
                enabled = uiState.value.projectName.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Добавить")
            }

            Spacer(Modifier.height(16.dp))

            LazyColumn {
                items(uiState.value.projects, key = { it.id }) { project ->
                    Text(project.name, modifier = Modifier.padding(vertical = 8.dp))
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectsTopBar(
    modifier: Modifier = Modifier,
    deviceConfiguration: DeviceConfiguration,
    scrollBehavior: TopAppBarScrollBehavior,
    onSettings: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(stringResource(Res.string.projects))
        },
        actions = {
            IconButton(
                onClick = onSettings,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_settings_outlined),
                    contentDescription = stringResource(Res.string.settings)
                )
            }
        },
        colors = TopAppBarColors(
            containerColor = if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceContainer
            },
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            subtitleContentColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        scrollBehavior = scrollBehavior,
    )
}
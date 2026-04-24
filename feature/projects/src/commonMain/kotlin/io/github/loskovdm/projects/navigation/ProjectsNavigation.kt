package io.github.loskovdm.projects.navigation

import androidx.compose.material3.WideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.designsystem.navigation.NavigationItem
import io.github.loskovdm.projects.projects.ProjectsScreen
import io.github.loskovdm.projects.tasks.TasksScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun ProjectsNavigation(
    modifier: Modifier = Modifier,
    railState: WideNavigationRailState,
    onSettings: () -> Unit,
    deviceConfiguration: DeviceConfiguration,
    navigationItems: Map<NavKey, NavigationItem>,
    selectedNavigationItem: NavKey,
    onSelectedNavigationItem: (NavKey) -> Unit,
) {
    val projectsBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(ProjectsRoute.Projects::class, ProjectsRoute.Projects.serializer())
                    subclass(ProjectsRoute.Tasks::class, ProjectsRoute.Tasks.serializer())
                }
            }
        },
        ProjectsRoute.Projects
    )
    NavDisplay(
        modifier = modifier,
        backStack = projectsBackStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<ProjectsRoute.Projects> {
                ProjectsScreen(
                    railState = railState,
                    navigationItems = navigationItems,
                    deviceConfiguration = deviceConfiguration,
                    selectedNavigationItem = selectedNavigationItem,
                    onSelectedNavigationItem = { item ->
                        onSelectedNavigationItem(item)
                    },
                    onSettings = {
                        onSettings()
                    },
                    onTasks = {
                        projectsBackStack.add(ProjectsRoute.Tasks("Project-test"))
                    },
                )
            }
            entry<ProjectsRoute.Tasks> {
                TasksScreen(
                    railState = railState,
                    navigationItems = navigationItems,
                    deviceConfiguration = deviceConfiguration,
                    selectedNavigationItem = selectedNavigationItem,
                    onSelectedNavigationItem = { item ->
                        onSelectedNavigationItem(item)
                    },
                    projectName = it.projectName,
                    onBack = {
                        projectsBackStack.remove(ProjectsRoute.Tasks(it.projectName))
                    },
                )
            }
        }
    )
}
package io.github.loskovdm.projects.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import io.github.loskovdm.projects.ProjectsScreen
import io.github.loskovdm.projects.TasksScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

@Composable
fun ProjectsNavigation(
    modifier: Modifier = Modifier,
    appBottomBar: @Composable () -> Unit,
    titleResource: StringResource,
    settingsIconResource: DrawableResource,
    backIconDescriptionResource: StringResource,
    backIconResource: DrawableResource,
    onSettings: () -> Unit,
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
                    appBottomBar = appBottomBar,
                    titleResource = titleResource,
                    settingsIconResource = settingsIconResource,
                    onSettings = {
                        onSettings()
                    },
                    onTasks = {
                        projectsBackStack.add(ProjectsRoute.Tasks("Project-test"))
                    }
                )
            }
            entry<ProjectsRoute.Tasks> {
                TasksScreen(
                    projectName = it.projectName,
                    appBottomBar = appBottomBar,
                    backIconDescriptionResource = backIconDescriptionResource,
                    backIconResource = backIconResource,
                    onBack = {
                        projectsBackStack.remove(ProjectsRoute.Tasks(it.projectName))
                    },
                )
            }
        }
    )
}
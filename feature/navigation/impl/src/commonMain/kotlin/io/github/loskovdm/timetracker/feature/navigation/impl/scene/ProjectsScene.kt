package io.github.loskovdm.timetracker.feature.navigation.impl.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.contains
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadata
import io.github.loskovdm.timetracker.feature.navigation.api.SceneMetadataBuilder

data class ProjectsScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val projectsListEntry: NavEntry<T>,
    val tasksListEntry: NavEntry<T>?,
    val deviceConfiguration: DeviceConfiguration,
    val isArchived: Boolean,
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOf(projectsListEntry)
    override val content: @Composable (() -> Unit) = {
        if (tasksListEntry == null) {
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                ) {
                    projectsListEntry.Content()
                }
            }
        } else {
            Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surface)
                        .fillMaxSize()
                ) {
                    Column {
                        tasksListEntry.Content()
                    }

                }
            }
        }
    }
    override val metadata: Map<String, Any> =
//        if (tasksListEntry == null) {
//            SceneMetadataBuilder.projects()
//        } else {
//            if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT ||
//                deviceConfiguration == DeviceConfiguration.MOBILE_LANDSCAPE ||
//                deviceConfiguration == DeviceConfiguration.TABLET_PORTRAIT
//            ) {
//                SceneMetadataBuilder.tasks()
//            } else {
//                SceneMetadataBuilder.projects()
//            }
//        }
        if (tasksListEntry == null) {
            println("_________$isArchived")
            if (isArchived) {
                SceneMetadataBuilder.archivedProjects()
            } else {
                SceneMetadataBuilder.activeProjects()
            }
        } else {
            SceneMetadataBuilder.tasks()
        }
}

@Composable
fun <T : Any> rememberProjectsSceneStrategy(): ProjectsSceneStrategy<T> {
    val deviceConfiguration = LocalDeviceConfiguration.current
    return remember(deviceConfiguration) {
        ProjectsSceneStrategy(deviceConfiguration)
    }
}

class ProjectsSceneStrategy<T : Any>(
    private val deviceConfiguration: DeviceConfiguration,
) : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        if (entries.last().metadata.contains(EntryMetadata.TasksListKey)) {
            val tasksListEntry = entries.last()
            val projectsListEntry =
                entries
                    .findLast {
                        it.metadata.contains(EntryMetadata.ActiveProjectsListKey) ||
                                it.metadata.contains(EntryMetadata.ArchivedProjectsListKey)
                    }
                    ?: return null

            return ProjectsScene(
                key = tasksListEntry.contentKey,
                previousEntries = entries.dropLast(1),
                deviceConfiguration = deviceConfiguration,
                projectsListEntry = projectsListEntry,
                tasksListEntry = tasksListEntry,
                isArchived = projectsListEntry.metadata.contains(EntryMetadata.ArchivedProjectsListKey)
            )
        } else {
            val projectsListEntry =
                entries.lastOrNull()
                    ?.takeIf {
                        it.metadata.contains(EntryMetadata.ActiveProjectsListKey) ||
                                it.metadata.contains(EntryMetadata.ArchivedProjectsListKey)
                    }
                    ?: return null
            println("________projectsListEntry.metadata.contains(EntryMetadata.ArchivedProjectsListKey): ${
                projectsListEntry.metadata.contains(
                    EntryMetadata.ArchivedProjectsListKey
                )
            }")
            return ProjectsScene(
                key = projectsListEntry.contentKey,
                previousEntries = entries.dropLast(1),
                deviceConfiguration = deviceConfiguration,
                projectsListEntry = projectsListEntry,
                tasksListEntry = null,
                isArchived = projectsListEntry.metadata.contains(EntryMetadata.ArchivedProjectsListKey)
            )
        }
    }
}
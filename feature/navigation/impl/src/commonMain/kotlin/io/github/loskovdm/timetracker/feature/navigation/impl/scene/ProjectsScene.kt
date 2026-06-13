@file:OptIn(kotlin.uuid.ExperimentalUuidApi::class)

package io.github.loskovdm.timetracker.feature.navigation.impl.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.contains
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import io.github.loskovdm.designsystem.component.TooltipIconButton
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadata
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import io.github.loskovdm.timetracker.feature.navigation.api.SceneMetadataBuilder
import io.github.loskovdm.timetracker.feature.tasks.api.destination.TaskEditorDestination
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.koin.compose.koinInject
import timetracker.designsystem.generated.resources.Res
import timetracker.designsystem.generated.resources.add_task
import timetracker.designsystem.generated.resources.close
import timetracker.designsystem.generated.resources.ic_add_task_filled
import timetracker.designsystem.generated.resources.ic_close
import kotlin.uuid.Uuid

data class ProjectsScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val projectsListEntry: NavEntry<T>,
    val tasksListEntry: NavEntry<T>?,
    val deviceConfiguration: DeviceConfiguration,
    val isArchived: Boolean,
    val onCloseTasks: () -> Unit,
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOfNotNull(projectsListEntry, tasksListEntry)

    val showTasksInSplitPane: Boolean =
        tasksListEntry != null && (
            deviceConfiguration == DeviceConfiguration.DESKTOP ||
                deviceConfiguration == DeviceConfiguration.TABLET_LANDSCAPE
            )

    override val content: @Composable (() -> Unit) = {
        val currentTasksListEntry = tasksListEntry
        when {
            showTasksInSplitPane && currentTasksListEntry != null -> SplitProjectsTasksContent(
                projectsListEntry = projectsListEntry,
                tasksListEntry = currentTasksListEntry,
                projectName = extractProjectName(currentTasksListEntry.contentKey),
                onCloseTasks = onCloseTasks,
            )
            currentTasksListEntry == null -> {
                Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .fillMaxSize()
                    ) {
                        projectsListEntry.Content()
                    }
                }
            }
            else -> {
                Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surface)
                            .fillMaxSize()
                    ) {
                        Column {
                            currentTasksListEntry.Content()
                        }
                    }
                }
            }
        }
    }
    override val metadata: Map<String, Any> =
        if (tasksListEntry == null || showTasksInSplitPane) {
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
                isArchived = projectsListEntry.metadata.contains(EntryMetadata.ArchivedProjectsListKey),
                onCloseTasks = onBack,
            )
        } else {
            val projectsListEntry =
                entries.lastOrNull()
                    ?.takeIf {
                        it.metadata.contains(EntryMetadata.ActiveProjectsListKey) ||
                                it.metadata.contains(EntryMetadata.ArchivedProjectsListKey)
                    }
                    ?: return null
            return ProjectsScene(
                key = projectsListEntry.contentKey,
                previousEntries = entries.dropLast(1),
                deviceConfiguration = deviceConfiguration,
                projectsListEntry = projectsListEntry,
                tasksListEntry = null,
                isArchived = projectsListEntry.metadata.contains(EntryMetadata.ArchivedProjectsListKey),
                onCloseTasks = onBack,
            )
        }
    }
}

@Composable
private fun <T : Any> SplitProjectsTasksContent(
    projectsListEntry: NavEntry<T>,
    tasksListEntry: NavEntry<T>,
    projectName: String?,
    onCloseTasks: () -> Unit,
) {
    val navigator: Navigator = koinInject()
    val projectId = extractProjectId(tasksListEntry.contentKey)

    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            ProjectsScenePane(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                projectsListEntry.Content()
            }
            Spacer(modifier = Modifier.size(16.dp))
            ProjectsScenePane(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    SplitTasksHeader(
                        projectName = projectName,
                        onClose = onCloseTasks,
                        onAddTask = {
                            projectId?.let {
                                navigator.goTo(TaskEditorDestination(projectId = it))
                            }
                        },
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        tasksListEntry.Content()
                    }
                }
            }
        }
    }
}

@Composable
private fun ProjectsScenePane(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .background(MaterialTheme.colorScheme.surface),
    ) {
        content()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SplitTasksHeader(
    projectName: String?,
    onClose: () -> Unit,
    onAddTask: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        TooltipIconButton(
            onClick = onClose,
            tooltip = stringResource(Res.string.close),
        ) {
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = vectorResource(Res.drawable.ic_close),
                contentDescription = stringResource(Res.string.close),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
        Text(
            modifier = Modifier.weight(1f),
            text = projectName.orEmpty(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        TooltipIconButton(
            onClick = onAddTask,
            tooltip = stringResource(Res.string.add_task),
        ) {
            Icon(
                imageVector = vectorResource(Res.drawable.ic_add_task_filled),
                contentDescription = stringResource(Res.string.add_task),
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

private fun extractProjectId(sceneKey: Any): Uuid? {
    var current = sceneKey
    while (current is Pair<*, *>) {
        current = current.second ?: return null
    }
    val str = current.toString()
    val marker = "projectId="
    val start = str.indexOf(marker)
    if (start == -1) return null
    val valueStart = start + marker.length
    val end = str.indexOf(',', valueStart).takeIf { it != -1 } ?: str.indexOf(')', valueStart)
    if (end == -1) return null
    return runCatching { Uuid.parse(str.substring(valueStart, end).trim()) }.getOrNull()
}

private fun extractProjectName(sceneKey: Any): String? {
    var current = sceneKey
    while (current is Pair<*, *>) {
        current = current.second ?: return null
    }
    val str = current.toString()
    val marker = "projectName="
    val start = str.indexOf(marker)
    if (start == -1) return null
    val valueStart = start + marker.length
    val end = str.indexOf(',', valueStart).takeIf { it != -1 } ?: str.indexOf(')', valueStart)
    if (end == -1) return null
    return str.substring(valueStart, end)
        .trim()
        .removeSurrounding("\"")
        .takeIf { it.isNotEmpty() }
}
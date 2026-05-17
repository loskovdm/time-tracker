package io.github.loskovdm.timetracker.feature.navigation.impl.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadata
import io.github.loskovdm.timetracker.feature.navigation.api.SceneMetadataBuilder

data class ProjectsScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val projectsListEntry: NavEntry<T>,
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOf(projectsListEntry)
    override val content: @Composable (() -> Unit) = {
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
    override val metadata: Map<String, Any> = SceneMetadataBuilder.projects()
}

@Composable
fun <T : Any> rememberProjectsSceneStrategy(): ProjectsSceneStrategy<T> {
    return remember {
        ProjectsSceneStrategy()
    }
}

class ProjectsSceneStrategy<T : Any> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val projectsListEntry =
            entries.lastOrNull()?.takeIf { it.metadata.contains(EntryMetadata.ProjectsListKey) }
                ?: return null
        val sceneKey = projectsListEntry.contentKey

        return ProjectsScene(
            key = sceneKey,
            previousEntries = entries.dropLast(1),
            projectsListEntry = projectsListEntry,
        )
    }
}
package io.github.loskovdm.timetracker.feature.navigation.impl.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
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

data class ReportsScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val reportsEntry: NavEntry<T>,
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOf(reportsEntry)
    override val content: @Composable (() -> Unit) = {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxSize()
            ) {
                reportsEntry.Content()
            }
        }
    }
    override val metadata: Map<String, Any> = SceneMetadataBuilder.reports()
}

@Composable
fun <T : Any> rememberReportsSceneStrategy(): ReportsSceneStrategy<T> {
    return remember {
        ReportsSceneStrategy()
    }
}

class ReportsSceneStrategy<T : Any> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val reportsEntry =
            entries.lastOrNull()?.takeIf { it.metadata.contains(EntryMetadata.ReportsKey) }
                ?: return null
        val sceneKey = reportsEntry.contentKey

        return ReportsScene(
            key = sceneKey,
            previousEntries = entries.dropLast(1),
            reportsEntry = reportsEntry,
        )
    }
}
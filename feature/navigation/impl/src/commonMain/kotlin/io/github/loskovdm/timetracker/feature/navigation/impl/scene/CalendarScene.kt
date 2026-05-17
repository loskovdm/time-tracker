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

data class CalendarScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    private val timeEntryCalendarEntry: NavEntry<T>,
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOf(timeEntryCalendarEntry)
    override val content: @Composable (() -> Unit) = {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxSize()
            ) {
                timeEntryCalendarEntry.Content()
            }
        }
    }
    override val metadata: Map<String, Any> = SceneMetadataBuilder.calendar()
}

@Composable
fun <T : Any> rememberCalendarSceneStrategy(): CalendarSceneStrategy<T> {
    return remember {
        CalendarSceneStrategy()
    }
}

class CalendarSceneStrategy<T : Any> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null
        if (!lastEntry.metadata.contains(EntryMetadata.TimeEntryCalendarKey)) {
            return null
        }
        val sceneKey = lastEntry.contentKey

        return CalendarScene(
            key = sceneKey,
            previousEntries = entries.dropLast(1),
            timeEntryCalendarEntry = lastEntry,
        )
    }
}
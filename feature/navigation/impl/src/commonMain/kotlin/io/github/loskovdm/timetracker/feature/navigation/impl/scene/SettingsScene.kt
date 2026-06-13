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

data class SettingsScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val settingsEntry: NavEntry<T>,
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOf(settingsEntry)
    override val content: @Composable (() -> Unit) = {
        Box(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer)) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surface)
                    .fillMaxSize()
            ) {
                settingsEntry.Content()
            }
        }
    }
    override val metadata: Map<String, Any> =
        when {
            settingsEntry.metadata.contains(EntryMetadata.AuthKey) -> SceneMetadataBuilder.auth()
            settingsEntry.metadata.contains(EntryMetadata.ChangePasswordKey) ->
                SceneMetadataBuilder.changePassword()
            else -> SceneMetadataBuilder.settings()
        }
}

@Composable
fun <T : Any> rememberSettingsSceneStrategy(): SettingsSceneStrategy<T> {
    return remember {
        SettingsSceneStrategy()
    }
}

class SettingsSceneStrategy<T : Any> : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null
        val isSettingsOrAuth = lastEntry.metadata.contains(EntryMetadata.SettingsKey) ||
            lastEntry.metadata.contains(EntryMetadata.AuthKey) ||
            lastEntry.metadata.contains(EntryMetadata.ChangePasswordKey)
        if (!isSettingsOrAuth) {
            return null
        }
        val sceneKey = lastEntry.contentKey

        return SettingsScene(
            key = sceneKey,
            previousEntries = entries.dropLast(1),
            settingsEntry = lastEntry,
        )
    }
}
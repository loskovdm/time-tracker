package io.github.loskovdm.timetracker.feature.navigation.impl.scene

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.rememberLifecycleOwner
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.contains
import androidx.navigation3.runtime.get
import androidx.navigation3.scene.OverlayScene
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadata

@OptIn(ExperimentalMaterial3Api::class)
internal data class EditorBottomSheetScene <T : Any> (
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val editorEntry: NavEntry<T>,
    private val modalBottomSheetProperties: ModalBottomSheetProperties,
    private val onBack: () -> Unit,
) : OverlayScene<T> {
    override val entries: List<NavEntry<T>> = listOf(editorEntry)

    override val content: @Composable (() -> Unit) = {
        val lifecycleOwner = rememberLifecycleOwner()
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
        ModalBottomSheet(
            modifier = if (LocalDeviceConfiguration.current == DeviceConfiguration.MOBILE_LANDSCAPE) {
                Modifier
                    .fillMaxHeight()
                    .windowInsetsPadding(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Top)
                    )
            } else {
                Modifier
            },
            onDismissRequest = onBack,
            properties = modalBottomSheetProperties,
            dragHandle = null,
            sheetState = sheetState,
            contentWindowInsets = { BottomSheetDefaults.windowInsets},
        ) {
            CompositionLocalProvider(LocalLifecycleOwner provides lifecycleOwner) {
                editorEntry.Content()
            }
        }
    }
}

internal data class EditorDialogScene <T : Any> (
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    override val overlaidEntries: List<NavEntry<T>>,
    private val editorEntry: NavEntry<T>,
    private val dialogProperties: DialogProperties,
    private val onBack: () -> Unit,
) : OverlayScene<T> {
    override val entries: List<NavEntry<T>> = listOf(editorEntry)

    override val content: @Composable (() -> Unit) = {
        Dialog(
            onDismissRequest = onBack,
            properties = dialogProperties,
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
            ) {
                editorEntry.Content()
            }
        }
    }
}

@Composable
fun <T : Any> rememberEditorSceneStrategy(): TimeEntryEditorSceneStrategy<T> {
    val deviceConfiguration = LocalDeviceConfiguration.current
    return remember(deviceConfiguration) {
        TimeEntryEditorSceneStrategy(deviceConfiguration = deviceConfiguration)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class TimeEntryEditorSceneStrategy<T: Any>(
    private val deviceConfiguration: DeviceConfiguration
) : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null

        if (!lastEntry.metadata.contains(EntryMetadata.EditorKey)) {
            return null
        }
        val sceneKey = lastEntry.contentKey

        return if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT
            || deviceConfiguration == DeviceConfiguration.MOBILE_LANDSCAPE
        ) {
            @Suppress("UNCHECKED_CAST")
            EditorBottomSheetScene(
                key = sceneKey,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                editorEntry = lastEntry,
                modalBottomSheetProperties = ModalBottomSheetProperties(),
                onBack = onBack,
            )
        } else {
            @Suppress("UNCHECKED_CAST")
            EditorDialogScene(
                key = sceneKey,
                previousEntries = entries.dropLast(1),
                overlaidEntries = entries.dropLast(1),
                editorEntry = lastEntry,
                dialogProperties = DialogProperties(),
                onBack = onBack,
            )
        }
    }
}
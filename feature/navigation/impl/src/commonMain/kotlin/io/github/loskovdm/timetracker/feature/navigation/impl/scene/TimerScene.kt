package io.github.loskovdm.timetracker.feature.navigation.impl.scene

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.contains
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneStrategy
import androidx.navigation3.scene.SceneStrategyScope
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.feature.navigation.api.EntryMetadata
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import io.github.loskovdm.timetracker.feature.navigation.api.SceneMetadataBuilder
import io.github.loskovdm.timetracker.feature.navigation.api.TimeTrackerDestination
import io.github.loskovdm.timetracker.feature.navigation.api.TimerErrorDialogDestination
import io.github.loskovdm.timetracker.feature.navigation.impl.component.timer.ExpandedTimer
import io.github.loskovdm.timetracker.feature.navigation.impl.component.timer.MobileLandscapeTimer
import io.github.loskovdm.timetracker.feature.navigation.impl.component.timer.MobilePortraitTimer
import io.github.loskovdm.timetracker.feature.timeentry.api.destination.TimeEntryEditorDestination
import io.github.loskovdm.timetracker.feature.timeentry.api.presentation.TimerState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi

@Composable
fun <T : Any> rememberTimerSceneStrategy(
    timerState: TimerState,
    onStopTimer: () -> Unit,
    onClearTimerError: () -> Unit,
    onNavigateTo: (TimeTrackerDestination) -> Unit,
): TimerSceneStrategy<T> {
    val timerStateHolder = rememberUpdatedState(timerState)
    return remember(
        onStopTimer,
        onNavigateTo,
    ) {
        TimerSceneStrategy(
            timerState = timerStateHolder,
            onStopTimer = onStopTimer,
            onClearTimerError = onClearTimerError,
            onNavigateTo = onNavigateTo,
        )
    }
}

class TimerSceneStrategy<T : Any>(
    private val timerState: State<TimerState>,
    private val onStopTimer: () -> Unit,
    private val onClearTimerError: () -> Unit,
    private val onNavigateTo: (TimeTrackerDestination) -> Unit,
) : SceneStrategy<T> {
    override fun SceneStrategyScope<T>.calculateScene(entries: List<NavEntry<T>>): Scene<T>? {
        val lastEntry = entries.lastOrNull() ?: return null
        if (!lastEntry.metadata.contains(EntryMetadata.TimeEntriesListKey)) {
            return null
        }
        val sceneKey = lastEntry.contentKey

        return TimerScene(
            key = sceneKey,
            previousEntries = entries.dropLast(1),
            timerState = timerState,
            onStopTimer = onStopTimer,
            onClearTimerError = onClearTimerError,
            onNavigateTo = onNavigateTo,
            timeEntriesListEntry = lastEntry,
        )
    }
}

@OptIn(ExperimentalUuidApi::class)
data class TimerScene<T : Any>(
    override val key: Any,
    override val previousEntries: List<NavEntry<T>>,
    val timerState: State<TimerState>,
    val onStopTimer: () -> Unit,
    val onClearTimerError: () -> Unit,
    val onNavigateTo: (TimeTrackerDestination) -> Unit,
    val timeEntriesListEntry: NavEntry<T>,
) : Scene<T> {
    override val entries: List<NavEntry<T>> = listOf(timeEntriesListEntry)
    override val metadata: Map<String, Any> = SceneMetadataBuilder.timer()

    override val content: @Composable (() -> Unit) = {
        when (val currentTimerState = timerState.value) {
            TimerState.Empty -> {
                TimeEntriesListUnit(modifier = Modifier.fillMaxSize()) {
                    timeEntriesListEntry.Content()
                }
            }
            is TimerState.Error -> {
                val navigator: Navigator = koinInject()
                navigator.goTo(
                    TimerErrorDialogDestination(
                        errorMessage = stringResource(currentTimerState.message),
                        clearError = onClearTimerError,
                    )
                )
            }
            is TimerState.Loaded -> {
                val activeTimeEntry = currentTimerState.timeEntryWithRelations
                val duration = currentTimerState.duration

                when (LocalDeviceConfiguration.current) {
                    DeviceConfiguration.MOBILE_LANDSCAPE -> {
                        Row(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            TimeEntriesListUnit(
                                modifier = Modifier.weight(1f).fillMaxHeight(),
                            ) {
                                timeEntriesListEntry.Content()
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            MobileLandscapeTimer(
                                activeTimeEntry = activeTimeEntry,
                                duration = duration,
                                onClick = {
                                    onNavigateTo(
                                        TimeEntryEditorDestination(
                                            activeTimeEntry.timeEntry.id
                                        )
                                    )
                                },
                                onStop = onStopTimer,
                                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                            )
                        }
                    }

                    DeviceConfiguration.MOBILE_PORTRAIT -> {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            TimeEntriesListUnit(
                                modifier = Modifier.weight(1f),
                            ) {
                                timeEntriesListEntry.Content()
                            }
                            MobilePortraitTimer(
                                activeTimeEntry = activeTimeEntry,
                                duration = duration,
                                onClick = {
                                    onNavigateTo(
                                        TimeEntryEditorDestination(
                                            activeTimeEntry.timeEntry.id
                                        )
                                    )
                                },
                                onStop = onStopTimer,
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                                shape = RectangleShape,
                            )
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            ExpandedTimer(
                                activeTimeEntry = activeTimeEntry,
                                duration = duration,
                                onClick = {
                                    onNavigateTo(
                                        TimeEntryEditorDestination(
                                            activeTimeEntry.timeEntry.id
                                        )
                                    )
                                },
                                onStop = onStopTimer,
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(16.dp),
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            TimeEntriesListUnit(
                                modifier = Modifier.weight(1f),
                            ) {
                                timeEntriesListEntry.Content()
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
private fun TimeEntriesListUnit(
    modifier: Modifier = Modifier,
    timeEntriesListEntryContent: @Composable () -> Unit,
) {
    val deviceConfiguration = LocalDeviceConfiguration.current
    Box(
        modifier = modifier
            .clip(if (deviceConfiguration == DeviceConfiguration.MOBILE_PORTRAIT) {
                    RectangleShape
                } else {
                    RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                })
            .background(MaterialTheme.colorScheme.surface)
    ) {
        timeEntriesListEntryContent()
    }
}
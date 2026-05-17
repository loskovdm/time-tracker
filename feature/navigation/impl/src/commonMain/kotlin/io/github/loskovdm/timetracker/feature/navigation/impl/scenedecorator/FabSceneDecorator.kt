package io.github.loskovdm.timetracker.feature.navigation.impl.scenedecorator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.contains
import androidx.navigation3.runtime.get
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneDecoratorStrategyScope
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.local.LocalFabPadding
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.feature.navigation.api.SceneMetadata
import io.github.loskovdm.timetracker.feature.navigation.api.SceneType
import io.github.loskovdm.timetracker.feature.navigation.api.TimeTrackerDestination
import io.github.loskovdm.timetracker.feature.navigation.impl.component.fab.AddProjectFab
import io.github.loskovdm.timetracker.feature.navigation.impl.component.fab.AddTaskFab
import io.github.loskovdm.timetracker.feature.navigation.impl.component.fab.AddTimeEntryFab
import io.github.loskovdm.timetracker.feature.navigation.impl.component.fab.ShareFab
import io.github.loskovdm.timetracker.feature.navigation.impl.component.fab.StartTimerFab
import io.github.loskovdm.timetracker.feature.timeentry.api.presentation.TimerState
import kotlinx.coroutines.delay
import kotlin.math.abs

internal data class FabScene<T : TimeTrackerDestination>(
    private val scene: Scene<T>,
    private val deviceConfiguration: DeviceConfiguration,
    private val timerState: State<TimerState>,
    private val onStartTimer: () -> Unit,
    private val onAddTimeEntry: () -> Unit,
    private val onAddProject: () -> Unit,
    private val onAddTask: () -> Unit,
    private val onShareReport: () -> Unit,
) : Scene<T> by scene {
    override val key = scene::class to scene.key

    override val metadata = scene.metadata

    override val content = @Composable {
        val timerIsActive = timerState.value is TimerState.Loaded
        val isScrollingDownRaw = remember { mutableStateOf(false) }
        val isScrollingDown = remember { mutableStateOf(isScrollingDownRaw.value) }
        val scrollConnection = remember {
            object : NestedScrollConnection {
                override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                    if (abs(available.y) > 1f) {
                        isScrollingDownRaw.value = available.y < 0f
                    }
                    return Offset.Zero
                }

                override fun onPostScroll(
                    consumed: Offset,
                    available: Offset,
                    source: NestedScrollSource,
                ): Offset {
                    if (abs(consumed.y) > 1f) {
                        isScrollingDownRaw.value = consumed.y < 0f
                    }
                    return Offset.Zero
                }
            }
        }
        LaunchedEffect(isScrollingDownRaw.value) {
            delay(120)
            isScrollingDown.value = isScrollingDownRaw.value
        }

        val isExpanded = !isScrollingDown.value
        Box(modifier = Modifier.fillMaxSize().nestedScroll(scrollConnection)) {
            if (deviceConfiguration != DeviceConfiguration.DESKTOP && !timerIsActive) {
                CompositionLocalProvider(
                    LocalFabPadding provides PaddingValues(bottom = 80.dp)
                ) {
                    scene.content()
                }
            } else {
                scene.content()
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                when (scene.metadata[SceneMetadata.SceneTypeKey]) {
                    SceneType.Timer -> {
                        if (!timerIsActive) {
                            StartTimerFab(
                                onClick = onStartTimer,
                                isExpanded = isExpanded,
                            )
                        }
                    }
                    SceneType.Calendar -> AddTimeEntryFab(
                        onClick = onAddTimeEntry,
                        isExpanded = isExpanded,
                    )
                    SceneType.Projects -> AddProjectFab(
                        isExpanded = isExpanded,
                        onClick = onAddProject,
                    )
                    SceneType.Tasks -> AddTaskFab(
                        onClick = onAddTask,
                        isExpanded = isExpanded,
                    )
                    SceneType.Reports -> ShareFab(
                        onClick = onShareReport,
                        isExpanded = isExpanded,
                    )
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun <T : TimeTrackerDestination> rememberFabSceneDecoratorStrategy(
    timerState: TimerState,
    onStartTimer: () -> Unit,
    onAddTimeEntry: () -> Unit,
    onAddProject: () -> Unit,
    onAddTask: () -> Unit,
    onShareReport: () -> Unit,
) : FabSceneDecoratorStrategy<T> {
    val timerStateHolder = rememberUpdatedState(timerState)
    val deviceConfiguration = LocalDeviceConfiguration.current

    return remember(
        deviceConfiguration,
    ) {
        FabSceneDecoratorStrategy(
            deviceConfiguration = deviceConfiguration,
            timerState = timerStateHolder,
            onStartTimer = onStartTimer,
            onAddTimeEntry = onAddTimeEntry,
            onAddProject = onAddProject,
            onAddTask = onAddTask,
            onShareReport = onShareReport
        )
    }
}

class FabSceneDecoratorStrategy<T : TimeTrackerDestination>(
    private val deviceConfiguration: DeviceConfiguration,
    private val timerState: State<TimerState>,
    private val onStartTimer: () -> Unit,
    private val onAddTimeEntry: () -> Unit,
    private val onAddProject: () -> Unit,
    private val onAddTask: () -> Unit,
    private val onShareReport: () -> Unit,
) : SceneDecoratorStrategy<T> {
    override fun SceneDecoratorStrategyScope<T>.decorateScene(scene: Scene<T>): Scene<T> {
        val timerIsActive = timerState.value is TimerState.Loaded
        return if (
            !scene.metadata.contains(SceneMetadata.SceneTypeKey) ||
                deviceConfiguration == DeviceConfiguration.DESKTOP
        ) {
            scene
        } else {
            FabScene(
                scene = scene,
                deviceConfiguration = deviceConfiguration,
                timerState = timerState,
                onStartTimer = onStartTimer,
                onAddTimeEntry = onAddTimeEntry,
                onAddProject = onAddProject,
                onAddTask = onAddTask,
                onShareReport = onShareReport,
            )
        }
    }
}
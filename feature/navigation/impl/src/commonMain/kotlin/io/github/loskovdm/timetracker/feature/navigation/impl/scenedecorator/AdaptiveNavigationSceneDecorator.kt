package io.github.loskovdm.timetracker.feature.navigation.impl.scenedecorator

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.WideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.get
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneDecoratorStrategyScope
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.system.ApplyNavigationChromeSystemBarColor
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import io.github.loskovdm.timetracker.feature.navigation.api.SceneMetadata
import io.github.loskovdm.timetracker.feature.navigation.api.SceneType
import io.github.loskovdm.timetracker.feature.navigation.api.TimeTrackerDestination
import io.github.loskovdm.timetracker.feature.navigation.impl.component.fab.AddProjectFab
import io.github.loskovdm.timetracker.feature.navigation.impl.component.fab.AddTaskFab
import io.github.loskovdm.timetracker.feature.navigation.impl.component.fab.AddTimeEntryFab
import io.github.loskovdm.timetracker.feature.navigation.impl.component.fab.ShareFab
import io.github.loskovdm.timetracker.feature.navigation.impl.component.fab.StartTimerFab
import io.github.loskovdm.timetracker.feature.navigation.impl.component.fab.StopTimerFab
import io.github.loskovdm.timetracker.feature.navigation.impl.component.navbar.NavigationBar
import io.github.loskovdm.timetracker.feature.navigation.impl.component.navbar.NavigationRail
import io.github.loskovdm.timetracker.feature.navigation.impl.util.NavigationItem
import io.github.loskovdm.timetracker.feature.tasks.api.destination.TaskEditorDestination
import io.github.loskovdm.timetracker.feature.timeentry.api.presentation.TimerState
import org.koin.compose.koinInject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal data class AdaptiveNavigationScene<T : TimeTrackerDestination>(
    private val scene: Scene<T>,
    private val deviceConfiguration: DeviceConfiguration,
    private val railState: WideNavigationRailState,
    private val topLevelDestinations: Map<TimeTrackerDestination, NavigationItem>,
    private val selectedDestination: TimeTrackerDestination,
    private val onSelectedDestination: (TimeTrackerDestination) -> Unit,
    private val timerState: State<TimerState>,
    private val onStartTimer: () -> Unit,
    private val onStopTimer: () -> Unit,
    private val onAddTimeEntry: () -> Unit,
    private val onAddProject: () -> Unit,
    private val onShareReport: () -> Unit,
) : Scene<T> by scene {
    override val key = scene::class to scene.key

    override val metadata = scene.metadata

    override val content = @Composable {
        val showRail = deviceConfiguration != DeviceConfiguration.MOBILE_PORTRAIT
        val showBottomBar = !showRail
        val timerIsActive = timerState.value is TimerState.Loaded

        if (showBottomBar || showRail) {
            ApplyNavigationChromeSystemBarColor()
        }

        val extendedFab: @Composable (isExpanded: Boolean) -> Unit = { isExpanded ->
            val navigator: Navigator = koinInject()

            when (scene.metadata[SceneMetadata.SceneTypeKey]) {
                SceneType.Timer -> {
                    if (timerIsActive) {
                        StopTimerFab(
                            onClick = onStopTimer,
                            isExpanded = isExpanded,
                        )
                    } else {
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
                SceneType.ActiveProjects -> AddProjectFab(
                    onClick = onAddProject,
                    isExpanded = isExpanded,
                )
                SceneType.Tasks -> AddTaskFab(
                    onClick = {
                        val projectId = extractProjectId(scene.key)
                        projectId?.let {
                            navigator.goTo(TaskEditorDestination(
                                projectId = it,
                                taskId = null
                            ))
                        }
                    },
                    isExpanded = isExpanded,
                )
//                SceneType.Reports -> ShareFab(
//                    onClick = onShareReport,
//                    isExpanded = isExpanded,
//                )
                else -> {}
            }
        }

        Row(modifier = Modifier.fillMaxSize()) {
            if (showRail) {
                NavigationRail(
                    state = railState,
                    extendedFab = extendedFab,
                    destinations = topLevelDestinations,
                    selectedDestination = selectedDestination,
                    onSelectedDestination = onSelectedDestination,
                )
            }
            Column(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .consumeWindowInsets(WindowInsets.safeDrawing.only(
                            WindowInsetsSides.Start
                        ))
                ) {
                    scene.content()
                }

                if (showBottomBar) {
                    NavigationBar(
                        destinations = topLevelDestinations,
                        selectedDestination = selectedDestination,
                        onSelectedDestination = onSelectedDestination,
                    )
                }
            }
        }
    }
}

internal data class NoNavigationScene<T : TimeTrackerDestination>(
    private val scene: Scene<T>,
) : Scene<T> by scene {
    override val key = scene::class to scene.key

    override val metadata = scene.metadata

    override val content = @Composable {
        ApplyNavigationChromeSystemBarColor()

        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(start = 16.dp)
        ) {
            scene.content()
        }
    }
}

@Composable
fun <T : TimeTrackerDestination> rememberAdaptiveNavigationSceneDecoratorStrategy(
    timerState: TimerState,
    railState: WideNavigationRailState,
    topLevelDestinations: Map<TimeTrackerDestination, NavigationItem>,
    selectedDestination: TimeTrackerDestination,
    onSelectedDestination: (TimeTrackerDestination) -> Unit,
    onStartTimer: () -> Unit,
    onStopTimer: () -> Unit,
    onAddTimeEntry: () -> Unit,
    onAddProject: () -> Unit,
    onShareReport: () -> Unit,
): AdaptiveNavigationSceneDecoratorStrategy<T> {
    val timerStateHolder = rememberUpdatedState(timerState)
    val deviceConfiguration = LocalDeviceConfiguration.current

    return remember(
        deviceConfiguration,
        topLevelDestinations,
        selectedDestination,
        onSelectedDestination,
    ) {
        AdaptiveNavigationSceneDecoratorStrategy(
            deviceConfiguration = deviceConfiguration,
            railState = railState,
            topLevelDestinations = topLevelDestinations,
            selectedDestination = selectedDestination,
            onSelectedDestination = onSelectedDestination,
            timerState = timerStateHolder,
            onStartTimer = onStartTimer,
            onStopTimer = onStopTimer,
            onAddTimeEntry = onAddTimeEntry,
            onAddProject = onAddProject,
            onShareReport = onShareReport,
        )
    }
}

class AdaptiveNavigationSceneDecoratorStrategy<T : TimeTrackerDestination>(
    private val deviceConfiguration: DeviceConfiguration,
    private val railState: WideNavigationRailState,
    private val topLevelDestinations: Map<TimeTrackerDestination, NavigationItem>,
    private val selectedDestination: TimeTrackerDestination,
    private val onSelectedDestination: (TimeTrackerDestination) -> Unit,
    private val timerState: State<TimerState>,
    private val onStartTimer: () -> Unit,
    private val onStopTimer: () -> Unit,
    private val onAddTimeEntry: () -> Unit,
    private val onAddProject: () -> Unit,
    private val onShareReport: () -> Unit,
) : SceneDecoratorStrategy<T> {
    override fun SceneDecoratorStrategyScope<T>.decorateScene(scene: Scene<T>): Scene<T> {
        return when (scene.metadata[SceneMetadata.SceneTypeKey]) {
            SceneType.Timer,
            SceneType.Calendar,
            SceneType.ActiveProjects,
            SceneType.ArchivedProjects,
            SceneType.Tasks,
            SceneType.Reports -> AdaptiveNavigationScene(
                scene = scene,
                deviceConfiguration = deviceConfiguration,
                railState = railState,
                topLevelDestinations = topLevelDestinations,
                selectedDestination = selectedDestination,
                onSelectedDestination = onSelectedDestination,
                timerState = timerState,
                onStartTimer = onStartTimer,
                onStopTimer = onStopTimer,
                onAddTimeEntry = onAddTimeEntry,
                onAddProject = onAddProject,
                onShareReport = onShareReport,
            )
            else -> if (deviceConfiguration != DeviceConfiguration.MOBILE_PORTRAIT) {
                NoNavigationScene(scene)
            } else {
                scene
            }
        }
    }
}

@OptIn(ExperimentalUuidApi::class)
private fun extractProjectId(sceneKey: Any): Uuid? {
    var current: Any = sceneKey
    while (current is Pair<*, *>) {
        current = current.second ?: return null
    }
    val str = current.toString()
    // Format: "TasksListDestination(projectId=019e..., projectName=...)"
    val marker = "projectId="
    val start = str.indexOf(marker)
    if (start == -1) return null
    val valueStart = start + marker.length
    val end = str.indexOf(',', valueStart).takeIf { it != -1 } ?: str.indexOf(')', valueStart)
    if (end == -1) return null
    val idStr = str.substring(valueStart, end).trim()
    return try {
        Uuid.parse(idStr)
    } catch (_: Exception) {
        null
    }
}
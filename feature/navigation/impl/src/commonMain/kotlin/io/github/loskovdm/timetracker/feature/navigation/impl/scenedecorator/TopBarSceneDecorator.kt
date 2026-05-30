package io.github.loskovdm.timetracker.feature.navigation.impl.scenedecorator

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.navigation3.runtime.contains
import androidx.navigation3.runtime.get
import androidx.navigation3.scene.Scene
import androidx.navigation3.scene.SceneDecoratorStrategy
import androidx.navigation3.scene.SceneDecoratorStrategyScope
import io.github.loskovdm.timetracker.feature.navigation.api.AuthNavigationLock
import io.github.loskovdm.timetracker.feature.navigation.api.AuthTopBarModeSource
import io.github.loskovdm.timetracker.feature.navigation.api.SceneMetadata
import io.github.loskovdm.timetracker.feature.navigation.api.SceneType
import io.github.loskovdm.timetracker.feature.navigation.api.TimeTrackerDestination
import io.github.loskovdm.timetracker.feature.navigation.impl.component.topbar.AuthTopBar
import io.github.loskovdm.timetracker.feature.navigation.impl.component.topbar.ActiveProjectsTopBar
import io.github.loskovdm.timetracker.feature.navigation.impl.component.topbar.ArchivedProjectsTopBar
import io.github.loskovdm.timetracker.feature.navigation.impl.component.topbar.CalendarTopBar
import io.github.loskovdm.timetracker.feature.navigation.impl.component.topbar.ReportsTopBar
import io.github.loskovdm.timetracker.feature.navigation.impl.component.topbar.SettingsTopBar
import io.github.loskovdm.timetracker.feature.navigation.impl.component.topbar.TasksTopBar
import io.github.loskovdm.timetracker.feature.navigation.impl.component.topbar.TimerTopBar
import io.github.loskovdm.timetracker.feature.navigation.impl.scene.ProjectsScene
import kotlin.uuid.ExperimentalUuidApi

@OptIn(ExperimentalMaterial3Api::class)
internal data class TopBarScene<T : TimeTrackerDestination>(
    private val scene: Scene<T>,
    private val scrollBehaviorStore: MutableMap<Any, TopAppBarScrollBehavior>,
    private val authTopBarModeSource: AuthTopBarModeSource,
    private val authNavigationLock: AuthNavigationLock,
    private val onBack: () -> Unit,
    private val onSettings: () -> Unit,
    private val onAddEntry: () -> Unit,
    private val onArchivedProjects: () -> Unit,
) : Scene<T> by scene {
	override val key = scene::class to scene.key

    override val metadata = scene.metadata

    @OptIn(ExperimentalUuidApi::class)
    override val content = @Composable {
        val scrollBehavior = rememberSceneScrollBehavior(
            sceneKey = scene.key,
            scrollBehaviorStore = scrollBehaviorStore
        )
        val isAuthNavigationBlocked by authNavigationLock.isBlockingBack.collectAsStateWithLifecycle()
        val guardedOnBack = {
            if (!isAuthNavigationBlocked) {
                onBack()
            }
        }

        val topBar = @Composable {
            if (scene is ProjectsScene<*> && scene.showTasksInSplitPane) {
                if (scene.isArchived) {
                    ArchivedProjectsTopBar(
                        modifier = Modifier.consumeWindowInsets(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Start)
                        ),
                        scrollBehavior = scrollBehavior,
                        onBack = onBack,
                    )
                } else {
                    ActiveProjectsTopBar(
                        modifier = Modifier.consumeWindowInsets(
                            WindowInsets.safeDrawing.only(WindowInsetsSides.Start)
                        ),
                        scrollBehavior = scrollBehavior,
                        onArchivedProjects = onArchivedProjects,
                        onSettings = onSettings,
                    )
                }
            } else when (scene.metadata[SceneMetadata.SceneTypeKey]) {
                SceneType.Timer -> TimerTopBar(
                    modifier = Modifier.consumeWindowInsets(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Start)
                    ),
                    scrollBehavior = scrollBehavior,
                    onSettings = onSettings,
                    onAddEntry = onAddEntry,
                )
                SceneType.Calendar -> CalendarTopBar(
                    modifier = Modifier.consumeWindowInsets(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Start)
                    ),
                    scrollBehavior = scrollBehavior,
                    onSettings = onSettings,
                )
                SceneType.ActiveProjects -> ActiveProjectsTopBar(
                    modifier = Modifier.consumeWindowInsets(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Start)
                    ),
                    scrollBehavior = scrollBehavior,
                    onArchivedProjects = onArchivedProjects,
                    onSettings = onSettings,
                )
                SceneType.ArchivedProjects -> ArchivedProjectsTopBar(
                    modifier = Modifier.consumeWindowInsets(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Start)
                    ),
                    scrollBehavior = scrollBehavior,
                    onBack = onBack,
                )
                SceneType.Tasks -> TasksTopBar(
                    modifier = Modifier.consumeWindowInsets(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Start)
                    ),
                    scrollBehavior = scrollBehavior,
                    onBack = onBack,
                    projectName = extractProjectName(scene.key),
                )
                SceneType.Reports -> ReportsTopBar(
                    modifier = Modifier.consumeWindowInsets(
                        WindowInsets.safeDrawing.only(WindowInsetsSides.Start)
                    ),
                    scrollBehavior = scrollBehavior,
                    onSettings = onSettings,
                )
                SceneType.Settings -> SettingsTopBar(
                    scrollBehavior = scrollBehavior,
                    onBack = guardedOnBack,
                    backEnabled = !isAuthNavigationBlocked,
                )
                SceneType.Auth -> AuthTopBar(
                    authTopBarModeSource = authTopBarModeSource,
                    scrollBehavior = scrollBehavior,
                    onBack = guardedOnBack,
                    backEnabled = !isAuthNavigationBlocked,
                )
                else -> {}
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            topBar()

            Box(
                modifier = Modifier.consumeWindowInsets(WindowInsets.safeDrawing.only(
                    WindowInsetsSides.Top
                ))
            ) {
                scene.content()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : TimeTrackerDestination> rememberTopBarSceneDecoratorStrategy(
    authTopBarModeSource: AuthTopBarModeSource,
    authNavigationLock: AuthNavigationLock,
    onSettings: () -> Unit,
    onAddEntry: () -> Unit,
    onArchivedProjects: () -> Unit,
): TopBarSceneDecoratorStrategy<T> {
    val scrollBehaviorStore = remember { mutableMapOf<Any, TopAppBarScrollBehavior>() }
    return remember(
        scrollBehaviorStore,
        authTopBarModeSource,
        authNavigationLock,
    ) {
        TopBarSceneDecoratorStrategy(
            scrollBehaviorStore = scrollBehaviorStore,
            authTopBarModeSource = authTopBarModeSource,
            authNavigationLock = authNavigationLock,
            onSettings = onSettings,
            onAddEntry = onAddEntry,
            onArchivedProjects = onArchivedProjects,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
class TopBarSceneDecoratorStrategy<T : TimeTrackerDestination>(
    private val scrollBehaviorStore: MutableMap<Any, TopAppBarScrollBehavior>,
    private val authTopBarModeSource: AuthTopBarModeSource,
    private val authNavigationLock: AuthNavigationLock,
    private val onSettings: () -> Unit,
    private val onAddEntry: () -> Unit,
    private val onArchivedProjects: () -> Unit,
) : SceneDecoratorStrategy<T> {
    override fun SceneDecoratorStrategyScope<T>.decorateScene(scene: Scene<T>): Scene<T> {
        return if (!scene.metadata.contains(SceneMetadata.SceneTypeKey)) {
            scene
        } else {
            TopBarScene(
                scene = scene,
                scrollBehaviorStore = scrollBehaviorStore,
                authTopBarModeSource = authTopBarModeSource,
                authNavigationLock = authNavigationLock,
                onBack = onBack,
                onSettings = onSettings,
                onAddEntry = onAddEntry,
                onArchivedProjects = onArchivedProjects,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun rememberSceneScrollBehavior(
    sceneKey: Any,
    scrollBehaviorStore: MutableMap<Any, TopAppBarScrollBehavior>,
): TopAppBarScrollBehavior {
    val existing = scrollBehaviorStore[sceneKey]
    if (existing != null) {
        return existing
    }
    val created = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    SideEffect {
        scrollBehaviorStore[sceneKey] = created
    }
    return created
}

private fun extractProjectName(sceneKey: Any): String? {
    var current = sceneKey
    while (current is Pair<*, *>) {
        current = current.second ?: return null
    }
    val str = current.toString()
    // Format: "TasksListDestination(projectId=..., projectName=...)"
    val marker = "projectName="
    val start = str.indexOf(marker)
    if (start == -1) return null
    val valueStart = start + marker.length
    // Looking for a closing parenthesis or comma
    val end = str
        .indexOf(',', valueStart)
        .takeIf { it != -1 } ?: str.indexOf(')', valueStart)
    if (end == -1) return null
    var result = str.substring(valueStart, end).trim()
    // Remove quotation marks if there are any
    if (result.startsWith('"') && result.endsWith('"')) {
        result = result.substring(1, result.length - 1)
    }
    return result.takeIf { it.isNotEmpty() }
}
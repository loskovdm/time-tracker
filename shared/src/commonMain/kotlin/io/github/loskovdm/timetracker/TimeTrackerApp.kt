package io.github.loskovdm.timetracker

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.rememberWideNavigationRailState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.ui.NavDisplay
import io.github.loskovdm.designsystem.component.ErrorDialog
import io.github.loskovdm.designsystem.locale.AppLocaleProvider
import io.github.loskovdm.designsystem.local.LocalDeviceConfiguration
import io.github.loskovdm.designsystem.theme.AppTheme
import io.github.loskovdm.domain.model.AppLanguage
import io.github.loskovdm.domain.model.ThemeMode
import io.github.loskovdm.domain.usecase.settings.ObserveAppLanguageUseCase
import io.github.loskovdm.domain.usecase.settings.ObserveThemeModeUseCase
import io.github.loskovdm.designsystem.util.DeviceConfiguration
import io.github.loskovdm.timetracker.feature.navigation.api.AuthNavigationLock
import io.github.loskovdm.timetracker.feature.navigation.api.AuthTopBarModeSource
import io.github.loskovdm.timetracker.feature.navigation.api.ChangePasswordTopBarSource
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import io.github.loskovdm.timetracker.feature.navigation.api.TimeTrackerDestination
import io.github.loskovdm.timetracker.feature.navigation.api.toEntries
import io.github.loskovdm.timetracker.feature.navigation.impl.scene.rememberCalendarSceneStrategy
import io.github.loskovdm.timetracker.feature.navigation.impl.scene.rememberEditorSceneStrategy
import io.github.loskovdm.timetracker.feature.navigation.impl.scene.rememberProjectsSceneStrategy
import io.github.loskovdm.timetracker.feature.navigation.impl.scene.rememberReportsSceneStrategy
import io.github.loskovdm.timetracker.feature.navigation.impl.scene.rememberSettingsSceneStrategy
import io.github.loskovdm.timetracker.feature.navigation.impl.scene.rememberTimerSceneStrategy
import io.github.loskovdm.timetracker.feature.navigation.impl.scenedecorator.rememberAdaptiveNavigationSceneDecoratorStrategy
import io.github.loskovdm.timetracker.feature.navigation.impl.scenedecorator.rememberFabSceneDecoratorStrategy
import io.github.loskovdm.timetracker.feature.navigation.impl.scenedecorator.rememberSafeAreaContentSceneDecoratorStrategy
import io.github.loskovdm.timetracker.feature.navigation.impl.scenedecorator.rememberTopBarSceneDecoratorStrategy
import io.github.loskovdm.timetracker.feature.projects.api.destination.ArchivedProjectsListDestination
import io.github.loskovdm.timetracker.feature.projects.api.destination.ProjectEditorDestination
import io.github.loskovdm.timetracker.feature.settings.api.SettingsDestination
import io.github.loskovdm.timetracker.feature.timeentry.api.destination.TimeEntryEditorDestination
import io.github.loskovdm.timetracker.feature.timeentry.api.presentation.TimerState
import io.github.loskovdm.timetracker.feature.timeentry.api.presentation.TimerViewModel
import org.jetbrains.compose.resources.stringResource
import io.github.loskovdm.timetracker.session.GuestMigrationCoordinator
import io.github.loskovdm.timetracker.ui.GuestMigrationDialog
import io.github.loskovdm.timetracker.util.isDesktopPlatform
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI
import kotlin.uuid.ExperimentalUuidApi

@OptIn(KoinExperimentalAPI::class, ExperimentalMaterial3Api::class, ExperimentalUuidApi::class)
@Composable
fun TimeTrackerApp() {
    val deviceConfiguration = if (isDesktopPlatform()) {
        DeviceConfiguration.DESKTOP
    } else {
        DeviceConfiguration.fromWindowSizeClass(
            currentWindowAdaptiveInfo().windowSizeClass
        )
    }

    val timerViewModel: TimerViewModel = koinInject()
    val timerState by timerViewModel.state.collectAsStateWithLifecycle()
    val observeThemeModeUseCase: ObserveThemeModeUseCase = koinInject()
    val observeAppLanguageUseCase: ObserveAppLanguageUseCase = koinInject()
    val themeMode by observeThemeModeUseCase()
        .collectAsStateWithLifecycle(initialValue = ThemeMode.SYSTEM)
    val appLanguage by observeAppLanguageUseCase()
        .collectAsStateWithLifecycle(initialValue = AppLanguage.ENGLISH)
    val useDarkTheme = when (themeMode) {
        ThemeMode.DARK -> true
        ThemeMode.LIGHT -> false
        ThemeMode.SYSTEM -> isSystemInDarkTheme()
    }
    val localeTag = appLanguage.tag
    val guestMigrationCoordinator: GuestMigrationCoordinator = koinInject()
    val showGuestMigration by guestMigrationCoordinator.showDialog.collectAsStateWithLifecycle()
    val guestMigrationProcessing by guestMigrationCoordinator.isProcessing.collectAsStateWithLifecycle()

    CompositionLocalProvider(LocalDeviceConfiguration provides deviceConfiguration) {
        AppLocaleProvider(localeTag = localeTag) {
            AppTheme(darkTheme = useDarkTheme) {
                val navigator: Navigator = koinInject()
                val entryProvider = koinEntryProvider<TimeTrackerDestination>()

                val railState = rememberWideNavigationRailState()

                val timerSceneStrategy = rememberTimerSceneStrategy<TimeTrackerDestination>(
                    timerState = timerState,
                    onStopTimer = timerViewModel::stopTimer,
                    onClearTimerError = timerViewModel::clearError,
                    onNavigateTo = { destination -> navigator.goTo(destination) },
                )
                val calendarSceneStrategy = rememberCalendarSceneStrategy<TimeTrackerDestination>()
                val editorSceneStrategy = rememberEditorSceneStrategy<TimeTrackerDestination>()
                val projectsSceneStrategy = rememberProjectsSceneStrategy<TimeTrackerDestination>()
                val reportsSceneStrategy = rememberReportsSceneStrategy<TimeTrackerDestination>()
                val settingsSceneStrategy = rememberSettingsSceneStrategy<TimeTrackerDestination>()

                val safeAreaContentSceneDecoratorStrategy =
                    rememberSafeAreaContentSceneDecoratorStrategy<TimeTrackerDestination>()
                val fabSceneDecoratorStrategy =
                    rememberFabSceneDecoratorStrategy<TimeTrackerDestination>(
                        timerState = timerState,
                        onStartTimer = { timerViewModel.startTimer() },
                        onAddTimeEntry = { navigator.goTo(TimeEntryEditorDestination()) },
                        onAddProject = { navigator.goTo(ProjectEditorDestination()) },
                        onShareReport = {},
                    )
                val authTopBarModeSource: AuthTopBarModeSource = koinInject()
                val changePasswordTopBarSource: ChangePasswordTopBarSource = koinInject()
                val authNavigationLock: AuthNavigationLock = koinInject()
                val isAuthNavigationBlocked by authNavigationLock.isBlockingBack.collectAsStateWithLifecycle()
                val topBarSceneDecoratorStrategy =
                    rememberTopBarSceneDecoratorStrategy<TimeTrackerDestination>(
                        authTopBarModeSource = authTopBarModeSource,
                        changePasswordTopBarSource = changePasswordTopBarSource,
                        authNavigationLock = authNavigationLock,
                        onSettings = { navigator.goTo(SettingsDestination) },
                        onAddEntry = { navigator.goTo(TimeEntryEditorDestination()) },
                        onArchivedProjects = { navigator.goTo(ArchivedProjectsListDestination) },
                    )
                val timerErrorState = timerState as? TimerState.Error
                if (timerErrorState != null) {
                    ErrorDialog(
                        message = stringResource(timerErrorState.message),
                        onDismiss = timerViewModel::clearError,
                    )
                }

                val adaptiveNavigationSceneDecoratorStrategy =
                    rememberAdaptiveNavigationSceneDecoratorStrategy<TimeTrackerDestination>(
                        timerState = timerState,
                        railState = railState,
                        topLevelDestinations = TOP_LEVEL_DESTINATIONS,
                        selectedDestination = navigator.currentTopLevelDestination,
                        onSelectedDestination = { navigator.goTo(it) },
                        onStartTimer = { timerViewModel.startTimer() },
                        onStopTimer = { timerViewModel.stopTimer() },
                        onAddTimeEntry = { navigator.goTo(TimeEntryEditorDestination()) },
                        onAddProject = { navigator.goTo(ProjectEditorDestination()) },
                        onShareReport = {},
                    )

                if (showGuestMigration) {
                    GuestMigrationDialog(
                        isProcessing = guestMigrationProcessing,
                        onMigrate = guestMigrationCoordinator::onMigrate,
                        onDiscard = guestMigrationCoordinator::onDiscard,
                    )
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    NavDisplay(
                        entries = navigator.toEntries(entryProvider),
                        sceneStrategies = listOf(
                            timerSceneStrategy,
                            calendarSceneStrategy,
                            editorSceneStrategy,
                            projectsSceneStrategy,
                            reportsSceneStrategy,
                            settingsSceneStrategy,
                        ),
                        sceneDecoratorStrategies = listOf(
                            fabSceneDecoratorStrategy,
                            safeAreaContentSceneDecoratorStrategy,
                            topBarSceneDecoratorStrategy,
                            adaptiveNavigationSceneDecoratorStrategy,
                        ),
                        onBack = {
                            if (!isAuthNavigationBlocked) {
                                navigator.goBack()
                            }
                        },
                    )
                }
            }
        }
    }
}

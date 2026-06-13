package io.github.loskovdm.timetracker.di

import io.github.loskovdm.timetracker.TOP_LEVEL_DESTINATIONS
import io.github.loskovdm.timetracker.datastore.datastoreModule
import io.github.loskovdm.timetracker.feature.navigation.api.AuthNavigationLock
import io.github.loskovdm.timetracker.feature.navigation.api.AuthTopBarModeSource
import io.github.loskovdm.timetracker.feature.navigation.api.ChangePasswordTopBarSource
import io.github.loskovdm.timetracker.feature.navigation.api.DefaultChangePasswordTopBarSource
import io.github.loskovdm.timetracker.feature.navigation.api.DefaultAuthNavigationLock
import io.github.loskovdm.timetracker.feature.navigation.api.DefaultAuthTopBarModeSource
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import io.github.loskovdm.timetracker.feature.projects.impl.di.projectsModule
import io.github.loskovdm.timetracker.feature.reports.impl.di.reportsModule
import io.github.loskovdm.timetracker.feature.auth.impl.di.authModule
import io.github.loskovdm.timetracker.feature.settings.impl.di.settingsModule
import io.github.loskovdm.timetracker.feature.tasks.impl.di.tasksModule
import io.github.loskovdm.timetracker.feature.timeentry.api.destination.TimeEntriesListDestination
import io.github.loskovdm.timetracker.feature.timeentry.impl.di.timeEntryModule
import org.koin.dsl.module

val appModule = module {
    includes(
        datastoreModule,
        remoteIntegrationModule,
        timeEntryModule,
        projectsModule,
        tasksModule,
        reportsModule,
        settingsModule,
        authModule,
    )

    single<AuthTopBarModeSource> { DefaultAuthTopBarModeSource() }

    single<ChangePasswordTopBarSource> { DefaultChangePasswordTopBarSource() }

    single<AuthNavigationLock> { DefaultAuthNavigationLock() }

    single {
        Navigator(
            startDestination = TimeEntriesListDestination,
            topLevelDestinations = TOP_LEVEL_DESTINATIONS.keys
        )
    }
}
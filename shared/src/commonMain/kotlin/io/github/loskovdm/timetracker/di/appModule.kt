package io.github.loskovdm.timetracker.di

import io.github.loskovdm.timetracker.TOP_LEVEL_DESTINATIONS
import io.github.loskovdm.timetracker.database.di.databaseModule
import io.github.loskovdm.timetracker.datastore.datastoreModule
import io.github.loskovdm.timetracker.feature.navigation.api.AuthTopBarModeSource
import io.github.loskovdm.timetracker.feature.navigation.api.DefaultAuthTopBarModeSource
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import io.github.loskovdm.timetracker.feature.projects.impl.di.projectsModule
import io.github.loskovdm.timetracker.feature.reports.impl.di.reportsModule
import io.github.loskovdm.timetracker.feature.settings.impl.di.settingsModule
import io.github.loskovdm.timetracker.feature.tasks.impl.di.tasksModule
import io.github.loskovdm.timetracker.feature.timeentry.api.destination.TimeEntriesListDestination
import io.github.loskovdm.timetracker.feature.timeentry.impl.di.timeEntryModule
import org.koin.dsl.module

val appModule = module {
    includes(
        datastoreModule,
        databaseModule,
        timeEntryModule,
        projectsModule,
        tasksModule,
        reportsModule,
        settingsModule,
    )

    single<AuthTopBarModeSource> { DefaultAuthTopBarModeSource() }

    single {
        Navigator(
            startDestination = TimeEntriesListDestination,
            topLevelDestinations = TOP_LEVEL_DESTINATIONS.keys
        )
    }
}
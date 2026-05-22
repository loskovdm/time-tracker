package io.github.loskovdm.timetracker.di

import io.github.loskovdm.timetracker.TOP_LEVEL_DESTINATIONS
import io.github.loskovdm.timetracker.database.di.databaseModule
import io.github.loskovdm.timetracker.feature.projects.impl.di.projectsModule
import io.github.loskovdm.timetracker.feature.reports.impl.di.reportsModule
import io.github.loskovdm.timetracker.feature.timeentry.api.destination.TimeEntriesListDestination
import io.github.loskovdm.timetracker.feature.timeentry.impl.di.timeEntryModule
import io.github.loskovdm.timetracker.feature.navigation.api.Navigator
import io.github.loskovdm.timetracker.feature.navigation.impl.di.navigationModule
import io.github.loskovdm.timetracker.feature.settings.impl.di.settingsModule
import io.github.loskovdm.timetracker.feature.tasks.impl.di.tasksModule
import org.koin.dsl.module

val appModule = module {
    includes(
        databaseModule,
        timeEntryModule,
        projectsModule,
        tasksModule,
        reportsModule,
        settingsModule,
        navigationModule,
    )

    single {
        Navigator(
            startDestination = TimeEntriesListDestination,
            topLevelDestinations = TOP_LEVEL_DESTINATIONS.keys
        )
    }
}
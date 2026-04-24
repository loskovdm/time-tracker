package io.github.loskovdm.timetracker.di

import io.github.loskovdm.projects.di.projectsModule
import io.github.loskovdm.timer.di.timerModule
import io.github.loskovdm.timetracker.database.di.databaseModule
import org.koin.dsl.module

val appModule = module {
    includes(
        timerModule,
        projectsModule,
        databaseModule,
    )
}
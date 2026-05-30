package io.github.loskovdm.timetracker.powersyncclient.di

import io.github.loskovdm.timetracker.database.TimeTrackerDatabase
import io.github.loskovdm.timetracker.powersyncclient.PowerSyncEngine
import io.github.loskovdm.timetracker.powersyncclient.PowerSyncEngineImpl
import org.koin.dsl.module

val powersyncClientModule = module {
    single<PowerSyncEngine> {
        PowerSyncEngineImpl(
            remoteConfig = get(),
            supabaseClient = get(),
        )
    }

    single<TimeTrackerDatabase> {
        get<PowerSyncEngine>().roomDatabase
    }
}

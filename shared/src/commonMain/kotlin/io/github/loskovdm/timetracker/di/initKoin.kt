package io.github.loskovdm.timetracker.di

import io.github.loskovdm.timetracker.supabase.RemoteConfig
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.includes
import org.koin.dsl.module

fun initKoin(
    remoteConfig: RemoteConfig,
    config: KoinAppDeclaration? = null,
): KoinApplication {
    return startKoin {
        includes(config)
        modules(
            module { single { remoteConfig } },
            appModule,
        )
    }
}

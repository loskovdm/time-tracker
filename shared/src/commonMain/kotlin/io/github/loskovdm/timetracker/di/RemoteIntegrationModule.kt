package io.github.loskovdm.timetracker.di

import io.github.loskovdm.domain.auth.CurrentUserIdProvider
import io.github.loskovdm.domain.repository.SyncRepository
import io.github.loskovdm.timetracker.auth.CurrentUserIdProviderImpl
import io.github.loskovdm.timetracker.database.di.databaseBindingsModule
import io.github.loskovdm.timetracker.powersyncclient.di.powersyncClientModule
import io.github.loskovdm.timetracker.remote.RemoteConfig
import io.github.loskovdm.timetracker.supabaseclient.di.supabaseClientModule
import io.github.loskovdm.timetracker.feature.navigation.api.AuthNavigationLock
import io.github.loskovdm.timetracker.session.AppSessionCoordinator
import io.github.loskovdm.timetracker.session.GuestMigrationCoordinator
import io.github.loskovdm.timetracker.sync.SyncRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.module.dsl.createdAtStart
import org.koin.dsl.module

val remoteIntegrationModule = module {
    includes(supabaseClientModule, powersyncClientModule, databaseBindingsModule)

    single<CoroutineScope> {
        CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    single<SyncRepository> {
        SyncRepositoryImpl(engine = get())
    }

    single<CurrentUserIdProvider> {
        CurrentUserIdProviderImpl(authRepository = get())
    }

    single(createdAtStart = true) {
        GuestMigrationCoordinator(
            observeAuthStateUseCase = get(),
            hasGuestLocalDataUseCase = get(),
            migrateGuestDataUseCase = get(),
            discardGuestDataUseCase = get(),
            authNavigationLock = get(),
            scope = get(),
        )
    }

    single(createdAtStart = true) {
        AppSessionCoordinator(
            observeAuthStateUseCase = get(),
            guestMigrationCoordinator = get(),
            startSyncUseCase = get(),
            syncRepository = get(),
            scope = get(),
        )
    }
}

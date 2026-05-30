package io.github.loskovdm.timetracker.datastore

import io.github.loskovdm.domain.repository.UserSettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.bind
import org.koin.dsl.module

actual val datastorePlatformModule = module {
    single { AndroidUserSettingsRepository(androidContext()) } bind UserSettingsRepository::class
}

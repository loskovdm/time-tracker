package io.github.loskovdm.timetracker.datastore

import io.github.loskovdm.domain.repository.UserSettingsRepository
import org.koin.dsl.bind
import org.koin.dsl.module

actual val datastorePlatformModule = module {
    single { JvmUserSettingsRepository() } bind UserSettingsRepository::class
}
